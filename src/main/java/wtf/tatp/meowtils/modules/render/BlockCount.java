package wtf.tatp.meowtils.modules.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.util.PlaySound;

public class BlockCount
extends Module {
    private BooleanValue alert;
    private BooleanValue sound;
    private NumberValue threshold;
    private NumberValue scale;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private static boolean alerted = false;

    public BlockCount() {
        super("BlockCount", "blockCountKey", "blockCount", Module.Category.Render);
        this.tooltip("Displays block count on screen");
        this.color = new ColorComponent("blockCount_red", "blockCount_green", "blockCount_blue");
        this.rgb = new ColorValue("Text color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "blockCountScale", Float.TYPE);
        this.addValue(this.scale);
        this.threshold = new NumberValue("Alert threshold", 1.0, 16.0, 1.0, "blocks", "blockCountThreshold", Integer.TYPE);
        this.addValue(this.threshold);
        this.alert = new BooleanValue("Alert when low", "blockCountAlert");
        this.addBoolean(this.alert);
        this.sound = new BooleanValue("Ping sound", "blockCountSound");
        this.addBoolean(this.sound);
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        int color;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (mc.thePlayer.getHeldItem() == null) {
            return;
        }
        if (mc.currentScreen != null) {
            return;
        }
        int currentStack = mc.thePlayer.getHeldItem().stackSize;
        float scale = cfg.v.blockCountScale;
        int threshold = cfg.v.blockCountThreshold;
        int x = cfg.v.blockCount_x;
        int y = cfg.v.blockCount_y;
        int n = color = currentStack <= threshold ? new Color(170, 0, 0).getRGB() : new Color(cfg.v.blockCount_red, cfg.v.blockCount_green, cfg.v.blockCount_blue).getRGB();
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(currentStack), (float)x / scale, (float)y / scale, color);
                GlStateManager.popMatrix();
            } else {
                Meowtils.fontRenderer.drawScaledStringWithShadow(String.valueOf(currentStack), x, y, color, scale * 10.0f);
            }
            if (currentStack == threshold && !alerted) {
                if (cfg.v.blockCountAlert) {
                    String end = cfg.v.blockCountThreshold == 1 ? " block left!" : " blocks left!";
                    Meowtils.addMessage(EnumChatFormatting.RED + "You only have " + currentStack + end);
                }
                if (cfg.v.blockCountSound) {
                    PlaySound.getInstance().playPingSoundDeep();
                }
                alerted = true;
            } else if (currentStack > threshold) {
                alerted = false;
            }
        }
    }
}
