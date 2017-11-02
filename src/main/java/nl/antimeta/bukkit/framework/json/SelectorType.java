package nl.antimeta.bukkit.framework.json;

/**
 * Created by DariusPC on 3-11-2017.
 */
public enum SelectorType {

    CUSTOM(""),
    EXECUTOR("@s"),
    NEAREST_PLAYER("@p"),
    RANDOM_PLAYER("@r"),
    ALL_PLAYERS("@a"),
    ALL_ENTITIES("@e");

    public String name;

    private SelectorType(String name) {
        this.name = name;
    }
}
