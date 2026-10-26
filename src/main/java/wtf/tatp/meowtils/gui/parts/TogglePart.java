package wtf.tatp.meowtils.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.parts.ModulesPart;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class TogglePart
extends Component {
    private boolean hovered;
    private BooleanValue op;
    private ModulesPart parent;
    private int offset;
    private int x;
    private int y;

    public TogglePart(BooleanValue option, ModulesPart modulesPart, int offset) {
        this.op = option;
        this.parent = modulesPart;
        this.x = modulesPart.parent.getX() + modulesPart.parent.getWidth();
        this.y = modulesPart.parent.getY() + modulesPart.offset;
        this.offset = offset;
    }

    @Override
    public void render() {
        Minecraft mc = Minecraft.getMinecraft();
        boolean above = this.parent.isComponentAbove(this);
        boolean below = this.parent.isComponentBelow(this);
        boolean last = !this.parent.isModuleBelow() && !this.parent.isComponentBelow(this);
        int x = this.parent.parent.getX() - 1;
        int y = this.parent.parent.getY() + 4 + this.offset;
        int boolean_x = this.parent.parent.getX() + 68;
        int boolean_y = this.parent.parent.getY() + 4 + this.offset;
        String location = above & below ? "textures/gui/sub_component_background.png" : (above & this.parent.isModuleBelow() ? "textures/gui/sub_component_background_bottom.png" : (above & last ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", location));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/boolean_disabled.png"));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawScaledCustomSizeModalRect((int)boolean_x, (int)boolean_y, (float)0.0f, (float)0.0f, (int)128, (int)128, (int)12, (int)12, (float)128.0f, (float)128.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        if (this.op.getState()) {
            mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/boolean_enabled.png"));
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            float r = (float)cfg.v.meowtils_red / 255.0f;
            float g = (float)cfg.v.meowtils_green / 255.0f;
            float b = (float)cfg.v.meowtils_blue / 255.0f;
            GlStateManager.color((float)r, (float)g, (float)b);
            Gui.drawScaledCustomSizeModalRect((int)boolean_x, (int)boolean_y, (float)0.0f, (float)0.0f, (int)128, (int)128, (int)12, (int)12, (float)128.0f, (float)128.0f);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/boolean_enabled_button.png"));
            Gui.drawScaledCustomSizeModalRect((int)boolean_x, (int)boolean_y, (float)0.0f, (float)0.0f, (int)128, (int)128, (int)12, (int)12, (float)128.0f, (float)128.0f);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
        }
        Meowtils.fontRenderer.drawStringWithShadow(this.op.getName(), this.parent.parent.getX() + 2, this.parent.parent.getY() + 12 + this.offset, -1, 5.0f);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX + 2, mouseY - 3) && button == 0 && this.parent.open) {
            this.op.toggle();
        }
        return false;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 84 && y > this.y && y < this.y + 13;
    }
}
