package nl.antimeta.bukkit.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class PlayerCommand extends BaseCommand {

    protected Player player;
    protected UUID playerUUID;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            this.player = (Player) sender;
            playerUUID = player.getUniqueId();
            return executePlayerCommand(player, cmd, label, args);
        }

        return true;
    }

    protected abstract boolean executePlayerCommand(Player sender, Command cmd, String label, String[] args);
}
