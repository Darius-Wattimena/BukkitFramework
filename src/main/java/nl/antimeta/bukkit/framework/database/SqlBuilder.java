package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldConfig;
import nl.antimeta.bukkit.framework.database.model.TableConfig;
import nl.antimeta.bukkit.framework.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class SqlBuilder<T extends BaseEntity> {

    private StringBuilder sql = new StringBuilder();

    private Entity entity;
    private T entityObject;
    private TableConfig<T> tableConfig;
    private Map<String, Object> parameters = new HashMap<>();
    private int deleteId;

    private String selectRange = "*";

    private boolean selectStatement = false;
    private boolean insertStatement = false;
    private boolean updateStatement = false;
    private boolean deleteStatement = false;


    public SqlBuilder(Entity entity) {
        this.entity = entity;
    }

    public SqlBuilder(Entity entity, TableConfig<T> tableConfig, T entityObject) {
        this.entity = entity;
        this.tableConfig = tableConfig;
        this.entityObject = entityObject;
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public String build() {
        if (selectStatement) {
            return buildSelectStatement();
        } else if (insertStatement) {
            return buildInsertStatement();
        } else if (updateStatement) {
            return buildUpdateStatement();
        } else if (deleteStatement) {
            return buildDeleteStatement();
        }

        return "ERROR";
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
        }
        return sql.toString();
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
                sql.append(" WHERE ").append(fieldConfig.getFieldName()).append(" = '").append(deleteId).append("'");
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

    public void setSelectStatement(boolean selectStatement) {
        this.selectStatement = selectStatement;
    }

    public void setInsertStatement(boolean insertStatement) {
        this.insertStatement = insertStatement;
    }

    public void setUpdateStatement(boolean updateStatement) {
        this.updateStatement = updateStatement;
    }

    public void setDeleteStatement(boolean deleteStatement) {
        this.deleteStatement = deleteStatement;
    }

    public void setDeleteId(int deleteId) {
        this.deleteId = deleteId;
    }
}
