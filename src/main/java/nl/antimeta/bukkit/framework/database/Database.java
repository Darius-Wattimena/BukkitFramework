package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.DatabaseConnectionResource;
import nl.antimeta.bukkit.framework.database.type.DatabaseType;
import nl.antimeta.bukkit.framework.database.type.MysqlDatabaseType;

public class Database {

    private DatabaseConnection connection;

    private final DatabaseType databaseType;
    private final DatabaseConnectionResource resource;
    private final DaoManger daoManger;

    public Database(DatabaseConnectionResource resource) {
        this(new MysqlDatabaseType(), resource);
    }

    public Database(DatabaseType databaseType, DatabaseConnectionResource resource) {
        this.databaseType = databaseType;
        this.resource = resource;
        this.daoManger = new DaoManger(this);
        setupConnection();
    }

    private void setupConnection() {
        connection = new DatabaseConnection(databaseType, resource);
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public DaoManger getDaoManger() {
        return daoManger;
    }

    DatabaseConnection getConnection() {
        return connection;
    }
}
