package wtf.tatp.meowtils.modules.bedwars;

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

public class BedwarsCounter
extends Module {
    private BooleanValue kill;
    private BooleanValue finalKill;
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
    private static int finalKills = 0;
    private static int xp = 0;

    public BedwarsCounter() {
        super("BedwarsCounter", "bedwarsCounterKey", "bedwarsCounter", Module.Category.Bedwars);
        this.tooltip("Counts and displays different information about your bedwars game.");
        this.color = new ColorComponent("bedwarsCounter_red", "bedwarsCounter_green", "bedwarsCounter_blue");
        this.rgb = new ColorValue("Color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "bedwarsCounterScale", Float.TYPE);
        this.addValue(this.scale);
        this.reset = new BooleanValue("Reset after game", "bedwarsCounterReset");
        this.addBoolean(this.reset);
        this.dynamic = new BooleanValue("Dynamic color", "bedwarsCounterDynamicColor");
        this.addBoolean(this.dynamic);
        this.kill = new BooleanValue("Count: \u00a77Kills", "bedwarsCounterKill");
        this.addBoolean(this.kill);
        this.finalKill = new BooleanValue("Count: \u00a77Final kills", "bedwarsCounterFinal");
        this.addBoolean(this.finalKill);
        this.exp = new BooleanValue("Count: \u00a77Earned xp", "bedwarsCounterExp");
        this.addBoolean(this.exp);
        this.mode = new ArrayValue("Display", Arrays.asList("Horizontal", "Vertical"), "bedwarsCounterMode");
        this.addArray(this.mode);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String value;
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
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
        if (msg.contains(this.mc.thePlayer.getName()) && msg.contains("FINAL KILL!")) {
            ++finalKills;
        }
        if (msg.contains("Bed Wars XP") && !(value = msg.replaceAll("[a-zA-Z()+\\s]", "")).isEmpty()) {
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
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        EnumChatFormatting killsColor = null;
        EnumChatFormatting finalsColor = null;
        EnumChatFormatting expColor = null;
        float scale = cfg.v.bedwarsCounterScale;
        int x = cfg.v.bedwarsCounter_x;
        int y = cfg.v.bedwarsCounter_y;
        int color = new Color(cfg.v.bedwarsCounter_red, cfg.v.bedwarsCounter_green, cfg.v.bedwarsCounter_blue).getRGB();
        if (cfg.v.bedwarsCounterReset && cfg.v.bedwarsCounterDynamicColor) {
            killsColor = kills < 5 ? EnumChatFormatting.GREEN : (kills < 10 ? EnumChatFormatting.YELLOW : (kills < 15 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
            finalsColor = finalKills < 4 ? EnumChatFormatting.GREEN : (finalKills < 6 ? EnumChatFormatting.YELLOW : (finalKills < 8 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
            expColor = xp < 300 ? EnumChatFormatting.GREEN : (xp < 500 ? EnumChatFormatting.YELLOW : (xp < 750 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
        } else if (cfg.v.bedwarsCounterDynamicColor) {
            killsColor = kills < 50 ? EnumChatFormatting.GREEN : (kills < 100 ? EnumChatFormatting.YELLOW : (kills < 200 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
            finalsColor = finalKills < 30 ? EnumChatFormatting.GREEN : (finalKills < 60 ? EnumChatFormatting.YELLOW : (finalKills < 100 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
            expColor = xp < 2000 ? EnumChatFormatting.GREEN : (xp < 4000 ? EnumChatFormatting.YELLOW : (xp < 6000 ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
        }
        if (cfg.v.bedwarsCounterMode.equals("Vertical")) {
            ArrayList<String> counterStrings = new ArrayList<String>();
            if (cfg.v.bedwarsCounterKill) {
                counterStrings.add("Kills: " + (cfg.v.bedwarsCounterDynamicColor ? killsColor : "") + kills);
            }
            if (cfg.v.bedwarsCounterFinal) {
                counterStrings.add("Finals: " + (cfg.v.bedwarsCounterDynamicColor ? finalsColor : "") + finalKills);
            }
            if (cfg.v.bedwarsCounterExp) {
                counterStrings.add("EXP: " + (cfg.v.bedwarsCounterDynamicColor ? expColor : "") + xp);
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
            String killString = cfg.v.bedwarsCounterKill ? "Kills: " + (cfg.v.bedwarsCounterDynamicColor ? killsColor : "") + kills + (cfg.v.bedwarsCounterFinal || cfg.v.bedwarsCounterExp ? separator : "") + EnumChatFormatting.RESET : "";
            String finalString = cfg.v.bedwarsCounterFinal ? "Finals: " + (cfg.v.bedwarsCounterDynamicColor ? finalsColor : "") + finalKills + (cfg.v.bedwarsCounterExp ? separator : "") + EnumChatFormatting.RESET : "";
            String expString = cfg.v.bedwarsCounterExp ? "EXP: " + (cfg.v.bedwarsCounterDynamicColor ? expColor : "") + xp + EnumChatFormatting.RESET : "";
            String text = killString + finalString + expString;
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
        if (cfg.v.bedwarsCounterReset) {
            kills = 0;
            finalKills = 0;
            xp = 0;
        }
    }
}
