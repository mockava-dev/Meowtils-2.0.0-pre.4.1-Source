package wtf.tatp.meowtils.commands.playcommands.playduels;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import wtf.tatp.meowtils.Meowtils;

public class BlitzDuels
extends CommandBase {
    String Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "blitz duels" + EnumChatFormatting.RESET;

    public BlitzDuels() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public String getCommandName() {
        return "blitz";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play duels_blitz_duel");
        Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
