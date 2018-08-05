package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.database.Dao;
import nl.antimeta.bukkit.framework.database.Database;

public class TestDao extends Dao<TestEntity> {

    public TestDao(Database database) throws Exception {
        super(database, TestEntity.class);
    }
}
