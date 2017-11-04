package nl.antimeta.bukkit.framework.json;

import nl.antimeta.bukkit.framework.json.enums.CommandType;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class JsonMessage {
    private List<JsonMessagePart> messageParts = new ArrayList<>();
    private String command;
    private Selector selector;

    public void add(String message, ChatColor... chatColor) {
        JsonMessagePart messagePart = new JsonMessagePart();
        messagePart.text(message);
        messagePart.colors(chatColor);
        messageParts.add(messagePart);
    }

    public void add(JsonMessagePart messagePart) {
        messageParts.add(messagePart);
    }

    public String buildJson() {
        StringBuilder result = new StringBuilder();
        result.append("[{}");
        for(JsonMessagePart messagePart : messageParts) {
            result.append(",{");
            result.append(messagePart.toJson());
            result.append("}");
        }

        result.append("]");
        return result.toString();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCommand(CommandType command) {
        this.command = command.name;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }
}
