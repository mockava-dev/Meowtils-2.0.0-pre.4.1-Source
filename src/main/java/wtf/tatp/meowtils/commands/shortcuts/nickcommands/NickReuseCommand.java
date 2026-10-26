package wtf.tatp.meowtils.commands.shortcuts.nickcommands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class NickReuseCommand
extends CommandBase {
    public String getCommandName() {
        return "renick";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/nick reuse");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
