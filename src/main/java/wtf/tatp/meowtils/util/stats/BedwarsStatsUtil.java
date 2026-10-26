package wtf.tatp.meowtils.util.stats;

import com.google.gson.JsonObject;
import net.minecraft.util.EnumChatFormatting;

public class BedwarsStatsUtil {
    public static int calculateBedwarsLevel(JsonObject stats) {
        if (!stats.has("Experience") || stats.get("Experience").isJsonNull()) {
            return 0;
        }
        int exp = stats.get("Experience").getAsInt();
        int[] levelExp = new int[]{500, 1000, 2000, 3500};
        int fullCycleXP = 487000;
        int level = 0;
        while (exp >= fullCycleXP) {
            exp -= fullCycleXP;
            level += 100;
        }
        for (int i = 0; i < 4; ++i) {
            if (exp < levelExp[i]) {
                return level;
            }
            exp -= levelExp[i];
            ++level;
        }
        return level += exp / 5000;
    }

    public static EnumChatFormatting getFKDRColor(double fkdr) {
        if (fkdr == 0.0) {
            return EnumChatFormatting.DARK_BLUE;
        }
        if (fkdr < 1.0) {
            return EnumChatFormatting.GRAY;
        }
        if (fkdr < 3.0) {
            return EnumChatFormatting.WHITE;
        }
        if (fkdr < 5.0) {
            return EnumChatFormatting.GREEN;
        }
        if (fkdr < 7.0) {
            return EnumChatFormatting.DARK_GREEN;
        }
        if (fkdr < 10.0) {
            return EnumChatFormatting.YELLOW;
        }
        if (fkdr < 20.0) {
            return EnumChatFormatting.GOLD;
        }
        if (fkdr < 30.0) {
            return EnumChatFormatting.RED;
        }
        if (fkdr < 50.0) {
            return EnumChatFormatting.DARK_RED;
        }
        if (fkdr < 100.0) {
            return EnumChatFormatting.LIGHT_PURPLE;
        }
        return EnumChatFormatting.DARK_PURPLE;
    }

    public static EnumChatFormatting getWLRColor(double wlr) {
        if (wlr == 0.0) {
            return EnumChatFormatting.DARK_BLUE;
        }
        if (wlr < 0.3) {
            return EnumChatFormatting.GRAY;
        }
        if (wlr < 0.9) {
            return EnumChatFormatting.WHITE;
        }
        if (wlr < 1.5) {
            return EnumChatFormatting.GREEN;
        }
        if (wlr < 2.0) {
            return EnumChatFormatting.DARK_GREEN;
        }
        if (wlr < 3.0) {
            return EnumChatFormatting.YELLOW;
        }
        if (wlr < 6.0) {
            return EnumChatFormatting.GOLD;
        }
        if (wlr < 9.0) {
            return EnumChatFormatting.RED;
        }
        if (wlr < 15.0) {
            return EnumChatFormatting.DARK_RED;
        }
        if (wlr < 30.0) {
            return EnumChatFormatting.LIGHT_PURPLE;
        }
        return EnumChatFormatting.DARK_PURPLE;
    }

    public static EnumChatFormatting getWSColor(int ws) {
        if (ws == 0) {
            return null;
        }
        if (ws < 3) {
            return EnumChatFormatting.GRAY;
        }
        if (ws < 5) {
            return EnumChatFormatting.WHITE;
        }
        if (ws < 15) {
            return EnumChatFormatting.GREEN;
        }
        if (ws < 20) {
            return EnumChatFormatting.DARK_GREEN;
        }
        if (ws < 30) {
            return EnumChatFormatting.YELLOW;
        }
        if (ws < 40) {
            return EnumChatFormatting.GOLD;
        }
        if (ws < 50) {
            return EnumChatFormatting.RED;
        }
        if (ws < 80) {
            return EnumChatFormatting.DARK_RED;
        }
        return EnumChatFormatting.LIGHT_PURPLE;
    }
}
