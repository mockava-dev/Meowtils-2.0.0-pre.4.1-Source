package wtf.tatp.meowtils.commands.shortcuts;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class ChatAllCommand
extends CommandBase {
    public String getCommandName() {
        return "ca";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/chat all");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
