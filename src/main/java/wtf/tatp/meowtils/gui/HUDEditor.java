package wtf.tatp.meowtils.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class HUDEditor
extends GuiScreen {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<HudElement> elements = new ArrayList<HudElement>();
    private HudElement dragging = null;
    private int dragOffsetX;
    private int dragOffsetY;

    public HUDEditor() {
        this.elements.add(new HudElement("UpgradesHUD", "Trap: \u00a7aMiner Fatigue\nFeather Falling: \u00a7a1\nSharpness: \u00a7c\u2717\nProtection: \u00a7a4\nIron Forge: \u00a7a2\nHeal Pool: \u00a7a\u2713", cfg.v.upgradesHUD_x, cfg.v.upgradesHUD_y, (int)((float)this.mc.fontRendererObj.getStringWidth("Trap: Miner Fatigue") * cfg.v.upgradesHUDScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.upgradesHUDScale) * 8 + 14, cfg.v.upgradesHUDScale));
        this.elements.add(new HudElement("HealthInfo (Display)", "\u00a7220\u00a7c\u2764", cfg.v.healthInfoDisplay_x, cfg.v.healthInfoDisplay_y, (int)((float)this.mc.fontRendererObj.getStringWidth("20\u2764") * cfg.v.healthInfoDisplayScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.healthInfoDisplayScale), cfg.v.healthInfoDisplayScale));
        this.elements.add(new HudElement("HealthInfo (Indicator)", "14 HP", cfg.v.healthInfoIndicator_x, cfg.v.healthInfoIndicator_y, (int)((float)this.mc.fontRendererObj.getStringWidth("14 HP") * cfg.v.healthInfoIndicatorScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.healthInfoIndicatorScale), cfg.v.healthInfoIndicatorScale));
        this.elements.add(new HudElement("ConsumeTimer", "1.6", cfg.v.consumeTimerXPos, cfg.v.consumeTimerYPos, (int)((float)this.mc.fontRendererObj.getStringWidth("1.6") * cfg.v.consumeTimerScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.consumeTimerScale), cfg.v.consumeTimerScale));
        this.elements.add(new HudElement("CooldownHUD", "\u00a75End Lord: \u00a7aReady", cfg.v.cooldownHUDxPos, cfg.v.cooldownHUDyPos, (int)((float)this.mc.fontRendererObj.getStringWidth("End Lord: Ready") * cfg.v.cooldownHUDScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.cooldownHUDScale), cfg.v.cooldownHUDScale));
        this.elements.add(new HudElement("PotionHUD", "\u00a76Fire Resistance: \u00a771:00\n\u00a77Mining Fatigue: 1:00\n\u00a7dRegeneration: \u00a771:00\n\u00a7aJump Boost: \u00a771:00\n\u00a7bInvisibility: \u00a771:00\n\u00a74Strength: \u00a771:00\n\u00a7eSpeed: \u00a771:00", cfg.v.potionHUD_x, cfg.v.potionHUD_y, (int)((float)this.mc.fontRendererObj.getStringWidth("Fire Resistance: 1:00") * cfg.v.potionHUDScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.potionHUDScale) * 8 + 14, cfg.v.potionHUDScale));
        this.elements.add(new HudElement("TimeWarpDisplay", "\u00a75Time Warp: \u00a7a3.0", cfg.v.timeWarpDisplay_x, cfg.v.timeWarpDisplay_y, (int)((float)this.mc.fontRendererObj.getStringWidth("Time Warp: 3.0") * cfg.v.timeWarpDisplayScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.timeWarpDisplayScale), cfg.v.timeWarpDisplayScale));
        this.elements.add(new HudElement("BlockCount", "64", cfg.v.blockCount_x, cfg.v.blockCount_y, (int)((float)this.mc.fontRendererObj.getStringWidth("64") * cfg.v.blockCountScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.blockCountScale), cfg.v.blockCountScale));
        this.elements.add(new HudElement("BedTracker", "Bed: \u00a7a\u2713 \u00a77 | \u00a7fDistance: \u00a7c150 \u26a0", cfg.v.bedTracker_x, cfg.v.bedTracker_y, (int)((float)this.mc.fontRendererObj.getStringWidth("Bed: \u2713 | Distance: 150 \u26a0") * cfg.v.bedTrackerScale), (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.bedTrackerScale), cfg.v.bedTrackerScale));
        this.elements.add(new HudElement("BedwarsCounter", cfg.v.bedwarsCounterMode.equals("Vertical") ? "Kills: \u00a7a3\nFinals: \u00a7e7\nEXP: \u00a7a150" : "Kills: 0 \u00a77| \u00a7fFinals: 0 \u00a77| \u00a7fEXP: 0", cfg.v.bedwarsCounter_x, cfg.v.bedwarsCounter_y, (int)((float)this.mc.fontRendererObj.getStringWidth(cfg.v.bedwarsCounterMode.equals("Vertical") ? "Finals: 5" : "Kills: 0 | Finals: 0 | EXP: 0") * cfg.v.bedwarsCounterScale), cfg.v.bedwarsCounterMode.equals("Vertical") ? (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.bedwarsCounterScale) * 4 : (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.bedwarsCounterScale), cfg.v.bedwarsCounterScale));
        this.elements.add(new HudElement("SkywarsCounter", cfg.v.skywarsCounterMode.equals("Vertical") ? "Kills: \u00a7a3\nEXP: 15" : "Kills: 0 \u00a77| \u00a7fEXP: 15", cfg.v.skywarsCounter_x, cfg.v.skywarsCounter_y, (int)((float)this.mc.fontRendererObj.getStringWidth(cfg.v.skywarsCounterMode.equals("Vertical") ? "Kills: 5" : "Kills: 0 | EXP: 15") * cfg.v.skywarsCounterScale), cfg.v.skywarsCounterMode.equals("Vertical") ? (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.skywarsCounterScale) * 3 : (int)((float)this.mc.fontRendererObj.FONT_HEIGHT * cfg.v.skywarsCounterScale), cfg.v.skywarsCounterScale));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        HudElement showId;
        this.drawDefaultBackground();
        if (this.dragging != null) {
            this.dragging.x = mouseX - this.dragOffsetX;
            this.dragging.y = mouseY - this.dragOffsetY;
        }
        HudElement hovered = null;
        for (HudElement element : this.elements) {
            element.drawSample();
            if (!element.isMouseOver(mouseX, mouseY)) continue;
            hovered = element;
        }
        if (hovered != null) {
            hovered.drawBox();
        }
        HudElement hudElement = showId = this.dragging != null ? this.dragging : hovered;
        if (showId != null) {
            Meowtils.fontRenderer.drawScaledStringWithShadow(showId.id, mouseX + 6, mouseY + 6, -1, 6.0f);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (HudElement element : this.elements) {
                if (!element.isMouseOver(mouseX, mouseY)) continue;
                this.dragging = element;
                this.dragOffsetX = mouseX - element.x;
                this.dragOffsetY = mouseY - element.y;
                break;
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0 && this.dragging != null) {
            switch (this.dragging.id) {
                case "UpgradesHUD": {
                    cfg.v.upgradesHUD_x = this.dragging.x;
                    cfg.v.upgradesHUD_y = this.dragging.y;
                    break;
                }
                case "HealthInfo (Display)": {
                    cfg.v.healthInfoDisplay_x = this.dragging.x;
                    cfg.v.healthInfoDisplay_y = this.dragging.y;
                    break;
                }
                case "HealthInfo (Indicator)": {
                    cfg.v.healthInfoIndicator_x = this.dragging.x;
                    cfg.v.healthInfoIndicator_y = this.dragging.y;
                    break;
                }
                case "ConsumeTimer": {
                    cfg.v.consumeTimerXPos = this.dragging.x;
                    cfg.v.consumeTimerYPos = this.dragging.y;
                    break;
                }
                case "CooldownHUD": {
                    cfg.v.cooldownHUDxPos = this.dragging.x;
                    cfg.v.cooldownHUDyPos = this.dragging.y;
                    break;
                }
                case "PotionHUD": {
                    cfg.v.potionHUD_x = this.dragging.x;
                    cfg.v.potionHUD_y = this.dragging.y;
                    break;
                }
                case "TimeWarpDisplay": {
                    cfg.v.timeWarpDisplay_x = this.dragging.x;
                    cfg.v.timeWarpDisplay_y = this.dragging.y;
                    break;
                }
                case "BlockCount": {
                    cfg.v.blockCount_x = this.dragging.x;
                    cfg.v.blockCount_y = this.dragging.y;
                    break;
                }
                case "BedTracker": {
                    cfg.v.bedTracker_x = this.dragging.x;
                    cfg.v.bedTracker_y = this.dragging.y;
                    break;
                }
                case "BedwarsCounter": {
                    cfg.v.bedwarsCounter_x = this.dragging.x;
                    cfg.v.bedwarsCounter_y = this.dragging.y;
                    break;
                }
                case "SkywarsCounter": {
                    cfg.v.skywarsCounter_x = this.dragging.x;
                    cfg.v.skywarsCounter_y = this.dragging.y;
                }
            }
            cfg.save();
            this.dragging = null;
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private static class HudElement {
        final String id;
        final String sample;
        final float scale;
        int x;
        int y;
        int w;
        int h;

        HudElement(String id, String sample, int x, int y, int w, int h, float scale) {
            this.id = id;
            this.sample = sample == null ? "" : sample;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.scale = scale <= 0.0f ? 1.0f : scale;
        }

        void drawSample() {
            Minecraft mc = Minecraft.getMinecraft();
            String[] lines = this.sample.split("\n");
            int currentY = this.y;
            for (String line : lines) {
                if (!cfg.v.smoothFont) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale((float)this.scale, (float)this.scale, (float)this.scale);
                    mc.fontRendererObj.drawStringWithShadow(line, (float)this.x / this.scale, (float)currentY / this.scale, -1);
                    GlStateManager.popMatrix();
                    currentY += (int)((float)mc.fontRendererObj.FONT_HEIGHT * this.scale + 3.0f);
                    continue;
                }
                Meowtils.fontRenderer.drawScaledStringWithShadow(line, this.x, currentY, -1, this.scale * 10.0f);
                currentY += (int)((float)mc.fontRendererObj.FONT_HEIGHT * this.scale + 2.0f);
            }
        }

        void drawBox() {
            Gui.drawRect((int)(this.x - 1), (int)(this.y - 1), (int)(this.x + this.w + 1), (int)(this.y + this.h + 1), (int)new Color(0, 0, 0, 90).getRGB());
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.w), (int)(this.y + this.h), (int)new Color(80, 80, 80, 30).getRGB());
        }

        boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
        }
    }
}
