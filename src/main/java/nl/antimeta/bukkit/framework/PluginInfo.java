package nl.antimeta.bukkit.framework;

import org.bukkit.command.CommandSender;

public abstract class PluginInfo {

    private final String name;
    private final String auteur;
    private boolean customHelpMessage;

    public PluginInfo(String name, String auteur, boolean customHelpMessage) {
        this.name = name;
        this.auteur = auteur;
        this.customHelpMessage = customHelpMessage;
    }

    public String getName() {
        return name;
    }

    public String getAuteur() {
        return auteur;
    }

    public boolean isCustomHelpMessage() {
        return customHelpMessage;
    }

    public static PluginInfo getBasicInstance(String name, String auteur) {
        return new PluginInfo(name, auteur, false) {
            @Override
            public void onSendHelpCommand(CommandSender sender) {

            }
        };
    }

    public abstract void onSendHelpCommand(CommandSender sender);
}
