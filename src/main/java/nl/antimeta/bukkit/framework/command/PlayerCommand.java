package nl.antimeta.bukkit.framework.command;

import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import nl.antimeta.bukkit.framework.command.model.BukkitPlayerCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public abstract class PlayerCommand extends BaseCommand {

    private BukkitPlayerCommand bukkitPlayerCommand;

    @Override
    protected boolean onBaseCommand(BukkitCommand bukkitCommand) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean gotPermission = checkPermission(player, bukkitCommand);
            if (!gotPermission) {
                return true;
            }
            bukkitPlayerCommand = new BukkitPlayerCommand(bukkitCommand);
            bukkitPlayerCommand.setPlayer(player);
            bukkitPlayerCommand.setPlayerUUID(player.getUniqueId());
            return onPlayerCommand(bukkitPlayerCommand);
        }

        return true;
    }

    protected boolean checkPermission(Player player, BukkitCommand bukkitCommand) {
        if (StringUtils.isNotBlank(bukkitCommand.getPermission())) {
            if (!player.hasPermission(bukkitCommand.getPermission())) {
                onNoPermission(bukkitCommand);
                return false;
            }
        }
        return true;
    }

    protected abstract boolean onPlayerCommand(BukkitPlayerCommand bukkitPlayerCommand);

    protected abstract void onNoPermission(BukkitCommand bukkitCommand);

    public BukkitPlayerCommand getBukkitPlayerCommand() {
        return bukkitPlayerCommand;
    }
}
