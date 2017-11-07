package nl.antimeta.bukkit.framework.gui;

import nl.antimeta.bukkit.framework.gui.item.ItemClickEvent;
import nl.antimeta.bukkit.framework.gui.item.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Menu {

    private JavaPlugin plugin;
    private String name;
    private Menu parent;
    private Size size;
    private MenuItem[] items;

    public Menu(String name, Size size, JavaPlugin plugin, Menu parent) {
        this.name = name;
        this.plugin = plugin;
        this.parent = parent;
        this.size = size;
        items = new MenuItem[size.size];
    }

    public Menu(String name, Size size, JavaPlugin plugin) {
        this(name, size, plugin, null);
    }

    public void sendInventory(Player player) {
        Inventory menuHolderInventory = Bukkit.createInventory(player, size.size);
        MenuHolder holder = new MenuHolder(this, menuHolderInventory);
        Inventory inventory = Bukkit.createInventory(holder, size.size, name);
        build(inventory);
        player.openInventory(inventory);
    }

    private void build(Inventory inventory) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i].buildIcon());
            } else {
                inventory.setItem(i, null);
            }
        }
    }

    public void addItem(MenuItem item, int slot) {
        items[slot] = item;
    }

    public void addItem(MenuItem item, Integer... slots) {
        for(Integer slot : slots) {
            items[slot] = item;
        }
    }

    private MenuItem getItem(int slot) {
        return items[slot];
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getInventory().getHolder() instanceof MenuHolder) {
            MenuHolder holder = (MenuHolder) e.getInventory().getHolder();
            Menu menu = holder.getMenu();
            int slot = e.getRawSlot();
            Player player = (Player) e.getWhoClicked();

            MenuItem item = menu.getItem(slot);
            ItemClickEvent itemClickEvent = item.getItemClickEvent();

            item.onItemClick(itemClickEvent);
            if (itemClickEvent.close || itemClickEvent.back) {
                final String playerName = player.getName();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        Player p = Bukkit.getPlayerExact(playerName);
                        if (p != null) {
                            p.closeInventory();
                        }
                    }
                }, 1);

                if (itemClickEvent.back && parent != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            Player p = Bukkit.getPlayerExact(playerName);
                            if (p != null) {
                                parent.sendInventory(p);
                            }
                        }
                    }, 3);
                }
            }
        }
    }

    public enum Size {
        ONE_LINE(9),
        TWO_LINE(18),
        THREE_LINE(27),
        FOUR_LINE(36),
        FIVE_LINE(45),
        SIX_LINE(54);

        private final int size;

        Size(int size) {
            this.size = size;
        }

        public static Size calculateRows(int totalItems) {
            if (totalItems < 10) {
                return ONE_LINE;
            } else if (totalItems < 19) {
                return TWO_LINE;
            } else if (totalItems < 28) {
                return THREE_LINE;
            } else if (totalItems < 37) {
                return FOUR_LINE;
            } else if (totalItems < 46) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }
    }
}
