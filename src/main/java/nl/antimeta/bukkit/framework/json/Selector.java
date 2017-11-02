package nl.antimeta.bukkit.framework.json;

public class Selector {

    SelectorType type;


    public Selector() {
        initailize();
    }

    public Selector(SelectorType type) {
        initailize(type);
    }

    private void initailize() {
        initailize(SelectorType.CUSTOM);
    }

    private void initailize(SelectorType type) {
        this.type = type;
    }

    public String buildSelector() {
        //TODO
        if (type == SelectorType.CUSTOM) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            result.append(type.name);
            return result.toString();
        }

    }
}
