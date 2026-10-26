package wtf.tatp.meowtils.commands.shortcuts;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;

public class SwapLobbyCommand
extends CommandBase {
    public String getCommandName() {
        return "slb";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /slb <Lobby number>");
            return;
        }
        String lobby = args[0];
        Meowtils.sendCleanMessage("/swaplobby " + lobby);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
