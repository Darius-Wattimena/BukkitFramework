package nl.antimeta.bukkit.framework.command;

import nl.antimeta.bukkit.framework.command.model.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class PlayerCommand extends BaseCommand {

    protected Player player;
    protected UUID playerUUID;

    @Override
    protected boolean onBaseCommand(CommandInfo commandInfo, CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            this.player = (Player) sender;
            playerUUID = player.getUniqueId();
            return onPlayerCommand(commandInfo, player, cmd, label, args);
        }

        return true;
    }

    protected abstract boolean onPlayerCommand(CommandInfo commandInfo, Player sender, Command cmd, String label, String[] args);
}
