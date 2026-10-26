package wtf.tatp.meowtils.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.OpacityValue;
import wtf.tatp.meowtils.modules.advanced.AntiBot;

public class AntiInvis
extends Module {
    private ColorComponent color;
    private OpacityValue opacity;

    public AntiInvis() {
        super("Anti-Invis", "antiInvisKey", "antiInvis", Module.Category.Render);
        this.tooltip("Renders invisible players semi-transparent instead.");
        this.opacity = new OpacityValue("Opacity", "antiInvisOpacity", this.color);
        this.addOpacity(this.opacity);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        float opacity = cfg.v.antiInvisOpacity / 100.0f;
        if (!this.shouldBeTransparent(player)) {
            return;
        }
        if (player.isInvisible()) {
            player.setInvisible(false);
            player.getEntityData().setBoolean("Meowtils_wasInvisible", true);
        }
        if (opacity < 1.0f) {
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)opacity);
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        EntityPlayer player = event.entityPlayer;
        float opacity = cfg.v.antiInvisOpacity / 100.0f;
        if (player.getEntityData().getBoolean("Meowtils_wasInvisible")) {
            player.setInvisible(true);
            player.getEntityData().setBoolean("Meowtils_wasInvisible", false);
        }
        if (opacity < 1.0f) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glDisable((int)3042);
        }
    }

    private boolean shouldBeTransparent(EntityPlayer player) {
        if (cfg.v.debugMessages) {
            return true;
        }
        return player != this.mc.thePlayer && player.isInvisible() && !AntiBot.isBot(player);
    }
}
