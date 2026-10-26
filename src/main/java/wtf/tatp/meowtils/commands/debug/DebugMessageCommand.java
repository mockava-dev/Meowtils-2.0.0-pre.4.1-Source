package wtf.tatp.meowtils.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class DebugMessageCommand
extends CommandBase {
    public String getCommandName() {
        return "meowdebug";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        cfg.v.debugMessages = !cfg.v.debugMessages;
        String toggleMessage = cfg.v.debugMessages ? EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "ON" : EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "OFF";
        cfg.save();
        Meowtils.addMessage("Debug messages: " + toggleMessage);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
