package wtf.tatp.meowtils.modules.hypixel;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.stats.StatsFetcher;

public class Stats
extends Module {
    private BooleanValue chatStats;
    private BooleanValue tabStats;
    private ArrayValue starsInTab;

    public Stats() {
        super("Stats", "statsKey", "stats", Module.Category.Hypixel);
        this.tooltip("Displays stats of players in your game.");
        this.chatStats = new BooleanValue("Chat stats", "chatStats");
        this.addBoolean(this.chatStats);
        this.tabStats = new BooleanValue("Tab stats", "tabStats");
        this.addBoolean(this.tabStats);
        this.starsInTab = new ArrayValue("Tab stars", Arrays.asList("Prefix", "Suffix", "None"), "starsInTab");
        this.addArray(this.starsInTab);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.startsWith("ONLINE: ") && cfg.v.chatStats) {
            String[] players = message.substring(8).split(", ");
            ArrayList<String> cached = new ArrayList<String>();
            ArrayList<String> uncached = new ArrayList<String>();
            for (String player : players) {
                if (StatsFetcher.isCached(player)) {
                    cached.add(player);
                    continue;
                }
                uncached.add(player);
            }
            for (String player : cached) {
                StatsFetcher.displayStats(player, StatsFetcher.getCachedStat(player));
            }
            for (String player : uncached) {
                StatsFetcher.fetchStats(player);
            }
        }
    }
}
