package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.TableConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Dao<T extends BaseEntity> {

    private Database database;
    private TableConfig tableConfig;
    private Class<T> tClass;
    private Entity entity;

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
        sql.append("SELECT * FROM ").append(entity.tableName());

        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            Field field = entityField.getAnnotation(Field.class);
            if (field.primary()) {
                sql.append(" WHERE ").append(field.name()).append(" = '").append(id).append("'");
                return sql.toString();
            }
        }
        return null;
    }

    private String buildFind(String field, Object value) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(entity.tableName());

        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            Field fieldAnnotation = entityField.getAnnotation(Field.class);
            if (fieldAnnotation.name().equals(field)) {
                sql.append(" WHERE ").append(field).append(" = '").append(value).append("'");
                return sql.toString();
            }
        }
        return null;
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

    public TableConfig getTableConfig() {
        return tableConfig;
    }
}
