package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.TableConfig;
import nl.antimeta.bukkit.framework.exception.FieldNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Dao<T extends BaseEntity> {

    private Database database;
    private TableConfig tableConfig;
    private Class<T> tClass;
    private Entity entity;
    private T entityObject;

    public Dao(Database database, Class<T> tClass) throws Exception {
        this.database = database;
        this.tClass = tClass;
        entity = tClass.getAnnotation(Entity.class);
        if (entity != null) {
            tableConfig = new TableConfig();
            tableConfig.setDatabaseType(database.getDatabaseType());
            tableConfig.setTableName(entity.tableName());
            initFieldConfig();
        }
    }

    private void initFieldConfig() {
        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            Field field = entityField.getAnnotation(Field.class);
            FieldConfig fieldConfig = new FieldConfig();
            fieldConfig.setFieldName(field.name());
            fieldConfig.setFieldType(field.fieldType());
            fieldConfig.setPrimary(field.primary());
            fieldConfig.setSize(field.size());
            fieldConfig.setDigitSize(field.digitSize());
            fieldConfig.setGet(findGetter(entityField));
            fieldConfig.setSet(findSetter(entityField));
            tableConfig.getFieldConfigs().add(fieldConfig);
        }
    }

    public ResultSet execute(String sql) throws SQLException {
        PreparedStatement stmt = database.openConnection().prepareStatement(sql);
        return stmt.executeQuery();
    }

    public boolean executeNoResult(String sql) throws SQLException {
        PreparedStatement stmt = database.openConnection().prepareStatement(sql);
        return stmt.execute();
    }

    public T find(int id) throws SQLException {
        String sql = buildFindPrimaryKeySql(id);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public T find(String field, Object value) throws SQLException {
        String sql = buildFind(field, value);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public T find(Map<String, Object> parameters) throws SQLException {
        String sql = buildFind(parameters);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    private boolean create() throws SQLException {
        String sql = buildInsert();
        return executeNoResult(sql);
    }

    private boolean update() throws SQLException {
        String sql = buildUpdate();
        return executeNoResult(sql);
    }

    public boolean save(T entity) throws SQLException, FieldNotFoundException {
        this.entityObject = entity;
        if (entity.getId() == null) {
            return create();
        } else {
            return update();
        }
    }

    public boolean delete(T entity) {
        return delete(entity.getId());
    }

    public boolean delete(Integer id) {
        //TODO
        if (id == null) {
            return false;
        }

        return false;
    }

    public boolean delete(String field, Object value) {
        return false;
    }

    public boolean delete(Map<String, Object> parameters) {
        return false;
    }

    private T processResultSet(ResultSet resultSet) {
        T result = null;
        try {
            result = tClass.newInstance();
            result.buildResultSet(resultSet);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildFindPrimaryKeySql(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableConfig.getTableName());

        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            Field field = entityField.getAnnotation(Field.class);
            if (field != null) {
                if (field.primary()) {
                    sql.append(" WHERE ").append(field.name()).append(" = '").append(id).append("'");
                    return sql.toString();
                }
            }
        }
        return null;
    }

    private String buildFind(String field, Object value) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableConfig.getTableName());
        sql.append(" WHERE ").append(field).append(" = '").append(value).append("'");
        return sql.toString();
    }

    private String buildFind(Map<String, Object> parameters) {
        if (parameters.isEmpty()) {
            return null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(entity.tableName());

        boolean first = true;
        for (Map.Entry<String, Object> set : parameters.entrySet()) {
            if (first) {
                sql.append(" WHERE ").append(set.getKey()).append(" = '").append(set.getValue()).append("'");
                first = false;
            } else {
                sql.append(" AND ").append(set.getKey()).append(" = '").append(set.getValue()).append("'");
            }
        }

        return sql.toString();
    }

    private String buildInsert() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableConfig.getTableName()).append(" ");

        Map<String, String> fieldMap = new HashMap<>();

        for (FieldConfig fieldConfig : tableConfig.getFieldConfigs()) {
            if (!fieldConfig.isPrimary()) {
                fieldMap.put(fieldConfig.getFieldName(), runGetter(fieldConfig));
            }
        }

        StringBuilder fieldNames = new StringBuilder("(");
        StringBuilder fieldValues = new StringBuilder("(");
        boolean firstField = true;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            if (firstField) {
                fieldNames.append(entry.getKey());
                fieldValues.append("'").append(entry.getValue()).append("'");
                firstField = false;
            } else {
                fieldNames.append(", ").append(entry.getKey());
                fieldValues.append(", '").append(entry.getValue()).append("'");
            }
        }

        fieldNames.append(") ");
        fieldValues.append(") ");

        sql.append(fieldNames);
        sql.append(fieldValues);

        return sql.toString();
    }

    private String buildUpdate() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(this.entity.tableName()).append(" SET ");

        boolean firstField = true;
        for (FieldConfig fieldConfig : tableConfig.getFieldConfigs()) {
            if (fieldConfig.isPrimary()) {
                if (firstField) {
                    sql.append(fieldConfig.getFieldName()).append(" = ").append(runGetter(fieldConfig));
                    firstField = false;
                } else {
                    sql.append(", ").append(fieldConfig.getFieldName()).append(" = ").append(runGetter(fieldConfig));
                }
            }
        }

        return sql.toString();
    }

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    private String runGetter(FieldConfig fieldConfig) {
        try {
            Object object = fieldConfig.getGet().invoke(entityObject);
            return object.toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            //TODO log error
            e.printStackTrace();
        }
        return null;
    }

    private Method findGetter(java.lang.reflect.Field field) {
        for (Method method : tClass.getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    return method;
                }
            }
        }
        return null;
    }

    private Method findSetter(java.lang.reflect.Field field) {
        for (Method method : tClass.getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    return method;
                }
            }
        }
        return null;
    }
}
