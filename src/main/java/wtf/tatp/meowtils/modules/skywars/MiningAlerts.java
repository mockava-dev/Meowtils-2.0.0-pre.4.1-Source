package wtf.tatp.meowtils.modules.skywars;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.events.ReceivePacketEvent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.PlaySound;

public class MiningAlerts
extends Module {
    private BooleanValue ping;

    public MiningAlerts() {
        super("MiningAlerts", "miningAlertsKey", "miningAlerts", Module.Category.Skywars);
        this.tooltip("Alerts you when a player mines Diamond Ore.");
        this.ping = new BooleanValue("Ping sound", "miningAlertsSound");
        this.addBoolean(this.ping);
    }

    @SubscribeEvent
    public void onPacketReceived(ReceivePacketEvent event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (!(event.getPacket() instanceof S25PacketBlockBreakAnim)) {
            return;
        }
        if (!GamemodeUtil.skywarsGame && !GamemodeUtil.skywarsMiniGame) {
            return;
        }
        S25PacketBlockBreakAnim packet = (S25PacketBlockBreakAnim)event.getPacket();
        BlockPos pos = packet.getPosition();
        int progress = packet.getProgress();
        if (this.mc.theWorld.getBlockState(pos).getBlock() != Blocks.diamond_ore) {
            return;
        }
        if (progress != 9) {
            return;
        }
        EntityPlayer closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            double distanceSq;
            if (player == null || player == this.mc.thePlayer || !((distanceSq = player.getDistanceSq(pos)) < closestDistance)) continue;
            closestDistance = distanceSq;
            closest = player;
        }
        if (closest != null) {
            String playerName = closest.getName();
            Meowtils.addMessage(NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GRAY + " mined " + EnumChatFormatting.AQUA + "Diamond Ore");
            if (cfg.v.miningAlertsSound) {
                PlaySound.getInstance().playPingSoundDeep();
            }
        }
    }
}
