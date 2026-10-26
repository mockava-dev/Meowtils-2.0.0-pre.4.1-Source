package wtf.tatp.meowtils.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.parts.ModulesPart;
import wtf.tatp.meowtils.gui.values.SaturationValue;

public class SaturationPart
extends Component {
    private boolean hovered;
    private boolean dragging = false;
    private final SaturationValue value;
    private final ModulesPart parent;
    private int offset;
    private int x;
    private int y;

    public SaturationPart(SaturationValue value, ModulesPart modulesPart, int offset) {
        this.value = value;
        this.parent = modulesPart;
        this.offset = offset;
        this.x = modulesPart.parent.getX();
        this.y = modulesPart.parent.getY() + offset;
    }

    @Override
    public void render() {
        Minecraft mc = Minecraft.getMinecraft();
        int width = this.parent.parent.getWidth() - 18;
        int baseX = this.parent.parent.getX() - 1;
        int baseY = this.parent.parent.getY() + 4 + this.offset;
        boolean above = this.parent.isComponentAbove(this);
        boolean below = this.parent.isComponentBelow(this);
        boolean last = !this.parent.isModuleBelow() && !this.parent.isComponentBelow(this);
        int sliderX = this.parent.parent.getX() + 3;
        int sliderY = this.parent.parent.getY() + this.offset + 6;
        int sliderWidth = width - 7;
        int sliderHeight = 6;
        float percent = (float)((this.value.get() - 0.0) / 100.0);
        float circleX = (float)sliderX + percent * (float)sliderWidth;
        float circleY = (float)sliderY + (float)sliderHeight / 2.0f;
        int buttonScale = 8;
        float renderX = circleX - (float)buttonScale / 2.0f;
        float renderY = circleY - (float)buttonScale / 2.0f;
        String location = above & below ? "textures/gui/sub_component_background.png" : (above & this.parent.isModuleBelow() ? "textures/gui/sub_component_background_bottom.png" : (above & last ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", location));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((int)baseX, (int)baseY, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/colorpart/blank_track.png"));
        int rgb = this.value.getLink().getPureHueRGB();
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GlStateManager.color((float)((float)r / 255.0f), (float)((float)g / 255.0f), (float)((float)b / 255.0f), (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture((int)baseX, (int)baseY, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/colorpart/saturation_fade.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawModalRectWithCustomSizedTexture((int)baseX, (int)baseY, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/colorpart/track_overlay.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawModalRectWithCustomSizedTexture((int)baseX, (int)baseY, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/colorpart/color_button.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawScaledCustomSizeModalRect((int)((int)renderX), (int)((int)renderY + 3), (float)0.0f, (float)0.0f, (int)64, (int)64, (int)buttonScale, (int)buttonScale, (float)64.0f, (float)64.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
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
        int sliderX = this.parent.parent.getX() + 3;
        int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
        if (this.dragging && Mouse.isButtonDown((int)0)) {
            float percent = (float)(mouseX - sliderX) / (float)sliderWidth;
            percent = Math.max(0.0f, Math.min(1.0f, percent));
            double range = 100.0;
            this.value.set(0.0 + (double)percent * range);
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
            int sliderX = this.parent.parent.getX() + 3;
            int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
            float percent = (float)(mouseX - sliderX) / (float)sliderWidth;
            percent = Math.max(0.0f, Math.min(1.0f, percent));
            double range = 100.0;
            this.value.set(0.0 + (double)percent * range);
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
