package nl.antimeta.bukkit.framework.json;

import org.bukkit.ChatColor;

public class JsonMessagePart {

    private StringBuilder text;
    private ChatColor chatColor;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean obfuscated;
    private ClickAction clickEvent;
    private String clickEventValue;
    private HoverAction hoverEvent;
    private String hoverEventValue;

    public JsonMessagePart() {
        initialize();
    }

    public JsonMessagePart(String message) {
        initialize(message);
    }

    public JsonMessagePart(String message, ChatColor... chatColors) {
        initialize(message, chatColors);
    }

    private void initialize() {
        initialize("");
    }

    private void initialize(String message, ChatColor... chatColors) {
        text = new StringBuilder(message);
        chatColor = ChatColor.WHITE;
        bold = false;
        italic = false;
        underlined = false;
        strikethrough = false;
        obfuscated = false;
        clickEvent = ClickAction.NONE;
        clickEventValue = "";
        hoverEvent = HoverAction.NONE;
        hoverEventValue = "";
    }

    /**
     * Add value to the current message
     * @param message text value
     */
    public void text(String message) {
        text.append(message);
    }

    /**
     * Can only add one color and multiple formats
     * @param colors
     */
    public void colors(ChatColor... colors) {
        for (ChatColor color : colors) {
            if (color.isFormat()) {
                switch (color) {
                    case BOLD:
                        bold();
                        break;
                    case ITALIC:
                        italic();
                        break;
                    case UNDERLINE:
                        underline();
                        break;
                    case STRIKETHROUGH:
                        strikethrough();
                        break;
                    case MAGIC:
                        obfuscate();
                        break;
                }
            } else {
                color(color);
            }
        }
    }

    /**
     * Remove all the current text of this {@link JsonMessagePart}
     */
    public void clearText() {
        text = new StringBuilder();
    }

    /**
     * Add a color to the current {@link JsonMessagePart}
     * @param color
     *      Chat color thats not a format
     */
    public void color(ChatColor color) {
        if (!color.isFormat()) {
            chatColor = color;
        }
    }

    /**
     * Add a onclick event on the current {@link JsonMessagePart}
     * @param action
     * @param value
     */
    public void onClick(ClickAction action, String value) {
        clickEvent = action;
        clickEventValue = value;
    }

    /**
     * Add a onhover event on the current {@link JsonMessagePart}
     * @param action
     * @param value
     */
    public void onHover(HoverAction action, String value) {
        hoverEvent = action;
        hoverEventValue = value;
    }

    /**
     * Bold this {@link JsonMessagePart}
     */
    public void bold() {
        bold = true;
    }

    /**
     * Italic this {@link JsonMessagePart}
     */
    public void italic() {
        italic = true;
    }

    /**
     * Underline this {@link JsonMessagePart}
     */
    public void underline() {
        underlined = true;
    }

    /**
     * Strikethrough this {@link JsonMessagePart}
     */
    public void strikethrough() {
        strikethrough = true;
    }

    /**
     * Obfuscate this {@link JsonMessagePart}
     */
    public void obfuscate() {
        obfuscated = true;
    }

    /**
     * Obfuscate this {@link JsonMessagePart}
     */
    public void magic() {
        obfuscated = true;
    }

    /**
     * Add a unicode character to your message all unicodes can be found <a href="http://unicode.org/charts/">here</a>
     * @param hex four chars found
     *
     */
    public void unicode(String hex) {
        text.append("\\" + "u" + hex);
    }

    /**
     * Add a new line in this message
     */
    public void newLine() {
        text.append("\n");
    }

    /**
     * Add a ' character to the message
     */
    public void quote() {
        text.append("\'");
    }

    /**
     * Add a " character to the message
     */
    public void doubleQuote() {
        text.append("\"");
    }

    /**
     * Add a \ character to the message
     */
    public void backSlash() {
        text.append("\\");
    }

    /**
     * Convert all the given config to the accepted Json in minecraft
     * @return Json value of this message part
     */
    public String toJson() {
        //TODO
        return "";
    }
}
