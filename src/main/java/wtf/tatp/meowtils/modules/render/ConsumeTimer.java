package wtf.tatp.meowtils.modules.render;

import java.awt.Color;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.util.ColorUtil;

public class ConsumeTimer
extends Module {
    private boolean usingItem = false;
    private int useTicksLeft = -1;
    private int lastUseDuration = 0;
    private int maxUseDuration = 32;
    private BooleanValue dynamic;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private NumberValue scale;
    private ArrayValue mode;

    public ConsumeTimer() {
        super("ConsumeTimer", "consumeTimerKey", "consumeTimer", Module.Category.Render);
        this.tooltip("Displays a countdown of how much time left to consume an item.");
        this.color = new ColorComponent("consumeTimerRed", "consumeTimerGreen", "consumeTimerBlue");
        this.rgb = new ColorValue("Text color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "consumeTimerScale", Float.TYPE);
        this.addValue(this.scale);
        this.mode = new ArrayValue("Mode", Arrays.asList("Ticks", "Seconds"), "consumeTimerMode");
        this.addArray(this.mode);
        this.dynamic = new BooleanValue("Dynamic color", "consumeTimerDynamicColor");
        this.addBoolean(this.dynamic);
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (mc.thePlayer == null) {
            return;
        }
        if (mc.thePlayer.isUsingItem()) {
            int duration = mc.thePlayer.getItemInUseDuration();
            ItemStack item = mc.thePlayer.getItemInUse();
            if (item != null) {
                int maxDuration = item.getMaxItemUseDuration();
                if (maxDuration <= 32) {
                    if (!this.usingItem || duration < this.lastUseDuration) {
                        this.usingItem = true;
                        this.maxUseDuration = maxDuration;
                        this.useTicksLeft = maxDuration - duration;
                    } else if (this.usingItem) {
                        this.useTicksLeft = this.maxUseDuration - duration;
                    }
                    this.lastUseDuration = duration;
                } else {
                    this.usingItem = false;
                    this.useTicksLeft = -1;
                    this.lastUseDuration = 0;
                }
            }
        } else {
            this.usingItem = false;
            this.useTicksLeft = -1;
            this.lastUseDuration = 0;
        }
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        float scale = cfg.v.consumeTimerScale;
        int r = cfg.v.consumeTimerRed;
        int g = cfg.v.consumeTimerGreen;
        int b = cfg.v.consumeTimerBlue;
        int color = !cfg.v.consumeTimerDynamicColor ? new Color(r, g, b).getRGB() : (this.useTicksLeft < 6 ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.DARK_RED) : (this.useTicksLeft < 12 ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.RED) : (this.useTicksLeft < 18 ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.GOLD) : (this.useTicksLeft < 24 ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.YELLOW) : new Color(cfg.v.consumeTimerRed, cfg.v.consumeTimerGreen, cfg.v.consumeTimerBlue).getRGB()))));
        if (this.useTicksLeft > 0 && this.usingItem) {
            String text;
            if (cfg.v.consumeTimerMode.equals("Ticks")) {
                text = String.valueOf(this.useTicksLeft);
            } else if (cfg.v.consumeTimerMode.equals("Seconds")) {
                double secondsLeft = (double)this.useTicksLeft / 20.0;
                text = String.format("%.1f", secondsLeft).replace(",", ".");
            } else {
                return;
            }
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                mc.fontRendererObj.drawStringWithShadow(text, (float)cfg.v.consumeTimerXPos / scale, (float)cfg.v.consumeTimerYPos / scale, color);
                GlStateManager.popMatrix();
            } else {
                Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.consumeTimerXPos, cfg.v.consumeTimerYPos, color, scale * 10.0f);
            }
        }
    }
}
