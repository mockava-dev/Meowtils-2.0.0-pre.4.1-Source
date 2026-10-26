package wtf.tatp.meowtils.modules.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.events.ReceivePacketEvent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.PlaySound;

public class ActionSounds
extends Module {
    private BooleanValue block;
    private BooleanValue crit;
    private long hurtTime = 0L;
    private long lastCritTime = 0L;

    public ActionSounds() {
        super("ActionSounds", "actionSoundsKey", "actionSounds", Module.Category.Utility);
        this.tooltip("Plays a sound when performing certain actions.");
        this.block = new BooleanValue("Blocked damage", "actionSoundsBlock");
        this.addBoolean(this.block);
        this.crit = new BooleanValue("Critical hit", "actionSoundsCrit");
        this.addBoolean(this.crit);
    }

    @SubscribeEvent
    public void onPacketReceived(ReceivePacketEvent event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (!cfg.v.actionSoundsBlock) {
            return;
        }
        if (event.getPacket() instanceof S19PacketEntityStatus) {
            if (((S19PacketEntityStatus)event.getPacket()).getEntity((World)this.mc.theWorld) != this.mc.thePlayer || ((S19PacketEntityStatus)event.getPacket()).getOpCode() != 2) {
                return;
            }
            if (!this.mc.thePlayer.isBlocking()) {
                return;
            }
            long now = System.currentTimeMillis();
            if (now - this.hurtTime >= 250L) {
                PlaySound.getInstance().playAnvilSound();
                this.hurtTime = now;
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(AttackEntityEvent event) {
        if (!(event.target instanceof EntityPlayer)) {
            return;
        }
        if (!cfg.v.actionSoundsCrit) {
            return;
        }
        if (this.mc.thePlayer.onGround || !(this.mc.thePlayer.fallDistance > 0.0f) || this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava() || this.mc.thePlayer.isRiding()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - this.lastCritTime > 250L) {
            PlaySound.getInstance().playCritSound();
            this.lastCritTime = now;
        }
    }
}
