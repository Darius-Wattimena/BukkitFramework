package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.TableConfig;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract class BaseDao<T extends BaseEntity> {

    protected Database database;
    TableConfig<T> tableConfig;
    private Class<T> tClass;
    protected Entity entity;

    public BaseDao(Database database, Class<T> tClass) throws Exception {
        this.database = database;
        this.tClass = tClass;
        entity = tClass.getAnnotation(Entity.class);
        if (entity != null) {
            tableConfig = new TableConfig<>(entity, database.getDatabaseType());
            initFieldConfig();
        }
    }

    private void initFieldConfig() {
        for (java.lang.reflect.Field entityField : tClass.getDeclaredFields()) {
            Field field = entityField.getAnnotation(Field.class);
            if (field != null) {
                entityField.setAccessible(true);

                String fieldName = getFieldName(field, entityField);
                createFieldConfig(field, entityField, fieldName);
            }
        }
    }

    private String getFieldName(Field field, java.lang.reflect.Field entityField) {
        if (StringUtils.isBlank(field.fieldName())) {
            return entityField.getName();
        } else {
            return field.fieldName();
        }
    }

    private void createFieldConfig(Field field, java.lang.reflect.Field entityField, String fieldName) {
        FieldConfig<T> fieldConfig = new FieldConfig<>(entityField, fieldName, field);
        if (field.foreign()) {
            fieldConfig.setForeignClass(entityField.getClass());
            fieldConfig.setForeignDao(database.getDaoManger().findDao(entityField.getType()));
        }

        tableConfig.getFieldConfigs().put(fieldName, fieldConfig);
    }

    protected List<T> processResultSet(ResultSet resultSet) {
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
}
