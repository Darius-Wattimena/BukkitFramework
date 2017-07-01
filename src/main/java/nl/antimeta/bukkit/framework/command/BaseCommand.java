package nl.antimeta.bukkit.framework.command;

import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class BaseCommand implements CommandExecutor {

    CommandSender sender;
    private BukkitCommand bukkitCommand;

    public BaseCommand() {
        nl.antimeta.bukkit.framework.command.annotation.Command command = getClass().getAnnotation(nl.antimeta.bukkit.framework.command.annotation.Command.class);
        bukkitCommand = new BukkitCommand();
        bukkitCommand.setMain(command.main());
        bukkitCommand.setPermission(command.permission());
        bukkitCommand.setAliases(Arrays.asList(command.aliases()));
        bukkitCommand.setSubcommands(Arrays.asList(command.subcommands()));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;

        bukkitCommand.setArgs(args);
        bukkitCommand.setCommand(cmd);
        bukkitCommand.setSender(sender);

        return onBaseCommand(bukkitCommand);
    }

    protected abstract boolean onBaseCommand(BukkitCommand commandInfo);

    public BukkitCommand getBukkitCommand() {
        return bukkitCommand;
    }
}
