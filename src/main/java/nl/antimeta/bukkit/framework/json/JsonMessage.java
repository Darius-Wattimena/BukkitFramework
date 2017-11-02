package nl.antimeta.bukkit.framework.json;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JsonMessage {
    public List<JsonMessagePart> messageParts = new ArrayList<>();

    public void add(String message, ChatColor... chatColor) {
        JsonMessagePart messagePart = new JsonMessagePart();
        messagePart.text(message);
        messagePart.colors(chatColor);
        messageParts.add(messagePart);
    }

    public void add(JsonMessagePart messagePart) {
        messageParts.add(messagePart);
    }

    public void send(Player player) {
        String json = buildJson();
        send(player, json);
    }

    private void send(Player player, String json) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + json);
    }

    public void send(Iterable<Player> players) {
        String json = buildJson();
        for (Player player : players) {
            send(player, json);
        }
    }

    private String buildJson() {
        StringBuilder result = new StringBuilder();
        for(JsonMessagePart messagePart : messageParts) {
            result.append(messagePart.toJson());
        }
        return result.toString();
    }
}
