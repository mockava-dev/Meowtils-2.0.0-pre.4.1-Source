package wtf.tatp.meowtils.util;

import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.ColorUtil;

public class Prefix {
    public static String getPrefix() {
        String boldprefixcase;
        String prefixcase = cfg.v.lowerCasePrefix ? "m" : "M";
        String string = boldprefixcase = cfg.v.lowerCasePrefix ? EnumChatFormatting.BOLD + "m" : EnumChatFormatting.BOLD + "M";
        if (cfg.v.meowtilsPrefixMode.equals("Default")) {
            return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.BLUE + prefixcase + EnumChatFormatting.DARK_AQUA + "e" + EnumChatFormatting.AQUA + "o" + EnumChatFormatting.WHITE + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
        }
        if (cfg.v.meowtilsPrefixMode.equals("Myau")) {
            return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.RED + prefixcase + EnumChatFormatting.GOLD + "e" + EnumChatFormatting.YELLOW + "o" + EnumChatFormatting.GREEN + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
        }
        if (cfg.v.meowtilsPrefixMode.equals("Fire")) {
            return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.YELLOW + prefixcase + EnumChatFormatting.GOLD + "e" + EnumChatFormatting.RED + "o" + EnumChatFormatting.DARK_RED + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
        }
        if (cfg.v.meowtilsPrefixMode.equals("Nebula")) {
            return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.DARK_RED + prefixcase + EnumChatFormatting.RED + "e" + EnumChatFormatting.LIGHT_PURPLE + "o" + EnumChatFormatting.DARK_PURPLE + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
        }
        if (cfg.v.meowtilsPrefixMode.equals("Air")) {
            return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.AQUA + prefixcase + EnumChatFormatting.WHITE + "e" + EnumChatFormatting.GRAY + "o" + EnumChatFormatting.DARK_GRAY + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
        }
        if (cfg.v.meowtilsPrefixMode.equals("Custom")) {
            return ColorUtil.getColorFromString(cfg.v.customThemeFirstBracket) + "[" + ColorUtil.getColorFromString(cfg.v.customThemeM) + prefixcase + ColorUtil.getColorFromString(cfg.v.customThemeE) + "e" + ColorUtil.getColorFromString(cfg.v.customThemeO) + "o" + ColorUtil.getColorFromString(cfg.v.customThemeW) + "w" + ColorUtil.getColorFromString(cfg.v.customThemeSecondBracket) + "] " + EnumChatFormatting.WHITE;
        }
        if (cfg.v.meowtilsPrefixMode.equals("Short")) {
            return ColorUtil.getColorFromString(cfg.v.customThemeFirstBracket) + "[" + ColorUtil.getColorFromString(cfg.v.customThemeM) + boldprefixcase + ColorUtil.getColorFromString(cfg.v.customThemeSecondBracket) + "] " + EnumChatFormatting.WHITE;
        }
        return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.BLUE + prefixcase + EnumChatFormatting.DARK_AQUA + "e" + EnumChatFormatting.AQUA + "o" + EnumChatFormatting.WHITE + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
    }
}
