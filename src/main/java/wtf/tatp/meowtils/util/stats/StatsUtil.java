package wtf.tatp.meowtils.util.stats;

import com.google.gson.JsonObject;
import net.minecraft.util.EnumChatFormatting;

public class StatsUtil {
    public static String getRank(JsonObject playerData) {
        String rank;
        if (playerData.has("rank") && !playerData.get("rank").isJsonNull() && !(rank = playerData.get("rank").getAsString()).equals("NORMAL")) {
            return rank;
        }
        if (playerData.has("monthlyPackageRank") && !playerData.get("monthlyPackageRank").isJsonNull() && "SUPERSTAR".equals(playerData.get("monthlyPackageRank").getAsString())) {
            return "MVP++";
        }
        if (playerData.has("newPackageRank") && !playerData.get("newPackageRank").isJsonNull()) {
            String newRank = playerData.get("newPackageRank").getAsString();
            if ("VIP_PLUS".equals(newRank)) {
                return "VIP+";
            }
            if ("MVP_PLUS".equals(newRank)) {
                return "MVP+";
            }
            return newRank;
        }
        if (playerData.has("packageRank") && !playerData.get("packageRank").isJsonNull()) {
            return playerData.get("packageRank").getAsString();
        }
        return "NONE";
    }

    public static EnumChatFormatting getPlusColor(JsonObject playerData) {
        if (playerData.has("rankPlusColor") && !playerData.get("rankPlusColor").isJsonNull()) {
            try {
                String color = playerData.get("rankPlusColor").getAsString().toUpperCase();
                return EnumChatFormatting.valueOf((String)color);
            }
            catch (IllegalArgumentException e) {
                return EnumChatFormatting.RED;
            }
        }
        return EnumChatFormatting.RED;
    }

    public static EnumChatFormatting getRankColor(String rank) {
        switch (rank) {
            case "MVP": 
            case "MVP+": {
                return EnumChatFormatting.AQUA;
            }
            case "MVP++": {
                return EnumChatFormatting.GOLD;
            }
            case "VIP": 
            case "VIP+": {
                return EnumChatFormatting.GREEN;
            }
        }
        return EnumChatFormatting.GRAY;
    }

    public static String getFormattedRankDisplay(String rank, EnumChatFormatting plusColor) {
        if ("MVP++".equals(rank)) {
            return EnumChatFormatting.GOLD + "[MVP" + plusColor + "++" + EnumChatFormatting.GOLD + "] ";
        }
        if ("MVP+".equals(rank)) {
            return EnumChatFormatting.AQUA + "[MVP" + plusColor + "+" + EnumChatFormatting.AQUA + "] ";
        }
        if ("VIP+".equals(rank)) {
            return EnumChatFormatting.GREEN + "[VIP" + EnumChatFormatting.GOLD + "+" + EnumChatFormatting.GREEN + "] ";
        }
        if (!"NONE".equals(rank)) {
            return StatsUtil.getRankColor(rank) + "[" + rank + "] ";
        }
        return "";
    }
}
