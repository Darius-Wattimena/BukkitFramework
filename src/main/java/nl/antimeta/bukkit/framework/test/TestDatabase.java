package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.database.Database;
import nl.antimeta.bukkit.framework.database.model.DatabaseConnectionResource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TestDatabase {

    private Database database;
    private TestDao testDao;

    public TestDatabase() {
        DatabaseConnectionResource resource = new DatabaseConnectionResource();
        resource.setDatabase("DatabaseName");
        resource.setHostname("localhost");
        resource.setUser("admin");
        resource.setPassword("password");
        resource.setPort("80");
        resource.setJdbcParameters("?useSSL=false&autoReconnect=true");

        database = new Database(resource);
        try {
            testDao = new TestDao(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TestDao getTestDao() {
        return testDao;
    }
}
