package wtf.tatp.meowtils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.hypixel.Denicker;
import wtf.tatp.meowtils.util.BlacklistUtil;
import wtf.tatp.meowtils.util.ColorUtil;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.stats.BedwarsStarColor;
import wtf.tatp.meowtils.util.stats.BedwarsStatsUtil;
import wtf.tatp.meowtils.util.stats.StatsFetcher;

public class NameUtil {
    public static String getTabDisplayName(String playerName) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) {
            return playerName;
        }
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScorePlayerTeam team = scoreboard.getPlayersTeam(playerName);
        if (team != null) {
            return team.getColorPrefix() + playerName + team.getColorSuffix();
        }
        return playerName;
    }

    public static String appendTabName(NetworkPlayerInfo info, String baseName) {
        StatsFetcher.CachedStat stat;
        String name = info.getGameProfile().getName();
        String uuid = info.getGameProfile().getId().toString();
        String icon = BlacklistUtil.getFormattedIcon(uuid, name);
        boolean isNicked = info.getGameProfile().getId().version() == 1;
        String nickIcon = isNicked && cfg.v.nickReveal ? Denicker.NICK_ICON : null;
        StringBuilder out = new StringBuilder();
        if (cfg.v.stats && cfg.v.tabStats && GamemodeUtil.bedwarsGame && !GamemodeUtil.bedwarsLobby && !isNicked) {
            if (StatsFetcher.isCached(name)) {
                stat = StatsFetcher.getCachedStat(name);
                if (stat != null && cfg.v.starsInTab.equals("Prefix")) {
                    out.append(BedwarsStarColor.getFormattedBedwarsLevel(stat.level)).append(" ");
                }
            } else {
                StatsFetcher.queueFetch(name);
            }
        }
        if (icon != null) {
            out.append(icon);
        }
        out.append(baseName);
        if (nickIcon != null) {
            out.append(" ").append(nickIcon);
        }
        if (cfg.v.stats && cfg.v.tabStats && GamemodeUtil.bedwarsGame && !GamemodeUtil.bedwarsLobby && !isNicked && StatsFetcher.isCached(name)) {
            stat = StatsFetcher.getCachedStat(name);
            if (stat != null && cfg.v.starsInTab.equals("Suffix")) {
                out.append(EnumChatFormatting.DARK_GRAY).append(" | ").append(BedwarsStarColor.getFormattedBedwarsLevel(stat.level)).append(BedwarsStatsUtil.getFKDRColor(stat.fkdr)).append(" ").append(String.format("%.2f", stat.fkdr));
            } else if (stat != null) {
                out.append(EnumChatFormatting.DARK_GRAY).append(" | ").append(BedwarsStatsUtil.getFKDRColor(stat.fkdr)).append(String.format("%.2f", stat.fkdr));
            }
        }
        return out.toString();
    }

    public static EnumChatFormatting getNameColor(String formattedName) {
        if (formattedName == null || formattedName.isEmpty()) {
            return EnumChatFormatting.GRAY;
        }
        for (int i = 0; i < formattedName.length() - 1; ++i) {
            if (formattedName.charAt(i) != '\u00a7') continue;
            char colorCode = formattedName.charAt(i + 1);
            return ColorUtil.getColorFromCode(colorCode);
        }
        return EnumChatFormatting.WHITE;
    }
}
