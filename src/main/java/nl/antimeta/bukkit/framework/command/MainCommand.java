package nl.antimeta.bukkit.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class MainCommand implements CommandExecutor {

    private Set<Class> subcommands = new HashSet<>();

    private String name;

    public MainCommand(String name) {
        this.name = name;
    }

    public void addSubCommand(Class clazz) {
        subcommands.add(clazz);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
