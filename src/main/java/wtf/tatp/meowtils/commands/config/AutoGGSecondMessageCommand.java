package wtf.tatp.meowtils.commands.config;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class AutoGGSecondMessageCommand
extends CommandBase {
    public String getCommandName() {
        return "secondmessage";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("secondmsg", "autogg2", "autoggsecond", "autoggsecondmessage");
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String newMessage;
        if (args.length == 0) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /secondmessage <message>");
            return;
        }
        cfg.v.secondMessage = newMessage = String.join((CharSequence)" ", args);
        cfg.save();
        Meowtils.addMessage(EnumChatFormatting.GREEN + "AutoGG second message set to: " + EnumChatFormatting.DARK_AQUA + newMessage);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
