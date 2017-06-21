package nl.antimeta.bukkit.framework.database.type;

import nl.antimeta.bukkit.framework.database.model.FieldType;

public class MysqlDatabaseType implements DatabaseType {

    private final static String URL_PORTION = "mysql";
    private final static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private final static String NAME = "MySQL";

    private final static String DEFAULT_CREATE_TABLE_SUFFIX = "ENGINE=InnoDB";

    private String createTableSuffix = DEFAULT_CREATE_TABLE_SUFFIX;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDriverClassName() {
        return DRIVER_CLASS_NAME;
    }

    @Override
    public String getUrlPortion() {
        return URL_PORTION;
    }

    @Override
    public String getType(FieldType fieldType) {
        switch (fieldType) {
            case SmallInt:
                return "SMALLINT";
            case Integer:
                return "INT";
            case BigInt:
                return "BIGINT";
            case Boolean:
                return "CHAR";
            case Text:
                return "TEXT";
            case Varchar:
                return "VARCHAR";
            case Char:
                return "CHAR";
            case Decimal:
                return "DECIMAL";
            case Date:
                return "DATE";
            case DateTime:
                return "DATETIME";
            case Time:
                return "TIME";
        }
        return null;
    }

    public void setCreateTableSuffix(String createTableSuffix) {
        this.createTableSuffix = createTableSuffix;
    }
}
