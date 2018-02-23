package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.Resource;
import nl.antimeta.bukkit.framework.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection connection;

    private DatabaseType databaseType;
    private final Resource resource;

    private DaoManger daoManger = new DaoManger(this);

    public Database(DatabaseType databaseType, Resource resource) {
        this.databaseType = databaseType;
        this.resource = resource;
    }

    Connection openConnection() throws SQLException {
        if(checkConnection()) {
            return connection;
        } else {
            try {
                Class.forName(databaseType.getDriverClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection("jdbc:" + databaseType.getUrlPortion() + "://" + resource.getHostname() + ':' + resource.getPort() + '/' + resource.getDatabase() + "?useSSL=false&autoReconnect=true&useUnicode=yes", resource.getUser(), resource.getPassword());
            return connection;
        }
    }

    private boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
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
}
