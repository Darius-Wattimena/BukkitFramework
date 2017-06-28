package nl.antimeta.bukkit.framework.command.model;

import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerCommand extends BukkitCommand {

    private Player player;
    private UUID playerUUID;

    public BukkitPlayerCommand(BukkitCommand bukkitCommand) {
        setMain(bukkitCommand.getMain());
        setPermission(bukkitCommand.getPermission());
        setAliases(bukkitCommand.getAliases());
        setSender(bukkitCommand.getSender());
        setCommand(bukkitCommand.getCommand());
        setSubcommands(bukkitCommand.getSubcommands());
        setArgs(bukkitCommand.getArgs());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
}
