package wtf.tatp.meowtils.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.modules.advanced.AntiBot;

public class InstantHurt
extends Module {
    public InstantHurt() {
        super("InstantHurt", "instantHurtKey", "instantHurt", Module.Category.Render);
        this.tooltip("Instantly render hurt animation on players clientside.");
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!(event.target instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entity = (EntityPlayer)event.target;
        if (AntiBot.isBot(entity)) {
            return;
        }
        if (entity.hurtTime <= 0) {
            entity.performHurtAnimation();
        }
    }
}
