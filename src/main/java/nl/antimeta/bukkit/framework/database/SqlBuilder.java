package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.FieldType;
import nl.antimeta.bukkit.framework.database.model.TableConfig;
import nl.antimeta.bukkit.framework.database.type.DatabaseType;
import nl.antimeta.bukkit.framework.database.type.MysqlDatabaseType;
import nl.antimeta.bukkit.framework.util.LogUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SqlBuilder<T extends BaseEntity> {

    private StringBuilder sql = new StringBuilder();

    private Entity entity;
    private T entityObject;
    private TableConfig<T> tableConfig;
    private DatabaseType databaseType;
    private Map<String, Object> parameters = new HashMap<>();
    private int id;

    private String selectRange = "*";

    public SqlBuilder(Entity entity, TableConfig<T> tableConfig) {
        this(entity, tableConfig, null, null);
    }

    public SqlBuilder(Entity entity, TableConfig<T> tableConfig, T entityObject) {
        this(entity, tableConfig, entityObject, null);
    }

    public SqlBuilder(Entity entity, TableConfig<T> tableConfig, DatabaseType databaseType) {
        this(entity, tableConfig, null, databaseType);
    }

    public SqlBuilder(Entity entity, TableConfig<T> tableConfig, T entityObject, DatabaseType databaseType) {
        this.entity = entity;
        this.tableConfig = tableConfig;
        this.entityObject = entityObject;
        this.databaseType = databaseType;
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public void addParameters(Map<String, Object> parameters) {
        for (Map.Entry<String, Object> set : parameters.entrySet()) {
            this.parameters.put(set.getKey(), set.getValue());
        }
    }

    public String build(StatementType type) {
        switch (type) {
            case SELECT:
                return buildSelectStatement();
            case INSERT:
                return buildInsertStatement();
            case UPDATE:
                return buildUpdateStatement();
            case DELETE:
                return buildDeleteStatement();
            case CREATE_TABLE_IF_NEEDED:
                return buildCreateTableStatement();
            default:
                return "ERROR";
        }
    }

    private String buildSelectStatement() {
        sql.append("SELECT ").append(selectRange).append(" FROM ").append(entity.tableName());
        if (!parameters.isEmpty()) {
            sql.append("\n");
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
        } else if (id != 0) {
            sql.append("SELECT * FROM ").append(tableConfig.getTableName()).append("\n");

            FieldConfig fieldConfig = tableConfig.getPrimaryFieldConfig();
            if (fieldConfig != null) {
                sql.append(" WHERE ").append(fieldConfig.getFieldName()).append(" = '").append(id).append("'");
                return sql.toString();
            }
        }
        return null;
    }

    private String buildInsertStatement() {
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

    private String buildUpdateStatement() {
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
                    where = " WHERE " + fieldConfig.getFieldName() + " = '" + entityObject.getId() + "'";
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        sql.append(where);

        return sql.toString();
    }

    private String buildDeleteStatement() {
        sql.append("DELETE FROM ").append(tableConfig.getTableName());

        for (FieldConfig fieldConfig : tableConfig.getFieldConfigs().values()) {
            if (fieldConfig.isPrimary()) {
                sql.append(" WHERE ").append(fieldConfig.getFieldName()).append(" = '").append(id).append("'");
                return sql.toString();
            }
        }

        if (parameters.isEmpty()) {
            return null;
        }

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

    private String buildCreateTableStatement() {
        String primaryKeyName = "";

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS `").append(entity.tableName()).append("` (");

        for (FieldConfig<T> fieldConfig : tableConfig.getFieldConfigs().values()) {
            java.lang.reflect.Field entityField = fieldConfig.getField();
            Field field = entityField.getAnnotation(Field.class);

            String fieldName;
            if (StringUtils.isBlank(field.fieldName())) {
                fieldName = entityField.getName();
            } else {
                fieldName = field.fieldName();
            }

            sql.append("`").append(fieldName).append("` ");
            sql.append(getType(field));

            if (!field.nullable()) {
                sql.append(" NOT NULL");
            }

            if (field.primary()) {
                if (databaseType instanceof MysqlDatabaseType) {
                    sql.append(" AUTO_INCREMENT");
                }
                primaryKeyName = field.fieldName();
            }

            sql.append(", ");
        }

        sql.append("PRIMARY KEY (`").append(primaryKeyName).append("`)) ");
        sql.append(databaseType.getTableSuffix());
        if (databaseType instanceof MysqlDatabaseType) {
            sql.append(" DEFAULT CHARSET=utf8 AUTO_INCREMENT=0");
        }
        return sql.toString();
    }

    /**
     * @param field
     *      Annotation that contains the config of a field.
     * @return
     *      Database type of a field eg 'INT(5)' or 'VARCHAR(255)'
     */
    private String getType(Field field) {
        FieldType type = field.fieldType();
        String typeName = databaseType.getType(type);
        String size = null;
        if (type.isDigitSizeNeeded()) {
            if (field.size() != 0) {
                type.setSize(field.size());
            }

            if (field.digitSize() != 0) {
                type.setDigitsSize(field.digitSize());
            }
            size = "(" + type.getSize() + ", "+ type.getDigitsSize() +")";
        } else if (type.isSizeNeeded()) {
            if (field.size() != 0) {
                type.setSize(field.size());
            }
            size = "(" + type.getSize() + ")";
        }

        return typeName + size;
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

    public String getSelectRange() {
        return selectRange;
    }

    public void setSelectRange(String selectRange) {
        this.selectRange = selectRange;
    }

    public void setId(int id) {
        this.id = id;
    }
}
