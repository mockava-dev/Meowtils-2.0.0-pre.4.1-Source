package wtf.tatp.meowtils.gui;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.GuiScale;
import wtf.tatp.meowtils.gui.HUDEditor;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.component.Frame;
import wtf.tatp.meowtils.gui.parts.ModePart;
import wtf.tatp.meowtils.gui.parts.ModulesPart;

public class ClickGUI
extends GuiScreen {
    private String versionTag = "Meowtils 2.0.0-pre.4.1";
    public ArrayList<Frame> frames = new ArrayList();
    private Object lastHoveredModule = null;
    private long hoverStartTime = 0L;

    public ClickGUI() {
        for (Module.Category category : Module.Category.values()) {
            Frame frame = new Frame(category);
            this.frames.add(frame);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        float scale = this.getScale();
        int scaledMouseX = (int)((float)mouseX / scale);
        int scaledMouseY = (int)((float)mouseY / scale);
        ScaledResolution sr = new ScaledResolution(mc);
        float versionTagScale = 0.65f;
        int versionTagWidth = mc.fontRendererObj.getStringWidth(this.versionTag);
        int versionTagHeight = mc.fontRendererObj.FONT_HEIGHT;
        float versionTagX = (float)sr.getScaledWidth() - (float)versionTagWidth * versionTagScale - 2.0f;
        float versionTagY = (float)sr.getScaledHeight() - (float)versionTagHeight * versionTagScale - 2.0f;
        int versionTagColor = new Color(cfg.v.meowtils_red, cfg.v.meowtils_green, cfg.v.meowtils_blue).getRGB();
        float customGuiScale = GuiScale.customGuiScale();
        if (GuiScale.customGuiScale() != 1.0f) {
            GlStateManager.pushMatrix();
            GL11.glTranslatef((float)(0.5f / customGuiScale), (float)(0.5f / customGuiScale), (float)0.0f);
            GlStateManager.popMatrix();
        }
        Meowtils.fontRenderer.drawStringWithShadow(this.versionTag, versionTagX - 2.0f, versionTagY + 4.0f, versionTagColor, 7.0f);
        int screenHeightButton = sr.getScaledHeight();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/hud_button.png"));
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        Gui.drawModalRectWithCustomSizedTexture((int)6, (int)(screenHeightButton - 20), (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        Meowtils.fontRenderer.drawStringWithShadow("HUD Editor", 25.0f, screenHeightButton - 10, -1, 7.0f);
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)scale, (float)scale, (float)1.0f);
        ModulesPart hovered = null;
        ArrayList<ModePart> overlays = new ArrayList<ModePart>();
        for (Frame frame : this.frames) {
            frame.renderFrame(this.fontRendererObj);
            frame.updatePosition(scaledMouseX, scaledMouseY);
            for (Component comp : frame.getComponents()) {
                comp.updateComponent(scaledMouseX, scaledMouseY);
                if (!(comp instanceof ModulesPart)) continue;
                ModulesPart part = (ModulesPart)comp;
                if (part.isHovered()) {
                    hovered = part;
                }
                for (Component sub : part.getSubcomponents()) {
                    ModePart mode;
                    if (!(sub instanceof ModePart) || !(mode = (ModePart)sub).modeExpanded()) continue;
                    overlays.add(mode);
                }
            }
        }
        for (ModePart overlay : overlays) {
            overlay.renderExpanded();
        }
        GlStateManager.popMatrix();
        if (hovered != null && cfg.v.guiTooltips) {
            Frame parentFrame = hovered.parent;
            if (!parentFrame.isOpen()) {
                return;
            }
            if (hovered.mod != this.lastHoveredModule) {
                this.lastHoveredModule = hovered.mod;
                this.hoverStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - this.hoverStartTime < 250L) {
                return;
            }
            String tooltip = hovered.mod.getTooltip();
            if (tooltip != null && !tooltip.isEmpty()) {
                int mouseXScaled = Mouse.getX() * sr.getScaledWidth() / mc.displayWidth;
                int mouseYScaled = sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / mc.displayHeight - 1;
                float guiTooltipscale = this.getScale() / 2.0f;
                int drawX = (int)((float)(mouseXScaled + 6) / guiTooltipscale);
                int drawY = (int)((float)mouseYScaled / guiTooltipscale);
                int screenWidth = (int)((float)sr.getScaledWidth() / guiTooltipscale);
                int screenHeight = (int)((float)sr.getScaledHeight() / guiTooltipscale);
                int availableWidth = screenWidth - drawX - 8;
                ArrayList<String> lines = new ArrayList<String>();
                GL11.glPushMatrix();
                GL11.glScalef((float)guiTooltipscale, (float)guiTooltipscale, (float)1.0f);
                float fontScale = 10.5f;
                for (String manualLine : tooltip.split("\n")) {
                    StringBuilder currentLine = new StringBuilder();
                    for (String word : manualLine.split(" ")) {
                        String testLine;
                        String string = testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                        if (Meowtils.fontRenderer.getStringWidth(testLine, fontScale) > (float)availableWidth) {
                            if (currentLine.length() > 0) {
                                lines.add(currentLine.toString());
                                currentLine = new StringBuilder(word);
                                continue;
                            }
                            lines.add(word);
                            currentLine = new StringBuilder();
                            continue;
                        }
                        currentLine = new StringBuilder(testLine);
                    }
                    if (currentLine.length() <= 0) continue;
                    lines.add(currentLine.toString());
                }
                int lineHeight = mc.fontRendererObj.FONT_HEIGHT + 2;
                int maxWidth = 0;
                for (String line : lines) {
                    maxWidth = (int)Math.max((float)maxWidth, Meowtils.fontRenderer.getStringWidth(line, fontScale));
                }
                int totalHeight = lines.size() * lineHeight;
                if (drawX + maxWidth > screenWidth) {
                    drawX = screenWidth - maxWidth - 8;
                }
                if (drawY + totalHeight > screenHeight) {
                    drawY = screenHeight - totalHeight - 8;
                }
                if (drawX < 0) {
                    drawX = 0;
                }
                if (drawY < 0) {
                    drawY = 0;
                }
                int padding = 3;
                Gui.drawRect((int)(drawX - padding), (int)(drawY - padding), (int)(drawX + maxWidth + padding), (int)(drawY + totalHeight - 2 + padding), (int)new Color(14, 14, 14).getRGB());
                for (int i = 0; i < lines.size(); ++i) {
                    Meowtils.fontRenderer.drawString((String)lines.get(i), drawX, drawY + i * lineHeight + 7, -1, fontScale);
                }
                GL11.glPopMatrix();
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float scale = this.getScale();
        int scaledMouseX = (int)((float)mouseX / scale);
        int scaledMouseY = (int)((float)mouseY / scale);
        this.hudEditorClicked(mouseX, mouseY, mouseButton);
        ArrayList<ModePart> overlays = new ArrayList<ModePart>();
        for (Frame frame : this.frames) {
            for (Component comp : frame.getComponents()) {
                if (!(comp instanceof ModulesPart)) continue;
                ModulesPart part = (ModulesPart)comp;
                for (Component sub : part.getSubcomponents()) {
                    ModePart mode;
                    if (!(sub instanceof ModePart) || !(mode = (ModePart)sub).modeExpanded()) continue;
                    overlays.add(mode);
                }
            }
        }
        for (ModePart overlay : overlays) {
            if (!overlay.mouseClicked(scaledMouseX, scaledMouseY, mouseButton)) continue;
            return;
        }
        if (ModePart.expandedPart != null) {
            int boxHeight;
            boolean clickedInsideAny = false;
            ModePart expanded = ModePart.expandedPart;
            int boxX = expanded.parent.parent.getX();
            int topY = expanded.parent.parent.getY() + 4 + expanded.offset;
            int boxWidth = 80;
            int totalHeight = boxHeight = 11;
            if (expanded.modeExpanded()) {
                int extra = (expanded.value.getModes().size() - 1) * 10;
                totalHeight += extra;
            }
            if (scaledMouseX > boxX && scaledMouseX < boxX + boxWidth && scaledMouseY > topY && scaledMouseY < topY + totalHeight) {
                clickedInsideAny = true;
            }
            if (!clickedInsideAny) {
                expanded.expanded = false;
                ModePart.expandedPart = null;
            }
        }
        for (Frame frame : this.frames) {
            if (frame.isWithinHeader(scaledMouseX, scaledMouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = scaledMouseX - frame.getX();
                frame.dragY = scaledMouseY - frame.getY();
            }
            if (frame.isWithinHeader(scaledMouseX, scaledMouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
            }
        }
    }

    private void hudEditorClicked(int mouseX, int mouseY, int mouseButton) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int screenHeightButton = sr.getScaledHeight();
        int buttonX = 6;
        int buttonY = screenHeightButton - 20;
        int buttonW = 72;
        int buttonH = 13;
        if (mouseButton == 0 && mouseX >= buttonX && mouseX <= buttonX + buttonW && mouseY >= buttonY && mouseY <= buttonY + buttonH) {
            this.mc.displayGuiScreen((GuiScreen)new HUDEditor());
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        for (Frame frame : this.frames) {
            if (!frame.isOpen() || keyCode == 1 || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.keyTyped(typedChar, keyCode);
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Frame frame : this.frames) {
            frame.setDrag(false);
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private float getScale() {
        float effectiveScale = GuiScale.getEffectiveGuiScale();
        float baseScale = 3.0f;
        return baseScale / effectiveScale * GuiScale.customGuiScale();
    }

    public void initGui() {
        super.initGui();
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        try {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
}
