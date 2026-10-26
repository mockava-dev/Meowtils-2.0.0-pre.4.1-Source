package wtf.tatp.meowtils.commands.shortcuts;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import wtf.tatp.meowtils.Meowtils;

public class ReplayCommand
extends CommandBase {
    public String getCommandName() {
        return "rp";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("rpl");
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Meowtils.sendCleanMessage("/replay");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
