package nl.antimeta.bukkit.framework;

import nl.antimeta.bukkit.framework.gui.MenuListener;
import nl.antimeta.bukkit.framework.test.command.BF;
import nl.antimeta.bukkit.framework.util.LogUtil;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main main;

    @Override
    public void onEnable() {
        main = this;
        LogUtil.setLogger((PluginLogger) getLogger());
        getCommand("bf").setExecutor(new BF());
        getServer().getPluginManager().registerEvents(MenuListener.getInstance(), this);
        getLogger().info("Bukkit Framework Enabled");
    }

    public static Main getMain() {
        return main;
    }
}
