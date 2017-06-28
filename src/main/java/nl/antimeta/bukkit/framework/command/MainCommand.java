package nl.antimeta.bukkit.framework.command;

import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class MainCommand implements CommandExecutor {

    private Map<String, CommandExecutor> subcommands = new HashMap<>();

    private String name;

    public MainCommand(String name) {
        this.name = name;
    }

    public void addSubCommand(BaseCommand baseCommand) {
        BukkitCommand bukkitCommand = baseCommand.getBukkitCommand();
        if (bukkitCommand == null || bukkitCommand.getMain() == null) {
            throw new IllegalArgumentException("invalid command paramters specified");
        }

        for (String subcommand : bukkitCommand.getSubcommands()) {
            if (StringUtils.isNotBlank(subcommand)) {
                subcommands.put(subcommand, baseCommand);
            }
        }
        subcommands.put(bukkitCommand.getMain(), baseCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1 && hasSubCommand(args[0])) {
            CommandExecutor executor = getSubCommand(args[0]);
            return executor.onCommand(sender, cmd, label, removeFirstArg(args));
        } else {
            sender.sendMessage(ChatColor.GREEN + "Do " + ChatColor.AQUA + "/" + name +" help " + ChatColor.GREEN + "to see help for the plugin.");
            return true;
        }
    }

    private CommandExecutor getSubCommand(String command) {
        return subcommands.get(command.toLowerCase());
    }

    private boolean hasSubCommand(String command) {
        return subcommands.containsKey(command.toLowerCase());
    }

    private String[] removeFirstArg(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
