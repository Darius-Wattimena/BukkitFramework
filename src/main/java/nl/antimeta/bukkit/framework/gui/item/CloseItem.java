package nl.antimeta.bukkit.framework.gui.item;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class CloseItem extends MenuItem {
    public CloseItem() {
        super(ChatColor.RED + "Close", new Wool(DyeColor.RED).toItemStack(1));
    }

    public CloseItem(ItemStack itemStack) {
        super(ChatColor.RED + "Close", itemStack);
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.close = true;
    }
}
