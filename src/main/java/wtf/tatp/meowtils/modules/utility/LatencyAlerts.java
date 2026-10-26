package wtf.tatp.meowtils.modules.utility;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.events.ReceivePacketEvent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;

public class LatencyAlerts
extends Module {
    private BooleanValue ignoreLimbo;
    private NumberValue thresHold;
    private long lastPacket;
    private long lastAlert;

    public LatencyAlerts() {
        super("LatencyAlerts", "latencyAlertsKey", "latencyAlerts", Module.Category.Utility);
        this.tooltip("Warns you in chat when you lose connection to the server. \n May not work on all servers.");
        this.thresHold = new NumberValue("Latency threshold", 0.0, 3000.0, 50.0, "ms", "latencyLossThreshold", Integer.TYPE);
        this.addValue(this.thresHold);
        this.ignoreLimbo = new BooleanValue("Ignore limbo", "latencyAlertsIgnoreLimbo");
        this.addBoolean(this.ignoreLimbo);
    }

    @SubscribeEvent
    public void onPacketReceive(ReceivePacketEvent e) {
        this.lastPacket = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.mc.isSingleplayer() || this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
        if (sidebar == null && cfg.v.latencyAlertsIgnoreLimbo) {
            return;
        }
        long now = System.currentTimeMillis();
        long last = now - this.lastPacket;
        if (last >= (long)cfg.v.latencyLossThreshold && now - this.lastAlert >= 3000L) {
            Meowtils.addMessage(EnumChatFormatting.DARK_GRAY + "Packet loss detected: " + EnumChatFormatting.RED + last + "ms");
            this.lastAlert = now;
        }
    }
}
