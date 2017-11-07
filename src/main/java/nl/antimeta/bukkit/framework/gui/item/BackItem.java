package nl.antimeta.bukkit.framework.gui.item;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class BackItem extends MenuItem {

    public BackItem() {
        super(ChatColor.RED + "Back", new Wool(DyeColor.RED).toItemStack(1));
    }

    public BackItem(ItemStack itemStack) {
        super(ChatColor.RED + "Back", itemStack);
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.back = true;
    }
}
