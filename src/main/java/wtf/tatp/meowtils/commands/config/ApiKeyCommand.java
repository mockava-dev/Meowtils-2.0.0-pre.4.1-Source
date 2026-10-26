package wtf.tatp.meowtils.commands.config;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class ApiKeyCommand
extends CommandBase {
    public String getCommandName() {
        return "meowapi";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("meowapikey", "mapikey");
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String newMessage;
        if (args.length == 0) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /meowapi <key>");
            return;
        }
        cfg.v.apiKey = newMessage = String.join((CharSequence)" ", args);
        cfg.save();
        Meowtils.addMessage(EnumChatFormatting.GREEN + "Set API key.");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
