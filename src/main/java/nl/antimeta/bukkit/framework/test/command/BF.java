package nl.antimeta.bukkit.framework.test.command;

import nl.antimeta.bukkit.framework.PluginInfo;
import nl.antimeta.bukkit.framework.command.MainCommand;

public class BF extends MainCommand {

    public BF() {
        super(PluginInfo.getBasicInstance("bf", "Greaper_"));

        addSubCommand(new TestMenu());
        addSubCommand(new TestJsonMessage());
        addSubCommand(new TestDatabaseCommand());
    }
}
