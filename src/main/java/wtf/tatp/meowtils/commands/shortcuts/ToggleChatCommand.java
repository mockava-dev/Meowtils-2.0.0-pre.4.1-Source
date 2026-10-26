package wtf.tatp.meowtils.commands.shortcuts;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class ToggleChatCommand
extends CommandBase {
    public String getCommandName() {
        return "tc";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/togglechat");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
