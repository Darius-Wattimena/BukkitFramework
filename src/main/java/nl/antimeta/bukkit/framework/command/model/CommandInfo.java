package nl.antimeta.bukkit.framework.command.model;

import java.util.List;

public class CommandInfo {

    private String main;

    private List<String> subcommands;

    private String permission;

    private List<String> aliases;

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
}
