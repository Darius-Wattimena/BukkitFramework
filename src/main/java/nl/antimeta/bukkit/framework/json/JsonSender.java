package nl.antimeta.bukkit.framework.json;

import nl.antimeta.bukkit.framework.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JsonSender {

    public void send(JsonMessage message) {
        String json = message.buildJson();
        String selector = message.getSelector().buildSelector();
        String command = message.getCommand();

        send(command, selector, json);
    }

    public void send(Player player, JsonMessage message) {
        String command = message.getCommand();
        String json = message.buildJson();
        send(command, player.getName(), json);
    }

    public void send(Iterable<Player> players, JsonMessage message) {
        String command = message.getCommand();
        String json = message.buildJson();
        for (Player player : players) {
            send(command, player.getName(), json);
        }
    }

    public void send(Selector selector, JsonMessage message) {
        String json = message.buildJson();
        String command = message.getCommand();
        send(command, selector.buildSelector(), json);
    }

    private void send(String command, String selector, String json) {
        Main.getMain().getLogger().info(json);
        String commandLine = command + " " + selector + " " + json;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
    }
}
