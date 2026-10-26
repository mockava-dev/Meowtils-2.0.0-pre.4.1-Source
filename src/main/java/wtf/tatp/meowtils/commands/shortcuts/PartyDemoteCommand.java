package wtf.tatp.meowtils.commands.shortcuts;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.CommandUtil;

public class PartyDemoteCommand
extends CommandBase {
    public String getCommandName() {
        return "pd";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /pd <player>");
            return;
        }
        String playerName = args[0];
        Meowtils.sendCleanMessage("/party demote " + playerName);
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
