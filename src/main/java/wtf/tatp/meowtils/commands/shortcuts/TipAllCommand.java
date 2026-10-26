package wtf.tatp.meowtils.commands.shortcuts;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class TipAllCommand
extends CommandBase {
    public String getCommandName() {
        return "ta";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/tip all");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
