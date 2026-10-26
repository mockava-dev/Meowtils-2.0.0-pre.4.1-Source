package wtf.tatp.meowtils.commands.shortcuts;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.CommandUtil;

public class BlockAddCommand
extends CommandBase {
    public String getCommandName() {
        return "block";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String playerName;
        String arg = args[0];
        if (args.length < 1) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /block <player>");
            return;
        }
        if (arg.equalsIgnoreCase("list")) {
            Meowtils.sendCleanMessage("/block list");
            return;
        }
        if (arg.equalsIgnoreCase("help")) {
            Meowtils.sendCleanMessage("/block help");
            return;
        }
        if (arg.equalsIgnoreCase("add") || arg.equalsIgnoreCase("remove")) {
            playerName = args[1];
            Meowtils.sendCleanMessage("/block " + arg.toLowerCase() + " " + playerName);
        }
        playerName = arg;
        Meowtils.sendCleanMessage("/block add " + playerName);
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
