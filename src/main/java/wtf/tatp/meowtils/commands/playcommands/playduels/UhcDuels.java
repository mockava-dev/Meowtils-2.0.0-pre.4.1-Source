package wtf.tatp.meowtils.commands.playcommands.playduels;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import wtf.tatp.meowtils.Meowtils;

public class UhcDuels
extends CommandBase {
    String Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "uhc duels" + EnumChatFormatting.RESET;

    public UhcDuels() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public String getCommandName() {
        return "uhcduel";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("uhc");
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play duels_uhc_duel");
        Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
