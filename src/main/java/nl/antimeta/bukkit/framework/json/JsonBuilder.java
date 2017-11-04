package nl.antimeta.bukkit.framework.json;

public class JsonBuilder {

    private String result;
    private boolean first;

    public void add(String addition) {
        result += addition;
    }

    public void add(String key, Boolean value) {
        String valueString = (value) ? "true" : "false";

        if (first) {
            result += ("\"" + key + "\":" + valueString);
            first = false;
        } else {
            result += (",\"" + key + "\":" + valueString);
        }
    }

    public void add(String key, String value) {
        if (first) {
            result += ("\"" + key + "\":\"" + value + "\"");
            first = false;
        } else {
            result += (",\"" + key + "\":\"" + value + "\"");
        }
    }

    public void addClickEvent(ClickAction event, String value) {
        if (first) {
            result += ("\"clickEvent\":{\"action\":\"" + event.name + "\",\"value\":\"" + value +"\"" + "}");
            first = false;
        } else {
            result += (",\"clickEvent\":{\"action\":\"" + event.name + "\",\"value\":\"" + value +"\"" + "}");
        }
    }

    public void addHoverEvent(HoverAction event, String value) {
        if (first) {
            result += ("\"hoverEvent\":{\"action\":\"" + event.name + "\",\"value\":\"" + value +"\"" + "}");
            first = false;
        } else {
            result += (",\"hoverEvent\":{\"action\":\"" + event.name + "\",\"value\":\"" + value +"\"" + "}");
        }
    }

    public String get() {
        return result;
    }

}
