package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.TableConfig;

import java.lang.annotation.Annotation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dao<T extends BaseEntity> {

    private Database database;
    private TableConfig tableConfig;

    public Dao(Database database, Class<T> tClass) throws Exception {
        this.database = database;
        Entity entity = tClass.getAnnotation(Entity.class);
        if (entity != null) {
            tableConfig = new TableConfig();
            tableConfig.setDatabaseType(database.getDatabaseType());
            tableConfig.setTableName(entity.tableName());
            initFieldConfig(tClass);
        }
    }

    private void initFieldConfig(Class<T> tClass) {
        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            for (Annotation annotation : entityField.getAnnotations()) {
                if (annotation instanceof Field) {
                    Field field = (Field) annotation;
                    FieldConfig fieldConfig = new FieldConfig();
                    fieldConfig.setFieldName(field.name());
                    fieldConfig.setFieldType(field.fieldType());
                    fieldConfig.setPrimary(field.primary());
                    fieldConfig.setSize(field.size());
                    fieldConfig.setDigitSize(field.digitSize());
                    tableConfig.getFieldConfigs().add(fieldConfig);
                }
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

    public T find(Integer id) {
        T result = null;

        return result;
    }

    public TableConfig getTableConfig() {
        return tableConfig;
    }
}
