package wtf.tatp.meowtils.commands.stats;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.CommandUtil;
import wtf.tatp.meowtils.util.Prefix;

public class PlanckeCommand
extends CommandBase {
    public String getCommandName() {
        return "plancke";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("pla");
    }

    
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String player;
        if (args.length == 0) {
            if (Meowtils.mc.thePlayer == null) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Could not determine your username.");
                return;
            }
            player = Meowtils.mc.thePlayer.getName();
        } else {
            player = args[0];
        }
        ChatComponentText message = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + player + "'s stats on Plancke");
        message.setChatStyle(message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://plancke.io/hypixel/player/stats/" + player)).setUnderlined(Boolean.valueOf(true)));
        Meowtils.mc.thePlayer.addChatMessage((IChatComponent)message);
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
