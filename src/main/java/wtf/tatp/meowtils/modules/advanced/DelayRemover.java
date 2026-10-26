package wtf.tatp.meowtils.modules.advanced;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.mixins.EntityLivingBaseAccessor;
import wtf.tatp.meowtils.mixins.MinecraftAccessor;
import wtf.tatp.meowtils.mixins.PlayerControllerMPAccessor;

public class DelayRemover
extends Module {
    private NumberValue useDelayTicks;
    private NumberValue breakDelay;
    private BooleanValue noBreakDelay;
    private BooleanValue noUseDelay;
    private BooleanValue noHitDelay;
    private BooleanValue noJumpDelay;
    private int useTicks = 0;
    private boolean holdingItem = false;

    public DelayRemover() {
        super("DelayRemover", "delayRemoverKey", "delayRemover", Module.Category.Advanced);
        this.tooltip("Remove or reduce certain delays.");
        this.breakDelay = new NumberValue("Break delay", 0.0, 5.0, 1.0, "ticks", "breakDelay", Integer.TYPE);
        this.addValue(this.breakDelay);
        this.useDelayTicks = new NumberValue("Extra use delay", 0.0, 4.0, 1.0, "ticks", "noUseDelayExtraTicks", Integer.TYPE);
        this.addValue(this.useDelayTicks);
        this.noBreakDelay = new BooleanValue("No break delay", "noBreakDelay");
        this.addBoolean(this.noBreakDelay);
        this.noUseDelay = new BooleanValue("No use delay", "noUseDelay");
        this.addBoolean(this.noUseDelay);
        this.noHitDelay = new BooleanValue("No hit delay", "noHitDelay");
        this.addBoolean(this.noHitDelay);
        this.noJumpDelay = new BooleanValue("No jump delay", "noJumpDelay");
        this.addBoolean(this.noJumpDelay);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (this.mc.playerController != null && cfg.v.noBreakDelay) {
            PlayerControllerMPAccessor accessor = (PlayerControllerMPAccessor)this.mc.playerController;
            int configuredDelay = cfg.v.breakDelay;
            if (accessor.getBlockHitDelay() > configuredDelay) {
                accessor.setBlockHitDelay(configuredDelay);
            }
        }
        if (cfg.v.noUseDelay) {
            ItemStack held = this.mc.thePlayer.getHeldItem();
            boolean validItem = held != null && (held.getItem() instanceof ItemFood || held.getItem() instanceof ItemPotion);
            boolean holdingUse = this.mc.gameSettings.keyBindUseItem.isKeyDown();
            if (holdingUse && validItem) {
                this.holdingItem = true;
                ++this.useTicks;
                int consumeTicks = 32 + cfg.v.noUseDelayExtraTicks;
                if (this.useTicks >= consumeTicks) {
                    KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)false);
                    this.holdingItem = false;
                    this.useTicks = 0;
                }
            } else {
                this.holdingItem = false;
                this.useTicks = 0;
            }
        }
        if (cfg.v.noHitDelay) {
            ((MinecraftAccessor)this.mc).setLeftClickCounter(0);
        }
        if (cfg.v.noJumpDelay) {
            ((EntityLivingBaseAccessor)this.mc.thePlayer).setJumpTicks(0);
        }
    }
}
