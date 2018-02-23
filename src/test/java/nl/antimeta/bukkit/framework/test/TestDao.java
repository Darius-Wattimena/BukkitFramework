package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.database.Dao;
import nl.antimeta.bukkit.framework.database.Database;

import java.sql.SQLException;
import java.util.List;

public class TestDao extends Dao<TestEntity> {

    public TestDao(Database database) throws Exception {
        super(database, TestEntity.class);
    }

    public void myCustomSql() {
        String sql = "SELECT * FROM TestEntity";
        try {
            List<TestEntity> result = processResultSet(execute(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
