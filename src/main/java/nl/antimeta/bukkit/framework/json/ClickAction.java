package nl.antimeta.bukkit.framework.json;

public enum ClickAction {

    NONE(""),
    OPEN_URL("open_url"),
    OPEN_FILE("open_file"),
    RUN("run_command"),
    SUGGEST("suggest_command"),
    CHANGE_PAGE("change_page");

    public String name;

    private ClickAction(String name) {
        this.name = name;
    }
}
