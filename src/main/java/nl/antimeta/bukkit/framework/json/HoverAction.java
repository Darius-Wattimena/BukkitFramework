package nl.antimeta.bukkit.framework.json;

/**
 * Created by DariusPC on 2-11-2017.
 */
public enum HoverAction {

    NONE(""),
    SHOW_TEXT("show_text"),
    SHOW_ACHIEVEMENT("show_achievement"),
    SHOW_ITEM("show_item"),
    SHOW_ENTITY("show_entity");

    public String name;

    private HoverAction(String name) {
        this.name = name;
    }
}
