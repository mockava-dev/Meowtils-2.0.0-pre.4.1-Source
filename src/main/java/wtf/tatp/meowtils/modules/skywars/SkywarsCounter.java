package wtf.tatp.meowtils.modules.skywars;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
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
import wtf.tatp.meowtils.util.GamemodeUtil;

public class SkywarsCounter
extends Module {
    private BooleanValue kill;
    private BooleanValue exp;
    private NumberValue scale;
    private ColorComponent color;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ArrayValue mode;
    private BooleanValue dynamic;
    private BooleanValue reset;
    private static int kills = 0;
    private static int xp = 0;

    public SkywarsCounter() {
        super("SkywarsCounter", "skywarsCounterKey", "skywarsCounter", Module.Category.Skywars);
        this.tooltip("Counts and displays different information about your skywars game.");
        this.color = new ColorComponent("skywarsCounter_red", "skywarsCounter_green", "skywarsCounter_blue");
        this.rgb = new ColorValue("Color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "skywarsCounterScale", Float.TYPE);
        this.addValue(this.scale);
        this.reset = new BooleanValue("Reset after game", "skywarsCounterReset");
        this.addBoolean(this.reset);
        this.dynamic = new BooleanValue("Dynamic color", "skywarsCounterDynamicColor");
        this.addBoolean(this.dynamic);
        this.kill = new BooleanValue("Count: \u00a77Kills", "skywarsCounterKill");
        this.addBoolean(this.kill);
        this.exp = new BooleanValue("Count: \u00a77Earned xp", "skywarsCounterExp");
        this.addBoolean(this.exp);
        this.mode = new ArrayValue("Display", Arrays.asList("Horizontal", "Vertical"), "skywarsCounterMode");
        this.addArray(this.mode);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String value;
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        if (msg.startsWith(this.mc.thePlayer.getName())) {
            return;
        }
        if (msg.contains(":")) {
            return;
        }
        String replaced = msg.replace(".", "");
        if (replaced.endsWith(this.mc.thePlayer.getName())) {
            ++kills;
        }
        if (msg.contains("SkyWars Experience") && !(value = msg.replaceAll("[a-zA-Z()+!\\s]", "")).isEmpty()) {
            int parsedValue = Integer.parseInt(value);
            xp += parsedValue;
        }
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        EnumChatFormatting killsColor = null;
        EnumChatFormatting expColor = null;
        float scale = cfg.v.skywarsCounterScale;
        int x = cfg.v.skywarsCounter_x;
        int y = cfg.v.skywarsCounter_y;
        int color = new Color(cfg.v.skywarsCounter_red, cfg.v.skywarsCounter_green, cfg.v.skywarsCounter_blue).getRGB();
        if (cfg.v.skywarsCounterReset && cfg.v.skywarsCounterDynamicColor) {
            killsColor = kills < 2 ? EnumChatFormatting.GREEN : (kills < 5 ? EnumChatFormatting.YELLOW : (kills < 10 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
            expColor = xp < 3 ? EnumChatFormatting.GREEN : (xp < 7 ? EnumChatFormatting.YELLOW : (xp < 15 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
        } else if (cfg.v.skywarsCounterDynamicColor) {
            killsColor = kills < 20 ? EnumChatFormatting.GREEN : (kills < 50 ? EnumChatFormatting.YELLOW : (kills < 100 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
            expColor = xp < 50 ? EnumChatFormatting.GREEN : (xp < 100 ? EnumChatFormatting.YELLOW : (xp < 200 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
        }
        if (cfg.v.skywarsCounterMode.equals("Vertical")) {
            ArrayList<String> counterStrings = new ArrayList<String>();
            if (cfg.v.skywarsCounterKill) {
                counterStrings.add("Kills: " + (cfg.v.skywarsCounterDynamicColor ? killsColor : "") + kills);
            }
            if (cfg.v.skywarsCounterExp) {
                counterStrings.add("EXP: " + (cfg.v.skywarsCounterDynamicColor ? expColor : "") + xp);
            }
            for (String text : counterStrings) {
                if (!cfg.v.smoothFont) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale((float)scale, (float)scale, (float)scale);
                    this.mc.fontRendererObj.drawStringWithShadow(text, (float)x / scale, (float)y / scale, color);
                    GlStateManager.popMatrix();
                    y += (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * scale + 3.0f);
                    continue;
                }
                Meowtils.fontRenderer.drawScaledStringWithShadow(text, x, y, color, scale * 10.0f);
                y += (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * scale + 2.0f);
            }
        } else {
            String separator = EnumChatFormatting.GRAY + " | ";
            String killString = cfg.v.skywarsCounterKill ? "Kills: " + (cfg.v.skywarsCounterDynamicColor ? killsColor : "") + kills + (cfg.v.skywarsCounterExp ? separator : "") + EnumChatFormatting.RESET : "";
            String expString = cfg.v.skywarsCounterExp ? "EXP: " + (cfg.v.skywarsCounterDynamicColor ? expColor : "") + xp + EnumChatFormatting.RESET : "";
            String text = killString + expString;
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                this.mc.fontRendererObj.drawStringWithShadow(text, (float)x / scale, (float)y / scale, color);
                GlStateManager.popMatrix();
            } else {
                Meowtils.fontRenderer.drawScaledStringWithShadow(text, x, y, color, scale * 10.0f);
            }
        }
    }

    public static void clear() {
        if (cfg.v.skywarsCounterReset) {
            kills = 0;
            xp = 0;
        }
    }
}
