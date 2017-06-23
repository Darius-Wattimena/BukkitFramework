package nl.antimeta.bukkit.framework;

import nl.antimeta.bukkit.framework.util.LogUtil;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        LogUtil.setLogger((PluginLogger) getLogger());
    }
}
