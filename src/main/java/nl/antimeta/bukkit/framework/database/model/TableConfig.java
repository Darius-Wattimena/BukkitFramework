package nl.antimeta.bukkit.framework.database.model;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.type.DatabaseType;

import java.util.HashMap;
import java.util.Map;

public class TableConfig<T> {
    private final String tableName;
    private final DatabaseType databaseType;
    private Map<String, FieldConfig<T>> fieldConfigs;

    public TableConfig(Entity entity, DatabaseType databaseType) {
        this.fieldConfigs = new HashMap<>();
        this.tableName = entity.tableName();
        this.databaseType = databaseType;
    }

    public String getTableName() {
        return tableName;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
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
