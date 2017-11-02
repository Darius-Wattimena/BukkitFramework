package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.command.PlayerCommand;
import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import nl.antimeta.bukkit.framework.command.model.BukkitPlayerCommand;

/**
 * Created by Darius on 25-Sep-17.
 */
public class TestCommand extends PlayerCommand {
    @Override
    protected boolean onPlayerCommand(BukkitPlayerCommand bukkitPlayerCommand) {
        try {
            TestDatabase database = new TestDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onNoPermission(BukkitCommand bukkitCommand) {
        //TODO: add warning message
    }
}
