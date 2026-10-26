package wtf.tatp.meowtils.commands.shortcuts.statuscommands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class StatusAwayCommand
extends CommandBase {
    public String getCommandName() {
        return "away";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/status away");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
