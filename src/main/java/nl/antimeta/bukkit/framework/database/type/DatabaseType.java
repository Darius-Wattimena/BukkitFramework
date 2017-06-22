package nl.antimeta.bukkit.framework.database.type;

import nl.antimeta.bukkit.framework.database.model.FieldType;

public interface DatabaseType {

    String getName();
    String getDriverClassName();
    String getUrlPortion();
    String getTableSuffix();

    String getType(FieldType fieldType);
}
