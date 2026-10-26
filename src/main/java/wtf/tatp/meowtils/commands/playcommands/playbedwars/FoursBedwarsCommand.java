package wtf.tatp.meowtils.commands.playcommands.playbedwars;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import wtf.tatp.meowtils.Meowtils;

public class FoursBedwarsCommand
extends CommandBase {
    String Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "fours bedwars" + EnumChatFormatting.RESET;

    public FoursBedwarsCommand() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public String getCommandName() {
        return "4s";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("bw4");
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play bedwars_four_four");
        Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
