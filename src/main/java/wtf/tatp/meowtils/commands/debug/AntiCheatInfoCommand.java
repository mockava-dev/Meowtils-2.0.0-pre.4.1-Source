package wtf.tatp.meowtils.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class AntiCheatInfoCommand
extends CommandBase {
    public String getCommandName() {
        return "meowanticheatinfo";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        cfg.v.antiCheatInfo = !cfg.v.antiCheatInfo;
        String toggleMessage = cfg.v.antiCheatInfo ? EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "ON" : EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "OFF";
        cfg.save();
        Meowtils.addMessage("AntiCheat info: " + toggleMessage);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
