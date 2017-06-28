package nl.antimeta.bukkit.framework.command;

import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class PlayerCommand extends BaseCommand {

    protected Player player;
    protected UUID playerUUID;

    @Override
    protected boolean onBaseCommand(BukkitCommand bukkitCommand) {
        if (sender instanceof Player) {
            this.player = (Player) sender;
            playerUUID = player.getUniqueId();
            return onPlayerCommand(bukkitCommand, player);
        }

        return true;
    }

    protected abstract boolean onPlayerCommand(BukkitCommand commandInfo, Player sender);
}
