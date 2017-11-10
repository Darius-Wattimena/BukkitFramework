package nl.antimeta.bukkit.framework.test.command;

import nl.antimeta.bukkit.framework.command.MainCommand;

public class BF extends MainCommand {

    public BF() {
        super("bf", "Greaper_");

        addSubCommand(new TestMenu());
    }
}
