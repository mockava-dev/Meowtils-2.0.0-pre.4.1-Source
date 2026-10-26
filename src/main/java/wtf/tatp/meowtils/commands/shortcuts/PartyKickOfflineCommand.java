package wtf.tatp.meowtils.commands.shortcuts;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class PartyKickOfflineCommand
extends CommandBase {
    public String getCommandName() {
        return "poffline";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("pko");
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/party kickoffline");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
