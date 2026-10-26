package wtf.tatp.meowtils.commands.config.flagmessagecolor;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class SetFlagMessageColorCommand
extends CommandBase {
    public String getCommandName() {
        return "setflagmessagecolor";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /setflagmessagecolor");
            return;
        }
        String letter = args[0].toLowerCase();
        String colorName = args[1].toUpperCase();
        try {
            EnumChatFormatting.valueOf((String)colorName);
            switch (letter) {
                case "reason": {
                    cfg.v.flagMessageComponentColor = colorName;
                    break;
                }
                case "wdr": {
                    cfg.v.flagMessageButtonColor = colorName;
                    break;
                }
                case "bracket": {
                    cfg.v.flagMessageBracketColor = colorName;
                    break;
                }
                default: {
                    Meowtils.addMessage(EnumChatFormatting.RED + "Invalid letter.");
                    return;
                }
            }
            cfg.save();
            Meowtils.addMessage(EnumChatFormatting.GREEN + "Set color for '" + letter.toUpperCase() + "' to " + colorName);
        }
        catch (IllegalArgumentException ex) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Invalid color name.");
        }
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
