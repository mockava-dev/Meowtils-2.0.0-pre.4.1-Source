package wtf.tatp.meowtils.util.stats;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.stats.BedwarsStarColor;
import wtf.tatp.meowtils.util.stats.BedwarsStatsUtil;
import wtf.tatp.meowtils.util.stats.StatsUtil;

public class StatsFetcher {
    private static final Set<String> pendingFetches = Collections.synchronizedSet(new HashSet());
    private static final HashMap<String, CachedStat> statsCache = new HashMap();

    public static boolean isCached(String player) {
        CachedStat stat = statsCache.get(player);
        return stat != null && System.currentTimeMillis() - stat.timestamp < 86400000L;
    }

    public static CachedStat getCachedStat(String player) {
        return statsCache.get(player);
    }

    public static void fetchStats(String player) {
        StatsFetcher.fetchStats(player, true);
    }

    public static void fetchStats(String player, boolean showMessage) {
        new Thread(() -> {
            try {
                String line;
                String urlStr = "https://api.hypixel.net/player?key=" + cfg.v.apiKey + "&name=" + player;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
                if (!json.get("success").getAsBoolean()) {
                    if (showMessage) {
                        Meowtils.addMessage(EnumChatFormatting.RED + "Invalid API key or request failed.");
                    }
                    return;
                }
                JsonObject playerData = json.getAsJsonObject("player");
                if (playerData == null || playerData.isJsonNull()) {
                    if (showMessage) {
                        Meowtils.addMessage(EnumChatFormatting.DARK_PURPLE + player + EnumChatFormatting.RED + " might be nicked.");
                    }
                    return;
                }
                JsonObject stats = playerData.getAsJsonObject("stats").getAsJsonObject("Bedwars");
                int level = BedwarsStatsUtil.calculateBedwarsLevel(stats);
                double fkdr = stats.has("final_kills_bedwars") && stats.has("final_deaths_bedwars") ? stats.get("final_kills_bedwars").getAsDouble() / Math.max(1.0, stats.get("final_deaths_bedwars").getAsDouble()) : 0.0;
                double wlr = stats.has("wins_bedwars") && stats.has("losses_bedwars") ? stats.get("wins_bedwars").getAsDouble() / Math.max(1.0, stats.get("losses_bedwars").getAsDouble()) : 0.0;
                int winstreak = stats.has("winstreak") ? stats.get("winstreak").getAsInt() : 0;
                String rank = StatsUtil.getRank(playerData);
                EnumChatFormatting plusColor = StatsUtil.getPlusColor(playerData);
                CachedStat stat = new CachedStat(fkdr, wlr, level, winstreak, rank, plusColor);
                statsCache.put(player, stat);
                if (showMessage) {
                    StatsFetcher.displayStats(player, stat);
                }
            }
            catch (Exception e) {
                if (showMessage) {
                    Meowtils.addMessage(EnumChatFormatting.RED + "Failed to fetch stats for " + EnumChatFormatting.GRAY + player + EnumChatFormatting.RED + ".");
                }
                e.printStackTrace();
            }
        }).start();
    }

    public static void displayStats(String player, CachedStat stat) {
        EnumChatFormatting rankColor = StatsUtil.getRankColor(stat.rank);
        String rankDisplay = StatsUtil.getFormattedRankDisplay(stat.rank, stat.plusColor);
        String displayName = stat.rank == null || stat.rank.isEmpty() ? EnumChatFormatting.RESET + NameUtil.getTabDisplayName(player) : rankDisplay + rankColor + player;
        String message = BedwarsStarColor.getFormattedBedwarsLevel(stat.level) + " " + displayName + EnumChatFormatting.DARK_GRAY + " | " + BedwarsStatsUtil.getFKDRColor(stat.fkdr) + "FKDR: " + String.format("%.2f", stat.fkdr) + EnumChatFormatting.DARK_GRAY + " | " + BedwarsStatsUtil.getWLRColor(stat.wlr) + "WLR: " + String.format("%.2f", stat.wlr);
        if (BedwarsStatsUtil.getWSColor(stat.winstreak) != null) {
            message = message + EnumChatFormatting.DARK_GRAY + " | " + BedwarsStatsUtil.getWSColor(stat.winstreak) + "WS: " + stat.winstreak;
        }
        Meowtils.addMessage(message);
    }

    public static void queueFetch(String player) {
        if (!StatsFetcher.isCached(player)) {
            pendingFetches.add(player);
        }
    }

    static {
        new Thread(() -> {
            while (true) {
                try {
                    if (!pendingFetches.isEmpty()) {
                        String nextPlayer = pendingFetches.iterator().next();
                        pendingFetches.remove(nextPlayer);
                        StatsFetcher.fetchStats(nextPlayer, false);
                    }
                    Thread.sleep(150L);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "StatsQueue").start();
    }

    public static class CachedStat {
        public long timestamp = System.currentTimeMillis();
        public double fkdr;
        public double wlr;
        public int level;
        public int winstreak;
        public String rank;
        public EnumChatFormatting plusColor;

        public CachedStat(double fkdr, double wlr, int level, int winstreak, String rank, EnumChatFormatting plusColor) {
            this.fkdr = fkdr;
            this.wlr = wlr;
            this.level = level;
            this.winstreak = winstreak;
            this.rank = rank;
            this.plusColor = plusColor;
        }
    }
}
