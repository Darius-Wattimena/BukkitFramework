package nl.antimeta.bukkit.framework.json.enums;

public enum CommandType {

    TELLRAW("tellraw"),
    TITLE("title");

    public String name;

    CommandType(String name) {
        this.name = name;
    }
}
