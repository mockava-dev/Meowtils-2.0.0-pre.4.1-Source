package wtf.tatp.meowtils.commands.config.customthemecolor;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class SetThemeCommand
extends CommandBase {
    public String getCommandName() {
        return "settheme";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /settheme <m|e|o|w> <COLOR_NAME>");
            return;
        }
        String letter = args[0].toLowerCase();
        String colorName = args[1].toUpperCase();
        try {
            EnumChatFormatting.valueOf((String)colorName);
            switch (letter) {
                case "m": {
                    cfg.v.customThemeM = colorName;
                    break;
                }
                case "e": {
                    cfg.v.customThemeE = colorName;
                    break;
                }
                case "o": {
                    cfg.v.customThemeO = colorName;
                    break;
                }
                case "w": {
                    cfg.v.customThemeW = colorName;
                    break;
                }
                case "[": {
                    cfg.v.customThemeFirstBracket = colorName;
                    break;
                }
                case "]": {
                    cfg.v.customThemeSecondBracket = colorName;
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
