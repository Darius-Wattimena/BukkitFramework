package nl.antimeta.bukkit.framework.database.model;

import nl.antimeta.bukkit.framework.database.type.DatabaseType;

import java.util.ArrayList;
import java.util.List;

public class TableConfig {
    private String tableName;
    private DatabaseType databaseType;
    private List<FieldConfig> fieldConfigs;

    public TableConfig() {
        fieldConfigs = new ArrayList<>();
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

    public List<FieldConfig> getFieldConfigs() {
        return fieldConfigs;
    }

    public void setFieldConfigs(List<FieldConfig> fieldConfigs) {
        this.fieldConfigs = fieldConfigs;
    }

    public FieldConfig getPrimaryFieldConfig() {
        for (FieldConfig fieldConfig : fieldConfigs) {
            if (fieldConfig.isPrimary()) {
                return fieldConfig;
            }
        }
        return null;
    }
}
