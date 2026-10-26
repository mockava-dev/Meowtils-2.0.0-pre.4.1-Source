package wtf.tatp.meowtils.gui.parts;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.parts.ModulesPart;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.Render;

public class SliderPart
extends Component {
    private boolean hovered;
    private boolean dragging = false;
    private NumberValue value;
    private ModulesPart parent;
    private int offset;
    private int x;
    private int y;

    public SliderPart(NumberValue value, ModulesPart modulesPart, int offset) {
        this.value = value;
        this.parent = modulesPart;
        this.x = modulesPart.parent.getX() + modulesPart.parent.getWidth();
        this.y = modulesPart.parent.getY() + modulesPart.offset;
        this.offset = offset;
    }

    @Override
    public void render() {
        Minecraft mc = Minecraft.getMinecraft();
        int newWidth = this.parent.parent.getWidth() - 18;
        int drag = -2 + (int)((this.value.get() - this.value.getMin()) / (this.value.getMax() - this.value.getMin()) * (double)(newWidth - 8));
        float sliderHeight = 2.5f;
        boolean above = this.parent.isComponentAbove(this);
        boolean below = this.parent.isComponentBelow(this);
        boolean last = !this.parent.isModuleBelow() && !this.parent.isComponentBelow(this);
        int x = this.parent.parent.getX() - 1;
        int y = this.parent.parent.getY() + 4 + this.offset;
        float circleX = this.parent.parent.getX() + 5 + drag;
        float circleY = (float)(this.parent.parent.getY() + this.offset + 11) + sliderHeight / 2.0f;
        String location = above & below ? "textures/gui/sub_component_background.png" : (above & this.parent.isModuleBelow() ? "textures/gui/sub_component_background_bottom.png" : (above & last ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", location));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/sliderpart/slider_track.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/sliderpart/slider_end.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        float r = (float)cfg.v.meowtils_red / 255.0f;
        float g = (float)cfg.v.meowtils_green / 255.0f;
        float b = (float)cfg.v.meowtils_blue / 255.0f;
        GlStateManager.color((float)r, (float)g, (float)b);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        Render.drawRectFloat(this.parent.parent.getX() + 4, (float)(this.parent.parent.getY() + this.offset) + 11.25f, this.parent.parent.getX() + 5 + drag, (float)(this.parent.parent.getY() + this.offset) + 11.5f + sliderHeight, new Color(cfg.v.meowtils_red, cfg.v.meowtils_green, cfg.v.meowtils_blue).getRGB());
        int buttonScale = 5;
        float renderX = circleX - (float)buttonScale / 2.0f;
        float renderY = circleY - (float)buttonScale / 2.0f;
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/sliderpart/slider_button.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawScaledCustomSizeModalRect((int)((int)renderX + 1), (int)((int)renderY + 1), (float)0.0f, (float)0.0f, (int)64, (int)64, (int)buttonScale, (int)buttonScale, (float)64.0f, (float)64.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        int leftX = this.parent.parent.getX() + 2;
        int baseY = this.parent.parent.getY() + this.offset + 9;
        int rightEdge = this.parent.parent.getX() + this.parent.parent.getWidth() - 22;
        String nameText = this.value.getName();
        Meowtils.fontRenderer.drawStringWithShadow(nameText, leftX, baseY, -1, 5.0f);
        String formatted = this.value.getFormattedValue();
        String valueSuffix = this.value.getValueType() != null ? " " + this.value.getValueType() : "";
        String valueText = formatted + valueSuffix;
        int valueWidth = (int)Meowtils.fontRenderer.getStringWidth(valueText, 5.0f);
        int valueX = rightEdge - valueWidth;
        Meowtils.fontRenderer.drawStringWithShadow(valueText, valueX, baseY, -1, 5.0f);
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnSlider(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        int sliderX = this.parent.parent.getX() + 4;
        int sliderWidth = this.parent.parent.getWidth() - 18 - 8;
        if (this.dragging && Mouse.isButtonDown((int)0)) {
            double percent = (double)(mouseX - sliderX) / (double)sliderWidth;
            percent = Math.max(0.0, Math.min(percent, 1.0));
            double range = this.value.getMax() - this.value.getMin();
            this.value.set(this.value.getMin() + percent * range);
        } else if (!Mouse.isButtonDown((int)0)) {
            this.dragging = false;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (button != 0 || !this.parent.open) {
            return false;
        }
        if (this.isMouseOnSlider(mouseX, mouseY)) {
            int sliderX = this.parent.parent.getX() + 4;
            int sliderWidth = this.parent.parent.getWidth() - 18 - 8;
            double percent = (double)(mouseX - sliderX) / (double)sliderWidth;
            percent = Math.max(0.0, Math.min(percent, 1.0));
            double range = this.value.getMax() - this.value.getMin();
            this.value.set(this.value.getMin() + percent * range);
            this.dragging = true;
            return true;
        }
        return false;
    }

    private boolean isMouseOnSlider(int mouseX, int mouseY) {
        int sliderX = this.parent.parent.getX() + 3;
        int sliderY = this.parent.parent.getY() + this.offset + 4;
        int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
        int sliderHeight = 11;
        return mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= sliderY && mouseY <= sliderY + sliderHeight;
    }
}
