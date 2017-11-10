package nl.antimeta.bukkit.framework.gui.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MenuItem {

    private final String displayName;
    private final ItemStack icon;
    private final List<String> lore;

    public MenuItem(String displayName, ItemStack icon, String... lore) {
        this.displayName = displayName;
        this.icon = icon;
        this.lore = Arrays.asList(lore);
    }

    public MenuItem(String displayName, Material icon, String... lore) {
        this.displayName = displayName;
        this.icon = new ItemStack(icon);
        this.lore = Arrays.asList(lore);
    }

    public void onItemClick(ItemClickEvent event) {

    }

    public ItemStack buildIcon() {
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        icon.setItemMeta(meta);
        return icon;
    }
}
