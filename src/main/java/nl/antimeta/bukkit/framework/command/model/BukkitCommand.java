package nl.antimeta.bukkit.framework.command.model;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommand {

    private String main;

    private List<String> subcommands = new ArrayList<>();

    private String permission;

    private List<String> aliases = new ArrayList<>();

    private Command command;

    private CommandSender sender;

    private String[] args;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void addSubcommand(String subcommand) {
        subcommands.add(subcommand);
    }

    public List<String> getSubcommands() {
        return subcommands;
    }

    public void setSubcommands(List<String> subcommands) {
        this.subcommands = subcommands;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void setSender(CommandSender sender) {
        this.sender = sender;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
