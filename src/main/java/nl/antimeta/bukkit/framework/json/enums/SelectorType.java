package nl.antimeta.bukkit.framework.json.enums;

public enum SelectorType {

    CUSTOM(""),
    EXECUTOR("@s"),
    NEAREST_PLAYER("@p"),
    RANDOM_PLAYER("@r"),
    ALL_PLAYERS("@a"),
    ALL_ENTITIES("@e");

    public String name;

    SelectorType(String name) {
        this.name = name;
    }
}
