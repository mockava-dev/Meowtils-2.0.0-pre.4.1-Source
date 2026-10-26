package wtf.tatp.meowtils.commands.stats;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.CommandUtil;
import wtf.tatp.meowtils.util.stats.StatsFetcher;

public class BedwarsStatsCommand
extends CommandBase {
    public String getCommandName() {
        return "s";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("statbw", "meowstatbw");
    }

    
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String player;
        if (args.length == 0) {
            if (Meowtils.mc.thePlayer == null) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Could not determine your username.");
                return;
            }
            player = Meowtils.mc.thePlayer.getName();
        } else {
            player = args[0];
        }
        StatsFetcher.fetchStats(player);
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
