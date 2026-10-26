package wtf.tatp.meowtils.commands.playcommands.playtntgames;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import wtf.tatp.meowtils.Meowtils;

public class TntTag
extends CommandBase {
    String Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "tnt tag" + EnumChatFormatting.RESET;

    public TntTag() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public String getCommandName() {
        return "tnttag";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play tnt_tntag");
        Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
