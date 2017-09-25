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
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
        sqlBuilder.setSelectStatement();
        sqlBuilder.setId(id.intValue());

        String sql = sqlBuilder.build();
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> find(String field, Object value) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
        sqlBuilder.addParameter(field, value);
        sqlBuilder.setSelectStatement();

        String sql = sqlBuilder.build();
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> find(Map<String, Object> parameters) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
        sqlBuilder.setSelectStatement();
        sqlBuilder.addParameters(parameters);

        String sql = sqlBuilder.build();
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> findAll() throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
        sqlBuilder.setSelectStatement();

        String sql = sqlBuilder.build();
        LogUtil.info(sql);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    private boolean create() throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, entityObject);
        sqlBuilder.setInsertStatement();

        String sql = sqlBuilder.build();
        LogUtil.info(sql);
        return executeNoResult(sql);
    }

    private boolean update(T entity) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(this.entity, tableConfig, entityObject);
        sqlBuilder.setUpdateStatement();

        String sql = sqlBuilder.build();
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
            SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
            sqlBuilder.setDeleteStatement();
            sqlBuilder.setId(id.intValue());

            String sql = sqlBuilder.build();
            LogUtil.info(sql);
            return executeNoResult(sql);
        }

        return false;
    }

    public boolean delete(String field, Object value) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
        sqlBuilder.setDeleteStatement();
        sqlBuilder.addParameter(field, value);

        String sql = sqlBuilder.build();
        LogUtil.info(sql);
        return executeNoResult(sql);
    }

    public boolean delete(Map<String, Object> parameters) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, null);
        sqlBuilder.setDeleteStatement();
        sqlBuilder.addParameters(parameters);

        String sql = sqlBuilder.build();
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
