package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.database.Dao;
import nl.antimeta.bukkit.framework.database.Database;
import nl.antimeta.bukkit.framework.database.model.DatabaseConnectionResource;

public class TestDatabase {

    private TestDao testDao;

    public TestDatabase() throws Exception {
        DatabaseConnectionResource resource = new DatabaseConnectionResource();
        resource.setDatabase("DatabaseName");
        resource.setHostname("localhost");
        resource.setUser("admin");
        resource.setPassword("password");
        resource.setPort("80");
        resource.setJdbcParameters("?useSSL=false&autoReconnect=true");

        Database database = new Database(resource);
        testDao = new TestDao(database);
        testDao.myCustomSql();
    }

    public Dao<TestEntity> getTestDao() {
        return testDao;
    }
}
