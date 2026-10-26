package wtf.tatp.meowtils.modules.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.PlaySound;

public class PotionHUD
extends Module {
    private NumberValue hudScale;
    private NumberValue time;
    private BooleanValue sound;
    private BooleanValue invis;
    private BooleanValue jump;
    private BooleanValue speed;
    private BooleanValue regen;
    private BooleanValue strength;
    private BooleanValue fireres;
    private BooleanValue miningfatigue;
    private BooleanValue hideInfinite;
    private ArrayValue nameMode;
    private ArrayValue displayMode;
    private final Set<Integer> alertedPotions = new HashSet<Integer>();

    public PotionHUD() {
        super("PotionHUD", "potionHUDKey", "potionHUD", Module.Category.Render);
        this.tooltip("Display important potion effects on screen.");
        this.hudScale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "potionHUDScale", Float.TYPE);
        this.addValue(this.hudScale);
        this.time = new NumberValue("Alert time", 0.0, 10.0, 1.0, "s", "potionHUDSeconds", Integer.TYPE);
        this.addValue(this.time);
        this.sound = new BooleanValue("Expire sound", "potionHUDSound");
        this.addBoolean(this.sound);
        this.hideInfinite = new BooleanValue("Hide infinite effects", "potionHUDHideInfinite");
        this.addBoolean(this.hideInfinite);
        this.invis = new BooleanValue("\u00a7bInvisibility", "potionHUD_invis");
        this.addBoolean(this.invis);
        this.jump = new BooleanValue("\u00a7aJump Boost", "potionHUD_jump");
        this.addBoolean(this.jump);
        this.speed = new BooleanValue("\u00a7eSpeed", "potionHUD_speed");
        this.addBoolean(this.speed);
        this.regen = new BooleanValue("\u00a7dRegeneration", "potionHUD_regen");
        this.addBoolean(this.regen);
        this.strength = new BooleanValue("\u00a74Strength", "potionHUD_strength");
        this.addBoolean(this.strength);
        this.fireres = new BooleanValue("\u00a76Fire Resistance", "potionHUD_fireres");
        this.addBoolean(this.fireres);
        this.miningfatigue = new BooleanValue("\u00a77Mining Fatigue", "potionHUD_miningfatigue");
        this.addBoolean(this.miningfatigue);
        this.nameMode = new ArrayValue("Name", Arrays.asList("Full", "Short", "None"), "potionHUDNameMode");
        this.addArray(this.nameMode);
        this.displayMode = new ArrayValue("Display", Arrays.asList("Both", "HUD", "Chat"), "potionHUDMode");
        this.addArray(this.displayMode);
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (mc.currentScreen != null) {
            return;
        }
        float scale = cfg.v.potionHUDScale;
        ArrayList<String> potionStrings = new ArrayList<String>();
        mc.thePlayer.getActivePotionEffects().forEach(effect -> {
            int id = effect.getPotionID();
            Potion potion = Potion.potionTypes[id];
            if (potion == null) {
                return;
            }
            if (!(id == Potion.invisibility.id && cfg.v.potionHUD_invis || id == Potion.jump.id && cfg.v.potionHUD_jump || id == Potion.moveSpeed.id && cfg.v.potionHUD_speed || id == Potion.regeneration.id && cfg.v.potionHUD_regen || id == Potion.damageBoost.id && cfg.v.potionHUD_strength || id == Potion.fireResistance.id && cfg.v.potionHUD_fireres || id == Potion.digSlowdown.id && cfg.v.potionHUD_miningfatigue)) {
                return;
            }
            String timeLeft = Potion.getDurationString((PotionEffect)effect);
            if (timeLeft.contains("*") && cfg.v.potionHUDHideInfinite) {
                return;
            }
            int seconds = cfg.v.potionHUDSeconds * 20;
            if (effect.getDuration() <= seconds) {
                String string = timeLeft = !cfg.v.potionHUDNameMode.equals("None") ? EnumChatFormatting.RED + timeLeft : timeLeft;
                if (!this.alertedPotions.contains(id) && (cfg.v.potionHUDMode.equals("Chat") || cfg.v.potionHUDMode.equals("Both"))) {
                    String messageSuffix = seconds <= 0 ? " has expired!" : " is about to expire!";
                    Meowtils.addMessage(this.getPotionName(id, "Full").replace(":", "") + EnumChatFormatting.RED + messageSuffix);
                    this.alertedPotions.add(id);
                    if (cfg.v.potionHUDSound) {
                        PlaySound.getInstance().playPingSoundMedium();
                    }
                }
            } else {
                this.alertedPotions.remove(id);
            }
            potionStrings.add(this.getPotionName(id, cfg.v.potionHUDNameMode) + " " + (cfg.v.potionHUDNameMode.equals("None") ? "" : EnumChatFormatting.GRAY) + timeLeft);
        });
        potionStrings.sort(Comparator.comparingInt((String s) -> mc.fontRendererObj.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s))).reversed());
        if (potionStrings.isEmpty() || cfg.v.potionHUDMode.equals("Chat")) {
            return;
        }
        int y = cfg.v.potionHUD_y;
        for (String string : potionStrings) {
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                mc.fontRendererObj.drawStringWithShadow(string, (float)cfg.v.potionHUD_x / scale, (float)y / scale, -1);
                GlStateManager.popMatrix();
                y += (int)((float)mc.fontRendererObj.FONT_HEIGHT * scale + 3.0f);
                continue;
            }
            Meowtils.fontRenderer.drawScaledStringWithShadow(string, cfg.v.potionHUD_x, y, -1, scale * 10.0f);
            y += (int)((float)mc.fontRendererObj.FONT_HEIGHT * scale + 2.0f);
        }
    }

    private String getPotionName(int id, String mode) {
        EnumChatFormatting color;
        String shortName;
        String fullName;
        if (id == Potion.invisibility.id) {
            fullName = "Invisibility";
            shortName = "Inv";
            color = EnumChatFormatting.AQUA;
        } else if (id == Potion.jump.id) {
            fullName = "Jump Boost";
            shortName = "Jmp";
            color = EnumChatFormatting.GREEN;
        } else if (id == Potion.moveSpeed.id) {
            fullName = "Speed";
            shortName = "Spd";
            color = EnumChatFormatting.YELLOW;
        } else if (id == Potion.regeneration.id) {
            fullName = "Regeneration";
            shortName = "Reg";
            color = EnumChatFormatting.LIGHT_PURPLE;
        } else if (id == Potion.damageBoost.id) {
            fullName = "Strength";
            shortName = "Str";
            color = EnumChatFormatting.DARK_RED;
        } else if (id == Potion.fireResistance.id) {
            fullName = "Fire Resistance";
            shortName = "Fire";
            color = EnumChatFormatting.GOLD;
        } else if (id == Potion.digSlowdown.id) {
            fullName = "Mining Fatigue";
            shortName = "Mine";
            color = EnumChatFormatting.GRAY;
        } else {
            return "Unknown";
        }
        switch (mode) {
            case "Full": {
                return color + fullName + ":";
            }
            case "Short": {
                return color + shortName + ":";
            }
            case "None": {
                return color + "";
            }
        }
        return "Unknown";
    }
}
