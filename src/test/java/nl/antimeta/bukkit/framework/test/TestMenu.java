package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.gui.Menu;
import nl.antimeta.bukkit.framework.gui.item.CloseItem;
import nl.antimeta.bukkit.framework.gui.item.ItemClickEvent;
import nl.antimeta.bukkit.framework.gui.item.MenuItem;
import org.bukkit.Material;

public class TestMenu {

    public TestMenu() {
        Menu menu = new Menu("Test Menu", Menu.Size.TWO_LINE, null);
        MenuItem item = new MenuItem("Test Item", Material.MELON, "This is a melon");
        MenuItem item2 = new MenuItem("Test Item 2", Material.APPLE, "Nice apple", "you got", "there");

        menu.addItem(item, 4);
        menu.addItem(item2, 6, 7);
        menu.addItem(new TestItem(), 13);
        menu.addItem(new CloseItem(), 14);
        menu.sendInventory(null);
    }

    private class TestItem extends MenuItem {

        public TestItem() {
            super("Test Item 3", Material.ANVIL);
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            //Do something
            event.close = true;
        }
    }
}
