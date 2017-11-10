package nl.antimeta.bukkit.framework.test.command;

import nl.antimeta.bukkit.framework.command.PlayerCommand;
import nl.antimeta.bukkit.framework.command.annotation.Command;
import nl.antimeta.bukkit.framework.command.model.BukkitCommand;
import nl.antimeta.bukkit.framework.command.model.BukkitPlayerCommand;
import nl.antimeta.bukkit.framework.json.JsonMessage;
import nl.antimeta.bukkit.framework.json.JsonMessagePart;
import nl.antimeta.bukkit.framework.json.JsonSender;
import nl.antimeta.bukkit.framework.json.enums.ClickAction;
import nl.antimeta.bukkit.framework.json.enums.CommandType;
import nl.antimeta.bukkit.framework.json.enums.HoverAction;
import org.bukkit.ChatColor;

@Command(main = "testjsonmessage")
public class TestJsonMessage extends PlayerCommand {

    @Override
    protected boolean onPlayerCommand(BukkitPlayerCommand bukkitPlayerCommand) {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCommand(CommandType.TELLRAW);
        jsonMessage.add("First part! ", ChatColor.DARK_GREEN, ChatColor.BOLD);

        JsonMessagePart secondMessage = new JsonMessagePart("Second part?");
        secondMessage.newLine();
        secondMessage.newLine();
        secondMessage.newLine();
        secondMessage.text("End second part! ");
        secondMessage.color(ChatColor.BLUE);
        secondMessage.italic();
        jsonMessage.add(secondMessage);

        jsonMessage.add("Third part.", ChatColor.STRIKETHROUGH);

        JsonMessagePart onClickMessagePart = new JsonMessagePart();
        onClickMessagePart.newLine();
        onClickMessagePart.text("Click HERE!!!!");
        onClickMessagePart.onClick(ClickAction.OPEN_URL, "https://google.com");
        onClickMessagePart.color(ChatColor.BLACK);
        jsonMessage.add(onClickMessagePart);

        JsonMessagePart onHoverMessagePart = new JsonMessagePart();
        onHoverMessagePart.newLine();
        onHoverMessagePart.text("Hover Test!!");
        onHoverMessagePart.customOnHover(HoverAction.SHOW_TEXT, "[\"\",{\"text\":\"Nice :D!\n\",\"color\":\"green\",\"underlined\":true},\"Why are you reading this?\"]");
        onHoverMessagePart.color(ChatColor.GOLD);
        jsonMessage.add(onHoverMessagePart);

        JsonSender jsonSender = new JsonSender();
        jsonSender.send(bukkitPlayerCommand.getPlayer(), jsonMessage);

        return true;
    }

    @Override
    protected void onNoPermission(BukkitCommand bukkitCommand) {

    }
}
