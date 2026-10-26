package wtf.tatp.meowtils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;

public class ColorCodeCommand
extends CommandBase {
    public String getCommandName() {
        return "meowcolor";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY + "" + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
        Meowtils.addCleanMessage(EnumChatFormatting.BLACK + "&0 >> BLACK");
        Meowtils.addCleanMessage(EnumChatFormatting.DARK_BLUE + "&1 >> DARK_BLUE");
        Meowtils.addCleanMessage(EnumChatFormatting.DARK_GREEN + "&2 >> DARK_GREEN");
        Meowtils.addCleanMessage(EnumChatFormatting.DARK_AQUA + "&3 >> DARK_AQUA");
        Meowtils.addCleanMessage(EnumChatFormatting.DARK_RED + "&4 >> DARK_RED");
        Meowtils.addCleanMessage(EnumChatFormatting.DARK_PURPLE + "&5 >> DARK_PURPLE");
        Meowtils.addCleanMessage(EnumChatFormatting.GOLD + "&6 >> GOLD");
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY + "&7 >> GRAY");
        Meowtils.addCleanMessage(EnumChatFormatting.DARK_GRAY + "&8 >> DARK_GRAY");
        Meowtils.addCleanMessage(EnumChatFormatting.BLUE + "&9 >> BLUE");
        Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "&a >> GREEN");
        Meowtils.addCleanMessage(EnumChatFormatting.AQUA + "&b >> AQUA");
        Meowtils.addCleanMessage(EnumChatFormatting.RED + "&c >> RED");
        Meowtils.addCleanMessage(EnumChatFormatting.LIGHT_PURPLE + "&d >> LIGHT_PURPLE");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "&e >> YELLOW");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&f >> WHITE");
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.UNDERLINE + "Formatting codes:");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&k >> " + EnumChatFormatting.OBFUSCATED + "OBFUSCATED");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&m >> " + EnumChatFormatting.STRIKETHROUGH + "STRIKETHROUGH");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&o >> " + EnumChatFormatting.ITALIC + "ITALIC");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&l >> " + EnumChatFormatting.BOLD + "BOLD");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&n >> " + EnumChatFormatting.UNDERLINE + "UNDERLINE");
        Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&r >> RESET");
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY + "" + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
