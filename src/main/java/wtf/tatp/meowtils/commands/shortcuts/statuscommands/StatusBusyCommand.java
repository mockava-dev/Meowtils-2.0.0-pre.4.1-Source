package wtf.tatp.meowtils.commands.shortcuts.statuscommands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class StatusBusyCommand
extends CommandBase {
    public String getCommandName() {
        return "busy";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/status busy");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
