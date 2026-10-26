package wtf.tatp.meowtils.commands.meowtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;

public class ShortcutCommands
extends CommandBase {
    public String getCommandName() {
        return "shortcuts";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String separator = " \u00bb ";
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/renick" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/nick reuse");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/away" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status away");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/busy" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status busy");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/offline" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status offline");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/online" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status online");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/block" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/block add");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/unblock" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/block remove");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/ca" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat all");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/cc" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat coop");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/cg" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat guild");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/co" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat officer");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/cp" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat party");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/gi" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/guild info");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/glo" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/guild online");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/gt" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/guild toggle");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/pd" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party demote");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/disband" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party disband");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/poffline" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party kickoffline");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/pp" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party promote");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/psa" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party settings allinvite");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/pt" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party transfer");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/rp" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/replay");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/sh" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/shout");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/slb" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/swaplobby");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/ta" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/tip all");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/tc" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/togglechat");
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
