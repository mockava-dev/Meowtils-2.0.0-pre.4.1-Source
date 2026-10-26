package wtf.tatp.meowtils.util;

import java.awt.Color;
import net.minecraft.util.EnumChatFormatting;

public class ColorUtil {
    public static EnumChatFormatting getColorFromString(String colorName) {
        try {
            return EnumChatFormatting.valueOf((String)colorName.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return EnumChatFormatting.WHITE;
        }
    }

    public static EnumChatFormatting getColorFromCode(char code) {
        switch (code) {
            case '0': {
                return EnumChatFormatting.BLACK;
            }
            case '1': {
                return EnumChatFormatting.DARK_BLUE;
            }
            case '2': {
                return EnumChatFormatting.DARK_GREEN;
            }
            case '3': {
                return EnumChatFormatting.DARK_AQUA;
            }
            case '4': {
                return EnumChatFormatting.DARK_RED;
            }
            case '5': {
                return EnumChatFormatting.DARK_PURPLE;
            }
            case '6': {
                return EnumChatFormatting.GOLD;
            }
            case '7': {
                return EnumChatFormatting.GRAY;
            }
            case '8': {
                return EnumChatFormatting.DARK_GRAY;
            }
            case '9': {
                return EnumChatFormatting.BLUE;
            }
            case 'a': {
                return EnumChatFormatting.GREEN;
            }
            case 'b': {
                return EnumChatFormatting.AQUA;
            }
            case 'c': {
                return EnumChatFormatting.RED;
            }
            case 'd': {
                return EnumChatFormatting.LIGHT_PURPLE;
            }
            case 'e': {
                return EnumChatFormatting.YELLOW;
            }
            case 'f': {
                return EnumChatFormatting.WHITE;
            }
        }
        return EnumChatFormatting.WHITE;
    }

    public static int getRGBFromFormatting(EnumChatFormatting color) {
        switch (color) {
            case BLACK: {
                return new Color(0, 0, 0).getRGB();
            }
            case DARK_BLUE: {
                return new Color(0, 0, 170).getRGB();
            }
            case DARK_GREEN: {
                return new Color(0, 170, 0).getRGB();
            }
            case DARK_AQUA: {
                return new Color(0, 170, 170).getRGB();
            }
            case DARK_RED: {
                return new Color(170, 0, 0).getRGB();
            }
            case DARK_PURPLE: {
                return new Color(170, 0, 170).getRGB();
            }
            case GOLD: {
                return new Color(255, 170, 0).getRGB();
            }
            case GRAY: {
                return new Color(170, 170, 170).getRGB();
            }
            case DARK_GRAY: {
                return new Color(85, 85, 85).getRGB();
            }
            case BLUE: {
                return new Color(85, 85, 255).getRGB();
            }
            case GREEN: {
                return new Color(85, 255, 85).getRGB();
            }
            case AQUA: {
                return new Color(85, 255, 255).getRGB();
            }
            case RED: {
                return new Color(255, 85, 85).getRGB();
            }
            case LIGHT_PURPLE: {
                return new Color(255, 85, 255).getRGB();
            }
            case YELLOW: {
                return new Color(255, 255, 85).getRGB();
            }
            case WHITE: {
                return new Color(255, 255, 255).getRGB();
            }
        }
        return new Color(255, 255, 255).getRGB();
    }

    public static boolean isColorTooBright(int r, int g, int b) {
        double brightness = 0.2126 * (double)(r = Math.max(0, Math.min(255, r))) + 0.7152 * (double)(g = Math.max(0, Math.min(255, g))) + 0.0722 * (double)(b = Math.max(0, Math.min(255, b)));
        return brightness > 180.0;
    }

    public static String unformattedText(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\u00a7.", "");
    }
}
