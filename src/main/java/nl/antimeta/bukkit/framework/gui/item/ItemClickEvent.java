package nl.antimeta.bukkit.framework.gui.item;

import org.bukkit.entity.Player;

public class ItemClickEvent {

    private Player player;
    public boolean back;
    public boolean close;
    public boolean update;

    public ItemClickEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
