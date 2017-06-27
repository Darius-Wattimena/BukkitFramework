package nl.antimeta.bukkit.framework.command;

import nl.antimeta.bukkit.framework.command.model.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class BaseCommand implements CommandExecutor {

    protected CommandSender sender;
    protected CommandInfo commandInfo;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;

        nl.antimeta.bukkit.framework.command.annotation.Command command = getClass().getAnnotation(nl.antimeta.bukkit.framework.command.annotation.Command.class);
        if (command != null) {
            commandInfo = new CommandInfo();
            commandInfo.setMain(command.main());
            commandInfo.setSubcommands(Arrays.asList(command.subcommands()));
            commandInfo.setPermission(command.permission());
            commandInfo.setAliases(Arrays.asList(command.aliases()));
        }

        return onBaseCommand(commandInfo, sender, cmd, label, args);
    }

    protected abstract boolean onBaseCommand(CommandInfo commandInfo, CommandSender sender, Command cmd, String label, String[] args);
}
