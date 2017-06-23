package nl.antimeta.bukkit.framework.util;

import org.bukkit.plugin.PluginLogger;

public class LogUtil {

    private static PluginLogger staticLogger;

    public static void setLogger(PluginLogger logger) {
        staticLogger = logger;
    }

    public static void info(String message) {
        staticLogger.info(message);
    }

    public static void error(String message) {
        staticLogger.warning(message);
    }
}
