package wtf.tatp.meowtils.commands.shortcuts.statuscommands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class StatusOfflineCommand
extends CommandBase {
    public String getCommandName() {
        return "offline";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/status offline");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
