package wtf.tatp.meowtils.util.stats;

import net.minecraft.util.EnumChatFormatting;

public class BedwarsStarColor {
    private static LevelFormat getFormatForLevel(int level) {
        if (level < 100) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.GRAY}, EnumChatFormatting.GRAY, "\u272b", EnumChatFormatting.GRAY, false);
        }
        if (level < 200) {
            return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[]{EnumChatFormatting.WHITE}, EnumChatFormatting.WHITE, "\u272b", EnumChatFormatting.WHITE, false);
        }
        if (level < 300) {
            return new LevelFormat(EnumChatFormatting.GOLD, new EnumChatFormatting[]{EnumChatFormatting.GOLD}, EnumChatFormatting.GOLD, "\u272b", EnumChatFormatting.GOLD, false);
        }
        if (level < 400) {
            return new LevelFormat(EnumChatFormatting.AQUA, new EnumChatFormatting[]{EnumChatFormatting.AQUA}, EnumChatFormatting.AQUA, "\u272b", EnumChatFormatting.AQUA, false);
        }
        if (level < 500) {
            return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[]{EnumChatFormatting.DARK_GREEN}, EnumChatFormatting.DARK_GREEN, "\u272b", EnumChatFormatting.DARK_GREEN, false);
        }
        if (level < 600) {
            return new LevelFormat(EnumChatFormatting.DARK_AQUA, new EnumChatFormatting[]{EnumChatFormatting.DARK_AQUA}, EnumChatFormatting.DARK_AQUA, "\u272b", EnumChatFormatting.DARK_AQUA, false);
        }
        if (level < 700) {
            return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED}, EnumChatFormatting.DARK_RED, "\u272b", EnumChatFormatting.DARK_RED, false);
        }
        if (level < 800) {
            return new LevelFormat(EnumChatFormatting.LIGHT_PURPLE, new EnumChatFormatting[]{EnumChatFormatting.LIGHT_PURPLE}, EnumChatFormatting.LIGHT_PURPLE, "\u272b", EnumChatFormatting.LIGHT_PURPLE, false);
        }
        if (level < 900) {
            return new LevelFormat(EnumChatFormatting.BLUE, new EnumChatFormatting[]{EnumChatFormatting.BLUE}, EnumChatFormatting.BLUE, "\u272b", EnumChatFormatting.BLUE, false);
        }
        if (level < 1000) {
            return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[]{EnumChatFormatting.DARK_PURPLE}, EnumChatFormatting.DARK_PURPLE, "\u272b", EnumChatFormatting.DARK_PURPLE, false);
        }
        if (level < 1100) {
            return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[]{EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA}, EnumChatFormatting.LIGHT_PURPLE, "\u272b", EnumChatFormatting.DARK_PURPLE, false);
        }
        if (level < 1200) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.WHITE}, EnumChatFormatting.GRAY, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1300) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.YELLOW}, EnumChatFormatting.GOLD, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1400) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.AQUA}, EnumChatFormatting.DARK_AQUA, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1500) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.GREEN}, EnumChatFormatting.DARK_GREEN, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1600) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.DARK_AQUA}, EnumChatFormatting.BLUE, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1700) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.RED}, EnumChatFormatting.DARK_RED, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1800) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.LIGHT_PURPLE}, EnumChatFormatting.DARK_PURPLE, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 1900) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.BLUE}, EnumChatFormatting.DARK_BLUE, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 2000) {
            return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[]{EnumChatFormatting.DARK_PURPLE}, EnumChatFormatting.DARK_GRAY, "\u272a", EnumChatFormatting.GRAY, false);
        }
        if (level < 2100) {
            return new LevelFormat(EnumChatFormatting.DARK_GRAY, new EnumChatFormatting[]{EnumChatFormatting.GRAY, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY}, EnumChatFormatting.GRAY, "\u272a", EnumChatFormatting.DARK_GRAY, false);
        }
        if (level < 2200) {
            return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[]{EnumChatFormatting.WHITE, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD}, EnumChatFormatting.GOLD, "\u269d", EnumChatFormatting.GOLD, true);
        }
        if (level < 2300) {
            return new LevelFormat(EnumChatFormatting.GOLD, new EnumChatFormatting[]{EnumChatFormatting.GOLD, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.AQUA}, EnumChatFormatting.DARK_AQUA, "\u269d", EnumChatFormatting.DARK_AQUA, true);
        }
        if (level < 2400) {
            return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[]{EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.GOLD}, EnumChatFormatting.YELLOW, "\u269d", EnumChatFormatting.YELLOW, true);
        }
        if (level < 2500) {
            return new LevelFormat(EnumChatFormatting.AQUA, new EnumChatFormatting[]{EnumChatFormatting.AQUA, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY}, EnumChatFormatting.GRAY, "\u269d", EnumChatFormatting.DARK_GRAY, true);
        }
        if (level < 2600) {
            return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[]{EnumChatFormatting.WHITE, EnumChatFormatting.GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN}, EnumChatFormatting.DARK_GREEN, "\u269d", EnumChatFormatting.DARK_GREEN, true);
        }
        if (level < 2700) {
            return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE}, EnumChatFormatting.LIGHT_PURPLE, "\u269d", EnumChatFormatting.DARK_PURPLE, true);
        }
        if (level < 2800) {
            return new LevelFormat(EnumChatFormatting.YELLOW, new EnumChatFormatting[]{EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.DARK_GRAY}, EnumChatFormatting.DARK_GRAY, "\u269d", EnumChatFormatting.DARK_GRAY, true);
        }
        if (level < 2900) {
            return new LevelFormat(EnumChatFormatting.GREEN, new EnumChatFormatting[]{EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GOLD}, EnumChatFormatting.GOLD, "\u269d", EnumChatFormatting.YELLOW, true);
        }
        if (level < 3000) {
            return new LevelFormat(EnumChatFormatting.AQUA, new EnumChatFormatting[]{EnumChatFormatting.AQUA, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.BLUE}, EnumChatFormatting.BLUE, "\u269d", EnumChatFormatting.DARK_BLUE, true);
        }
        if (level < 3100) {
            return new LevelFormat(EnumChatFormatting.YELLOW, new EnumChatFormatting[]{EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD, EnumChatFormatting.GOLD, EnumChatFormatting.RED}, EnumChatFormatting.RED, "\u269d", EnumChatFormatting.DARK_RED, true);
        }
        if (level < 3200) {
            return new LevelFormat(EnumChatFormatting.BLUE, new EnumChatFormatting[]{EnumChatFormatting.BLUE, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.GOLD}, EnumChatFormatting.GOLD, "\u2725", EnumChatFormatting.YELLOW, false);
        }
        if (level < 3300) {
            return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.DARK_RED}, EnumChatFormatting.RED, "\u2725", EnumChatFormatting.RED, false);
        }
        if (level < 3400) {
            return new LevelFormat(EnumChatFormatting.BLUE, new EnumChatFormatting[]{EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.RED}, EnumChatFormatting.RED, "\u2725", EnumChatFormatting.DARK_RED, false);
        }
        if (level < 3500) {
            return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[]{EnumChatFormatting.GREEN, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.DARK_PURPLE}, EnumChatFormatting.DARK_PURPLE, "\u2725", EnumChatFormatting.DARK_GREEN, false);
        }
        if (level < 3600) {
            return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[]{EnumChatFormatting.RED, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_GREEN}, EnumChatFormatting.GREEN, "\u2725", EnumChatFormatting.GREEN, false);
        }
        if (level < 3700) {
            return new LevelFormat(EnumChatFormatting.GREEN, new EnumChatFormatting[]{EnumChatFormatting.GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE}, EnumChatFormatting.BLUE, "\u2725", EnumChatFormatting.DARK_BLUE, false);
        }
        if (level < 3800) {
            return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.AQUA}, EnumChatFormatting.DARK_AQUA, "\u2725", EnumChatFormatting.DARK_AQUA, false);
        }
        if (level < 3900) {
            return new LevelFormat(EnumChatFormatting.DARK_BLUE, new EnumChatFormatting[]{EnumChatFormatting.DARK_BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.DARK_PURPLE}, EnumChatFormatting.LIGHT_PURPLE, "\u2725", EnumChatFormatting.DARK_BLUE, false);
        }
        if (level < 4000) {
            return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[]{EnumChatFormatting.RED, EnumChatFormatting.GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_AQUA}, EnumChatFormatting.BLUE, "\u2725", EnumChatFormatting.BLUE, false);
        }
        if (level < 4100) {
            return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[]{EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.GOLD}, EnumChatFormatting.GOLD, "\u2725", EnumChatFormatting.YELLOW, false);
        }
        if (level < 4200) {
            return new LevelFormat(EnumChatFormatting.YELLOW, new EnumChatFormatting[]{EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE}, EnumChatFormatting.LIGHT_PURPLE, "\u2725", EnumChatFormatting.DARK_PURPLE, false);
        }
        if (level < 4300) {
            return new LevelFormat(EnumChatFormatting.DARK_BLUE, new EnumChatFormatting[]{EnumChatFormatting.BLUE, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.WHITE}, EnumChatFormatting.GRAY, "\u2725", EnumChatFormatting.GRAY, false);
        }
        if (level < 4400) {
            return new LevelFormat(EnumChatFormatting.BLACK, new EnumChatFormatting[]{EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.DARK_PURPLE}, EnumChatFormatting.DARK_PURPLE, "\u2725", EnumChatFormatting.BLACK, false);
        }
        if (level < 4500) {
            return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[]{EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD}, EnumChatFormatting.DARK_PURPLE, "\u2725", EnumChatFormatting.LIGHT_PURPLE, false);
        }
        if (level < 4600) {
            return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[]{EnumChatFormatting.WHITE, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.DARK_AQUA}, EnumChatFormatting.DARK_AQUA, "\u2725", EnumChatFormatting.DARK_AQUA, false);
        }
        if (level < 4700) {
            return new LevelFormat(EnumChatFormatting.DARK_AQUA, new EnumChatFormatting[]{EnumChatFormatting.AQUA, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD}, EnumChatFormatting.LIGHT_PURPLE, "\u2725", EnumChatFormatting.DARK_PURPLE, false);
        }
        if (level < 4800) {
            return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.BLUE}, EnumChatFormatting.DARK_BLUE, "\u2725", EnumChatFormatting.BLUE, false);
        }
        if (level < 4900) {
            return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[]{EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW}, EnumChatFormatting.AQUA, "\u2725", EnumChatFormatting.DARK_AQUA, false);
        }
        if (level < 5000) {
            return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[]{EnumChatFormatting.GREEN, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.GREEN}, EnumChatFormatting.GREEN, "\u2725", EnumChatFormatting.DARK_GREEN, false);
        }
        if (level < 5100) {
            return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE}, EnumChatFormatting.DARK_BLUE, "\u2725", EnumChatFormatting.BLACK, false);
        }
        return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[]{EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE}, EnumChatFormatting.DARK_BLUE, "\u2725", EnumChatFormatting.BLACK, false);
    }

    public static String getFormattedBedwarsLevel(int level) {
        LevelFormat format = BedwarsStarColor.getFormatForLevel(level);
        String levelStr = String.valueOf(level);
        StringBuilder sb = new StringBuilder();
        sb.append(format.leftBracketColor).append("[");
        for (int i = 0; i < levelStr.length(); ++i) {
            EnumChatFormatting color = i < format.digitColors.length ? format.digitColors[i] : format.digitColors[format.digitColors.length - 1];
            sb.append(color).append(levelStr.charAt(i));
        }
        if (format.starColor != null) {
            sb.append(format.starColor);
        }
        if (format.boldStar) {
            sb.append(EnumChatFormatting.BOLD);
        }
        sb.append(format.starIcon);
        sb.append(EnumChatFormatting.RESET);
        sb.append(format.rightBracketColor).append("]");
        return sb.toString();
    }

    static class LevelFormat {
        public final boolean boldStar;
        EnumChatFormatting leftBracketColor;
        EnumChatFormatting[] digitColors;
        EnumChatFormatting starColor;
        String starIcon;
        EnumChatFormatting rightBracketColor;

        public LevelFormat(EnumChatFormatting leftBracketColor, EnumChatFormatting[] digitColors, EnumChatFormatting starColor, String starIcon, EnumChatFormatting rightBracketColor, boolean boldStar) {
            this.leftBracketColor = leftBracketColor;
            this.digitColors = digitColors;
            this.starColor = starColor;
            this.starIcon = starIcon;
            this.rightBracketColor = rightBracketColor;
            this.boldStar = boldStar;
        }
    }
}
