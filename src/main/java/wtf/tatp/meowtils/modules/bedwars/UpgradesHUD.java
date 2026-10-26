package wtf.tatp.meowtils.modules.bedwars;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
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
import wtf.tatp.meowtils.util.GamemodeUtil;

public class UpgradesHUD
extends Module {
    private NumberValue scale;
    private BooleanValue showSharpness;
    private BooleanValue showProtection;
    private BooleanValue showTrap;
    private BooleanValue showFeatherFalling;
    private BooleanValue showIronForge;
    private BooleanValue showHealPool;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private static int sharpnessLevel = 0;
    private static int sharpnessLevelCached = 0;
    private static int protectionLevel = 0;
    private static int protectionLevelCached = 0;
    private static String trapName = null;
    private static String trapNameCached = null;
    private static int featherFallingLevel = 0;
    private static int featherFallingLevelCached = 0;
    private static boolean healPoolEnabled = false;
    private static boolean healPoolEnabledCached = false;
    private static int ironForgeLevel = 0;
    private static int ironForgeLevelCached = 0;
    public static final String modulename = "UpgradesHUD";

    public UpgradesHUD() {
        super(modulename, "upgradesHUDKey", "upgradesHUD", Module.Category.Bedwars);
        this.tooltip("Displays team upgrades on your screen. Only works with \"English\" language selected on Hypixel.");
        this.color = new ColorComponent("upgradesHUD_red", "upgradesHUD_green", "upgradesHUD_blue");
        this.rgb = new ColorValue("Text color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "upgradesHUDScale", Float.TYPE);
        this.addValue(this.scale);
        this.showSharpness = new BooleanValue("Show \u00a7bSharpness", "upgradesHUD_sharpness");
        this.addBoolean(this.showSharpness);
        this.showProtection = new BooleanValue("Show \u00a73Protection", "upgradesHUD_protection");
        this.addBoolean(this.showProtection);
        this.showTrap = new BooleanValue("Show \u00a79Traps", "upgradesHUD_trap");
        this.addBoolean(this.showTrap);
        this.showFeatherFalling = new BooleanValue("Show \u00a7aFeather Falling", "upgradesHUD_featherfalling");
        this.addBoolean(this.showFeatherFalling);
        this.showHealPool = new BooleanValue("Show \u00a7dHeal Pool", "upgradesHUD_healpool");
        this.addBoolean(this.showHealPool);
        this.showIronForge = new BooleanValue("Show \u00a77Iron Forge", "upgradesHUD_ironforge");
        this.addBoolean(this.showIronForge);
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        int x = cfg.v.upgradesHUD_x;
        int y = cfg.v.upgradesHUD_y;
        float scale = cfg.v.upgradesHUDScale;
        String upgradeFalse = EnumChatFormatting.RED + "\u2717";
        LinkedHashMap<String, String> upgrades = new LinkedHashMap<String, String>();
        if (cfg.v.upgradesHUD_sharpness) {
            upgrades.put("Sharpness:", sharpnessLevel > 0 ? EnumChatFormatting.GREEN + String.valueOf(sharpnessLevel) : upgradeFalse);
        }
        if (cfg.v.upgradesHUD_protection) {
            upgrades.put("Protection:", protectionLevel > 0 ? EnumChatFormatting.GREEN + String.valueOf(protectionLevel) : upgradeFalse);
        }
        if (cfg.v.upgradesHUD_trap) {
            upgrades.put("Trap:", trapName != null ? EnumChatFormatting.GREEN + trapName : upgradeFalse);
        }
        if (cfg.v.upgradesHUD_featherfalling) {
            upgrades.put("Feather Falling:", featherFallingLevel > 0 ? EnumChatFormatting.GREEN + String.valueOf(featherFallingLevel) : upgradeFalse);
        }
        if (cfg.v.upgradesHUD_healpool) {
            upgrades.put("Heal Pool:", healPoolEnabled ? EnumChatFormatting.GREEN + "\u2713" : upgradeFalse);
        }
        if (cfg.v.upgradesHUD_ironforge) {
            upgrades.put("Iron Forge:", ironForgeLevel > 0 ? EnumChatFormatting.GREEN + String.valueOf(ironForgeLevel) : upgradeFalse);
        }
        List<String> lines = upgrades.entrySet().stream().map(entry -> entry.getKey() + " " + entry.getValue()).sorted(Comparator.comparingInt((String s) -> this.mc.fontRendererObj.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s))).reversed()).collect(Collectors.toList());
        for (String line : lines) {
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                this.mc.fontRendererObj.drawStringWithShadow(line, (float)x / scale, (float)y / scale, -1);
                GlStateManager.popMatrix();
                y += (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * scale + 3.0f);
                continue;
            }
            Meowtils.fontRenderer.drawScaledStringWithShadow(line, x, y, -1, scale * 10.0f);
            y += (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * scale + 2.0f);
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (msg.equals("You will respawn because you still have a bed!")) {
            sharpnessLevel = sharpnessLevelCached;
            protectionLevel = protectionLevelCached;
            trapName = trapNameCached;
            featherFallingLevel = featherFallingLevelCached;
            healPoolEnabled = healPoolEnabledCached;
            ironForgeLevel = ironForgeLevelCached;
        }
        if (msg.contains("purchased") && !msg.contains(":")) {
            if (msg.contains("Sharpened Swords")) {
                if (msg.contains("II")) {
                    sharpnessLevel = 2;
                    sharpnessLevelCached = 2;
                } else {
                    sharpnessLevel = 1;
                    sharpnessLevelCached = 1;
                }
            }
            if (msg.contains("Reinforced Armor")) {
                if (msg.contains("IV")) {
                    protectionLevel = 4;
                    protectionLevelCached = 4;
                } else if (msg.contains("III")) {
                    protectionLevel = 3;
                    protectionLevelCached = 3;
                } else if (msg.contains("II")) {
                    protectionLevel = 2;
                    protectionLevelCached = 2;
                } else if (msg.contains("I")) {
                    protectionLevel = 1;
                    protectionLevelCached = 1;
                }
            }
            if (msg.contains("Trap")) {
                if (msg.contains("Miner Fatigue")) {
                    trapName = "Miner Fatigue";
                    trapNameCached = "Miner Fatigue";
                } else if (msg.contains("Blindness")) {
                    trapName = "Blindness";
                    trapNameCached = "Blindness";
                } else if (msg.contains("Reveal")) {
                    trapName = "Reveal";
                    trapNameCached = "Reveal";
                } else if (msg.contains("Counter-Offensive")) {
                    trapName = "Counter-Offensive";
                    trapNameCached = "Counter-Offensive";
                }
            }
            if (msg.contains("Cushioned Boots")) {
                if (msg.contains("II")) {
                    featherFallingLevel = 2;
                    featherFallingLevelCached = 2;
                } else if (msg.contains("I")) {
                    featherFallingLevel = 1;
                    featherFallingLevelCached = 1;
                }
            }
            if (msg.contains("Heal Pool")) {
                healPoolEnabled = true;
                healPoolEnabledCached = true;
            }
            if (msg.contains("Iron Forge")) {
                if (msg.contains("IV")) {
                    ironForgeLevel = 4;
                    ironForgeLevelCached = 4;
                } else if (msg.contains("III")) {
                    ironForgeLevel = 3;
                    ironForgeLevelCached = 3;
                } else if (msg.contains("II")) {
                    ironForgeLevel = 2;
                    ironForgeLevelCached = 2;
                } else if (msg.contains("I")) {
                    ironForgeLevel = 1;
                    ironForgeLevelCached = 1;
                }
            }
        }
        if (msg.contains("Trap was set off!") || msg.contains("Your Bed was destroyed")) {
            trapName = null;
            trapNameCached = null;
        }
    }

    public static void clear() {
        sharpnessLevel = 0;
        protectionLevel = 0;
        trapName = null;
        featherFallingLevel = 0;
        healPoolEnabled = false;
        ironForgeLevel = 0;
    }
}
