package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.TableConfig;
import nl.antimeta.bukkit.framework.util.LogUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dao<T extends BaseEntity> {

    private Database database;
    private TableConfig<T> tableConfig;
    private Class<T> tClass;
    private Entity entity;
    private T entityObject;

    public Dao(Database database, Class<T> tClass) throws Exception {
        database.getDaoManger().registerDao(tClass, this);
        this.database = database;
        this.tClass = tClass;
        entity = tClass.getAnnotation(Entity.class);
        if (entity != null) {
            tableConfig = new TableConfig<>();
            tableConfig.setDatabaseType(database.getDatabaseType());
            tableConfig.setTableName(entity.tableName());
            initFieldConfig();
        }
    }

    private void initFieldConfig() {
        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            Field field = entityField.getAnnotation(Field.class);
            if (field != null) {
                entityField.setAccessible(true);

                String fieldName;
                if (StringUtils.isBlank(field.fieldName())) {
                    fieldName = entityField.getName();
                } else {
                    fieldName = field.fieldName();
                }

                FieldConfig<T> fieldConfig = new FieldConfig<>();
                fieldConfig.setField(entityField);
                fieldConfig.setFieldName(fieldName);
                fieldConfig.setFieldType(field.fieldType());
                fieldConfig.setPrimary(field.primary());
                fieldConfig.setSize(field.size());
                fieldConfig.setDigitSize(field.digitSize());

                fieldConfig.setForeign(field.foreign());
                if (fieldConfig.isForeign()) {
                    fieldConfig.setForeignClass(entityField.getClass());
                    fieldConfig.setForeignDao(database.getDaoManger().findDao(entityField.getType()));
                    fieldConfig.setForeignAutoLoad(field.foreignAutoLoad());
                }

                tableConfig.getFieldConfigs().put(fieldName, fieldConfig);
            }
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

    public List<T> find(Number id) throws SQLException {
        String sql = buildFindPrimaryKeySql(id);
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> find(String field, Object value) throws SQLException {
        String sql = buildFind(field, value);
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> find(Map<String, Object> parameters) throws SQLException {
        String sql = buildFind(parameters);
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    private boolean create() throws SQLException {
        String sql = buildInsert();
        LogUtil.info(sql);
        return executeNoResult(sql);
    }

    private boolean update(T entity) throws SQLException {
        String sql = buildUpdate(entity.getId());
        LogUtil.info(sql);
        return executeNoResult(sql);
    }

    public boolean save(T entity) throws SQLException {
        this.entityObject = entity;
        if (entity.getId() == null) {
            return create();
        } else {
            return update(entity);
        }
    }

    public boolean delete(T entity) throws SQLException {
        return delete(entity.getId());
    }

    public boolean delete(Number id) throws SQLException {
        if (id != null) {
            String sql = buildDelete(id);
            LogUtil.info(sql);
            return executeNoResult(sql);
        }

        return false;
    }

    public boolean delete(String field, Object value) throws SQLException {
        String sql = buildDelete(field, value);
        LogUtil.info(sql);
        return executeNoResult(sql);
    }

    public boolean delete(Map<String, Object> parameters) throws SQLException {
        String sql = buildDelete(parameters);
        LogUtil.info(sql);
        return executeNoResult(sql);
    }

    private List<T> processResultSet(ResultSet resultSet) {
        try {
            List<T> results = new ArrayList<>();

            while (resultSet.next()) {
                T result = tClass.newInstance();
                for (FieldConfig<T> fieldConfig : tableConfig.getFieldConfigs().values()) {
                    if (fieldConfig.isForeign()) {
                        if (fieldConfig.isForeignAutoLoad()) {
                            List<?> foreignResult = fieldConfig.getForeignDao().find((Number) resultSet.getObject(fieldConfig.getFieldName()));
                            if (!foreignResult.isEmpty()) {
                                fieldConfig.setFieldValue(result, foreignResult.get(0));
                            }
                        }
                    } else {
                        Object resultFieldValue = resultSet.getObject(fieldConfig.getFieldName());
                        fieldConfig.setFieldValue(result, fieldConfig.getField().getType().cast(resultFieldValue));
                    }
                }
                results.add(result);
            }

            return results;
        } catch (InstantiationException | SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildFindPrimaryKeySql(Number id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableConfig.getTableName()).append("\n");

        FieldConfig fieldConfig = tableConfig.getPrimaryFieldConfig();
        if (fieldConfig != null) {
            sql.append(" WHERE ").append(fieldConfig.getFieldName()).append(" = '").append(id).append("'");
            return sql.toString();
        }
        return null;
    }

    private String buildFind(String field, Object value) {
        return "SELECT * FROM " + tableConfig.getTableName() + "\n" +
                " WHERE " + field + " = '" + value + "'";
    }

    private String buildFind(Map<String, Object> parameters) {
        if (parameters.isEmpty()) {
            return null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(entity.tableName()).append("\n");

        boolean first = true;
        for (Map.Entry<String, Object> set : parameters.entrySet()) {
            if (first) {
                sql.append(" WHERE ").append(set.getKey()).append(" = '").append(set.getValue()).append("'\n");
                first = false;
            } else {
                sql.append(" AND ").append(set.getKey()).append(" = '").append(set.getValue()).append("'\n");
            }
        }

        return sql.toString();
    }

    private String buildInsert() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableConfig.getTableName()).append(" \n");

        Map<String, String> fieldMap = new HashMap<>();

        for (FieldConfig<T> fieldConfig : tableConfig.getFieldConfigs().values()) {
            if (fieldConfig.isForeign()) {
                try {
                    BaseEntity foreignValue = (BaseEntity) fieldConfig.getFieldValue(entityObject);
                    fieldMap.put(fieldConfig.getFieldName(), String.valueOf(foreignValue.getId()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (!fieldConfig.isPrimary()) {
                fieldMap.put(fieldConfig.getFieldName(), runGetter(fieldConfig));
            }
        }

        StringBuilder fieldNames = new StringBuilder("(");
        StringBuilder fieldValues = new StringBuilder("VALUES (");
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

        sql.append(fieldNames).append("\n");
        sql.append(fieldValues);

        return sql.toString();
    }

    private String buildUpdate(Number id) {
        StringBuilder sql = new StringBuilder();
        String where = "";
        sql.append("UPDATE ").append(this.entity.tableName()).append(" SET ");

        boolean firstField = true;
        try {
            for (FieldConfig<T> fieldConfig : tableConfig.getFieldConfigs().values()) {
                if (fieldConfig.isForeign()) {
                    BaseEntity foreignValue = (BaseEntity) fieldConfig.getFieldValue(entityObject);
                    if (firstField) {
                        sql.append(fieldConfig.getFieldName()).append(" = ").append(foreignValue.getId());
                        firstField = false;
                    } else {
                        sql.append(", ").append(fieldConfig.getFieldName()).append(" = ").append(foreignValue.getId());
                    }
                } else if (!fieldConfig.isPrimary()) {
                    if (firstField) {
                        sql.append(fieldConfig.getFieldName()).append(" = ").append(runGetter(fieldConfig));
                        firstField = false;
                    } else {
                        sql.append(", ").append(fieldConfig.getFieldName()).append(" = ").append(runGetter(fieldConfig));
                    }
                } else {
                    where = " WHERE " + fieldConfig.getFieldName() + " = '" + id + "'";
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        sql.append(where);

        return sql.toString();
    }

    private String buildDelete(Number id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableConfig.getTableName());
        for (FieldConfig fieldConfig : tableConfig.getFieldConfigs().values()) {
            if (fieldConfig.isPrimary()) {
                sql.append(" WHERE ").append(fieldConfig.getFieldName()).append(" = '").append(id).append("'");
                return sql.toString();
            }
        }

        return null;
    }

    private String buildDelete(String field, Object value) {
        return "DELETE FROM " + tableConfig.getTableName() +
                " WHERE " + field + " = '" + value + "'";
    }

    private String buildDelete(Map<String, Object> parameters) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableConfig.getTableName());

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

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    private String runGetter(FieldConfig<T> fieldConfig) {
        try {
            Object object = fieldConfig.getFieldValue(entityObject);
            if (object != null) {
                return object.toString();
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            LogUtil.error("Error running getter!!" + e.getMessage());
        }
        return null;
    }
}
