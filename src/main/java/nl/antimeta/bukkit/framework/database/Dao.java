package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.BaseEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dao<T extends BaseEntity> {

    private Database database;

    public Dao(Database database) {
        this.database = database;
    }

    public ResultSet execute(String sql) throws SQLException {
        PreparedStatement stmt = database.openConnection().prepareStatement(sql);
        return stmt.executeQuery();
    }

    public boolean executeNoResult(String sql) throws SQLException {
        PreparedStatement stmt = database.openConnection().prepareStatement(sql);
        return stmt.execute();
    }

    public T find(Integer id) {
        T result = null;

        return result;
    }
}
