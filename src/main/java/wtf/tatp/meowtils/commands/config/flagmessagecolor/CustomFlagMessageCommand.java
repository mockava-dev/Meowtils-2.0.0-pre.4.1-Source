package wtf.tatp.meowtils.commands.config.flagmessagecolor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.Prefix;

public class CustomFlagMessageCommand
extends CommandBase {
    private static final Map<String, String> argToConfigField = new HashMap<String, String>();

    public String getCommandName() {
        return "anticheatcolor";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("accolor", "acc", "flagcolor", "anticheat");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /anticheatcolor");
            return;
        }
        this.openColorSelector("Reason");
        this.openColorSelector("WDR");
        this.openColorSelector("Bracket");
    }

    private void openColorSelector(String targetLetter) {
        EnumChatFormatting[] sortedColors;
        Minecraft mc = Minecraft.getMinecraft();
        String baseText = targetLetter.toUpperCase() + ": ";
        ChatComponentText base = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.WHITE + baseText);
        for (EnumChatFormatting color : sortedColors = new EnumChatFormatting[]{EnumChatFormatting.BLACK, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.WHITE, EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.DARK_BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.LIGHT_PURPLE}) {
            if (!color.isColor()) continue;
            ChatComponentText colorComponent = new ChatComponentText(color + "\u2b1b");
            colorComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setflagmessagecolor " + targetLetter + " " + color.name()));
            colorComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText("Click to set " + targetLetter.toUpperCase() + " to " + color + color.name())));
            base.appendSibling((IChatComponent)colorComponent);
        }
        mc.thePlayer.addChatMessage((IChatComponent)base);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    static {
        argToConfigField.put("Reason", "flagMessageComponentColor");
        argToConfigField.put("WDR", "flagMessageButtonColor");
        argToConfigField.put("Bracket", "flagMessageBracketColor");
    }
}
