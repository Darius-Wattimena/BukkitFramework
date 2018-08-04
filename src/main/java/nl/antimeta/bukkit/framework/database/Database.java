package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.DatabaseConnectionResource;
import nl.antimeta.bukkit.framework.database.type.DatabaseType;
import nl.antimeta.bukkit.framework.database.type.MysqlDatabaseType;

public class Database {

    private DatabaseConnection connection;

    private DatabaseType databaseType;
    private final DatabaseConnectionResource databaseConnectionResource;

    private DaoManger daoManger = new DaoManger(this);

    public Database(DatabaseConnectionResource databaseConnectionResource) {
        this(new MysqlDatabaseType(), databaseConnectionResource);
    }

    public Database(DatabaseType databaseType, DatabaseConnectionResource databaseConnectionResource) {
        this.databaseType = databaseType;
        this.databaseConnectionResource = databaseConnectionResource;
        setupConnection();
    }

    private void setupConnection() {
        connection = new DatabaseConnection(databaseType, databaseConnectionResource);
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public DaoManger getDaoManger() {
        return daoManger;
    }

    public void setDaoManger(DaoManger daoManger) {
        this.daoManger = daoManger;
    }

    DatabaseConnection getConnection() {
        return connection;
    }
}
