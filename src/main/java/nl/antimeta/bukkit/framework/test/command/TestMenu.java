package nl.antimeta.bukkit.framework.test.command;

import nl.antimeta.bukkit.framework.Main;
import nl.antimeta.bukkit.framework.command.PlayerCommand;
import nl.antimeta.bukkit.framework.command.annotation.Command;
import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import nl.antimeta.bukkit.framework.command.model.BukkitPlayerCommand;
import nl.antimeta.bukkit.framework.gui.Menu;
import nl.antimeta.bukkit.framework.gui.item.BackItem;
import nl.antimeta.bukkit.framework.gui.item.CloseItem;
import nl.antimeta.bukkit.framework.gui.item.ItemClickEvent;
import nl.antimeta.bukkit.framework.gui.item.MenuItem;
import org.bukkit.Material;

@Command(main = "testmenu")
public class TestMenu extends PlayerCommand {

    private Menu mainMenu;

    @Override
    protected boolean onPlayerCommand(BukkitPlayerCommand bukkitPlayerCommand) {
        mainMenu = new Menu("Test Menu", Menu.Size.TWO_LINE, Main.getMain());
        MenuItem item = new MenuItem("Test Item", Material.MELON, "This is a melon");
        MenuItem item2 = new MenuItem("Test Item 2", Material.APPLE, "Nice apple", "you got", "there");

        mainMenu.addItem(item, 4);
        mainMenu.addItem(item2, 6, 7, 8, 9);
        mainMenu.addItem(new TestSubMenuItem(), 11);
        mainMenu.addItem(new TestItem(), 13);
        mainMenu.addItem(new CloseItem(), 14);

        mainMenu.sendInventory(bukkitPlayerCommand.getPlayer());

        return true;
    }

    @Override
    protected void onNoPermission(BukkitCommand bukkitCommand) {

    }

    private class TestItem extends MenuItem {

        public TestItem() {
            super("Test Item 3", Material.ANVIL);
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            event.close = true;
        }
    }

    private class TestSubMenuItem extends MenuItem {

        public TestSubMenuItem() {
            super("Open New Menu", Material.BOAT);
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            Menu childMenu = new Menu("Child menu", Menu.Size.FOUR_LINE, Main.getMain(), mainMenu);
            childMenu.addItem(new MenuItem("Item", Material.GOLDEN_APPLE, "Hi I am a child menu"));
            childMenu.addItem(new BackItem(), 9);
            childMenu.addItem(new CloseItem(), 18);
            childMenu.sendInventory(event.getPlayer());
        }
    }
}
