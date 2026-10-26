package wtf.tatp.meowtils.modules.advanced;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;

public class AntiBot
extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<UUID, Long> tabJoinTimes = new HashMap<UUID, Long>();
    private static final Set<UUID> currentTabList = new HashSet<UUID>();
    private ArrayValue mode;

    public AntiBot() {
        super("AntiBot", "antiBotKey", "antiBot", Module.Category.Advanced);
        this.tooltip("Excludes bots from modules.");
        this.mode = new ArrayValue("Mode", Arrays.asList("Hypixel", "Universal"), "antiBotMode");
        this.addArray(this.mode);
    }

    public static void updateTabList() {
        if (mc.getNetHandler() == null) {
            return;
        }
        HashSet<UUID> newTabList = new HashSet<UUID>();
        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            newTabList.add(info.getGameProfile().getId());
        }
        long now = System.currentTimeMillis();
        for (UUID id2 : newTabList) {
            tabJoinTimes.putIfAbsent(id2, now);
        }
        tabJoinTimes.keySet().removeIf(id -> !newTabList.contains(id));
        currentTabList.clear();
        currentTabList.addAll(newTabList);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (AntiBot.mc.thePlayer == null || AntiBot.mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (cfg.v.antiBotMode.equals("Universal")) {
            AntiBot.updateTabList();
        }
    }

    public static boolean isBot(EntityPlayer player) {
        if (player == null) {
            return true;
        }
        if (!cfg.v.antiBot) {
            return false;
        }
        UUID id = player.getUniqueID();
        if (cfg.v.antiBotMode.equals("Universal")) {
            if (!currentTabList.contains(id)) {
                return true;
            }
            Long joinTime = tabJoinTimes.get(id);
            if (joinTime == null) {
                return true;
            }
            long timeInTab = System.currentTimeMillis() - joinTime;
            return timeInTab < 10000L;
        }
        if (cfg.v.antiBotMode.equals("Hypixel")) {
            return id.version() != 1 && id.version() != 4;
        }
        return true;
    }

    public static void clear() {
        currentTabList.clear();
        tabJoinTimes.clear();
    }
}
