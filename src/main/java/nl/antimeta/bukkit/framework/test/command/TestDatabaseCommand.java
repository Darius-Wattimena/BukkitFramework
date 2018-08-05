package nl.antimeta.bukkit.framework.test.command;

import nl.antimeta.bukkit.framework.command.PlayerCommand;
import nl.antimeta.bukkit.framework.command.annotation.Command;
import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import nl.antimeta.bukkit.framework.command.model.BukkitPlayerCommand;
import nl.antimeta.bukkit.framework.test.TestDatabase;
import nl.antimeta.bukkit.framework.test.TestEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Command(main = "testdatabase")
public class TestDatabaseCommand extends PlayerCommand {
    private TestDatabase testDatabase;

    @Override
    protected boolean onPlayerCommand(BukkitPlayerCommand bukkitPlayerCommand) {
        testDatabase = new TestDatabase();
        executeTestSql();

        return true;
    }

    @Override
    protected void onNoPermission(BukkitCommand bukkitCommand) {

    }

    public void executeTestSql() {
        try {
            ResultSet resultSet = testDatabase.getTestDao().execute("SELECT * FROM Test_Entity WHERE 1=1");
            List<TestEntity> result = testDatabase.getTestDao().processResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
