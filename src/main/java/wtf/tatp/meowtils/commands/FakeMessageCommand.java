package wtf.tatp.meowtils.commands;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.CommandUtil;

public class FakeMessageCommand
extends CommandBase {
    public String getCommandName() {
        return "fakemessage";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("fakemsg");
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /fakemessage <msg>");
            return;
        }
        String message = String.join((CharSequence)" ", args);
        message = message.replace("&0", "\u00a70").replace("&1", "\u00a71").replace("&2", "\u00a72").replace("&3", "\u00a73").replace("&4", "\u00a74").replace("&5", "\u00a75").replace("&6", "\u00a76").replace("&7", "\u00a77").replace("&8", "\u00a78").replace("&9", "\u00a79").replace("&a", "\u00a7a").replace("&b", "\u00a7b").replace("&c", "\u00a7c").replace("&d", "\u00a7d").replace("&e", "\u00a7e").replace("&f", "\u00a7f").replace("&k", "\u00a7k").replace("&l", "\u00a7l").replace("&m", "\u00a7m").replace("&n", "\u00a7n").replace("&o", "\u00a7o").replace("&r", "\u00a7r");
        Meowtils.addCleanMessage(message);
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
