package nl.antimeta.bukkit.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand implements CommandExecutor {

    protected CommandSender sender;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;

        return baseCommand(sender, cmd, label, args);
    }

    protected abstract boolean baseCommand(CommandSender sender, Command cmd, String label, String[] args);
}
