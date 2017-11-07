package nl.antimeta.bukkit.framework.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {

    private Menu menu;
    private Inventory inventory;

    public MenuHolder(Menu menu, Inventory inventory) {
        this.menu = menu;
        this.inventory = inventory;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
