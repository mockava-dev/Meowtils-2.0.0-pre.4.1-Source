package wtf.tatp.meowtils.commands.config;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.CommandUtil;

public class AutoTextMessageCommand
extends CommandBase {
    public String getCommandName() {
        return "autotext";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("at");
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int index;
        if (args.length < 2) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /at or /autotext <1-10> <message|clear>");
            return;
        }
        try {
            index = Integer.parseInt(args[0]) - 1;
        }
        catch (NumberFormatException e) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Must be a number between 1-10!");
            return;
        }
        if (index < 0 || index >= 10) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Must be a number between 1-10!");
            return;
        }
        if (args[1].equalsIgnoreCase("clear") && args.length == 2) {
            this.saveMessage(index, "");
            Meowtils.addMessage(EnumChatFormatting.GREEN + "Cleared AutoText message!");
            return;
        }
        String autoMessage = String.join((CharSequence)" ", Arrays.copyOfRange(args, 1, args.length));
        this.saveMessage(index, autoMessage);
        Meowtils.addMessage(EnumChatFormatting.GREEN + "AutoText " + (index + 1) + " set to: " + EnumChatFormatting.WHITE + autoMessage);
    }

    private void saveMessage(int index, String message) {
        switch (index) {
            case 0: {
                cfg.v.autoText0 = message;
                break;
            }
            case 1: {
                cfg.v.autoText1 = message;
                break;
            }
            case 2: {
                cfg.v.autoText2 = message;
                break;
            }
            case 3: {
                cfg.v.autoText3 = message;
                break;
            }
            case 4: {
                cfg.v.autoText4 = message;
                break;
            }
            case 5: {
                cfg.v.autoText5 = message;
                break;
            }
            case 6: {
                cfg.v.autoText6 = message;
                break;
            }
            case 7: {
                cfg.v.autoText7 = message;
                break;
            }
            case 8: {
                cfg.v.autoText8 = message;
                break;
            }
            case 9: {
                cfg.v.autoText9 = message;
            }
        }
        cfg.save();
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
