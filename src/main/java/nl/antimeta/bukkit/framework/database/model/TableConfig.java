package nl.antimeta.bukkit.framework.database.model;

import nl.antimeta.bukkit.framework.database.type.DatabaseType;

import java.util.HashMap;
import java.util.Map;

public class TableConfig<T> {
    private String tableName;
    private DatabaseType databaseType;
    private Map<String, FieldConfig<T>> fieldConfigs;

    public TableConfig() {
        fieldConfigs = new HashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public Map<String, FieldConfig<T>> getFieldConfigs() {
        return fieldConfigs;
    }

    public FieldConfig<T> getPrimaryFieldConfig() {
        for (FieldConfig<T> fieldConfig : fieldConfigs.values()) {
            if (fieldConfig.isPrimary()) {
                return fieldConfig;
            }
        }
        return null;
    }
}
