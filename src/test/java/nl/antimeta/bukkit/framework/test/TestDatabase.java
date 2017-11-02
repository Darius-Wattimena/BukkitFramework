package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.database.Dao;
import nl.antimeta.bukkit.framework.database.Database;
import nl.antimeta.bukkit.framework.database.model.Resource;
import nl.antimeta.bukkit.framework.database.type.MysqlDatabaseType;

public class TestDatabase {

    private Dao<TestEntity> testEntityDao;

    public TestDatabase() throws Exception {
        Resource resource = new Resource();
        resource.setDatabase("DatabaseName");
        resource.setHostname("localhost");
        resource.setUser("admin");
        resource.setPassword("password");
        resource.setPort("80");

        Database database = new Database(new MysqlDatabaseType(), resource);
        database.createTable(TestEntity.class);

        testEntityDao = new Dao<>(database, TestEntity.class);
    }

    public Dao<TestEntity> getTestDao() {
        return testEntityDao;
    }
}
