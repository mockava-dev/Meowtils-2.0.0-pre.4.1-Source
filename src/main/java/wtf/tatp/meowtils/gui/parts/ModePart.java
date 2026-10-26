package wtf.tatp.meowtils.gui.parts;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.gui.GuiScale;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.parts.ModulesPart;
import wtf.tatp.meowtils.gui.values.ArrayValue;

public class ModePart
extends Component {
    public final ArrayValue value;
    public final ModulesPart parent;
    private final String name;
    public int offset;
    private int x;
    private int y;
    private boolean hovered;
    public boolean expanded;
    public static ModePart expandedPart = null;

    public boolean modeExpanded() {
        return this.expanded;
    }

    public ModePart(ArrayValue value, ModulesPart parent, int offset, String name) {
        this.value = value;
        this.parent = parent;
        this.offset = offset;
        this.name = name;
        this.x = parent.parent.getX();
        this.y = parent.parent.getY() + offset;
    }

    @Override
    public void render() {
        Minecraft mc = Minecraft.getMinecraft();
        boolean above = this.parent.isComponentAbove(this);
        boolean below = this.parent.isComponentBelow(this);
        boolean last = !this.parent.isModuleBelow() && !this.parent.isComponentBelow(this);
        int x = this.parent.parent.getX() - 1;
        int y = this.parent.parent.getY() + 4 + this.offset;
        String location = above && below ? "textures/gui/sub_component_background.png" : (above && this.parent.isModuleBelow() ? "textures/gui/sub_component_background_bottom.png" : (above && last ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", location));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        String texture = this.expanded && this.value.getModes().size() >= 2 ? "textures/gui/modepart/mode_top.png" : "textures/gui/modepart/mode_not_expanded.png";
        this.renderBox(x, y, this.name + " - " + this.value.getValue(), texture);
        String arrow = this.expanded ? "textures/gui/modepart/mode_arrow_down.png" : "textures/gui/modepart/mode_arrow_up.png";
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", arrow));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawModalRectWithCustomSizedTexture((int)(x + 72), (int)(y + 4), (float)0.0f, (float)0.0f, (int)5, (int)5, (float)5.0f, (float)5.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public void renderExpanded() {
        if (!this.expanded) {
            return;
        }
        List<String> modes = this.value.getModes();
        String current = this.value.getValue();
        ArrayList<String> displayModes = new ArrayList<String>();
        for (String m : modes) {
            if (m.equals(current)) continue;
            displayModes.add(m);
        }
        for (int i = 0; i < displayModes.size(); ++i) {
            String mode = (String)displayModes.get(i);
            String modePart = displayModes.size() == 1 ? "textures/gui/modepart/mode_bottom.png" : (i == displayModes.size() - 1 ? "textures/gui/modepart/mode_bottom.png" : "textures/gui/modepart/mode_middle.png");
            int scaleOffset = GuiScale.customGuiScale() == 1.0f ? 10 : 9;
            int boxY = this.parent.parent.getY() + 4 + this.offset + (i + 1) * scaleOffset;
            this.renderBox(this.parent.parent.getX() - 1, boxY, mode, modePart);
        }
    }

    private void renderBox(int x, int y, String text, String texture) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", texture));
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        Meowtils.fontRenderer.drawString(text, x + 5, y + 8, -1, 5.0f);
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnCollapsed(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        boolean clickHandled;
        block8: {
            int boxHeight;
            int boxWidth;
            int topY;
            int boxX;
            block7: {
                if (button != 0 || !this.parent.open) {
                    return false;
                }
                boxX = this.parent.parent.getX();
                topY = this.parent.parent.getY() + 4 + this.offset;
                boxWidth = 80;
                boxHeight = 11;
                clickHandled = false;
                if (this.expanded) break block7;
                if (mouseX <= boxX || mouseX >= boxX + boxWidth || mouseY <= topY || mouseY >= topY + boxHeight) break block8;
                this.expanded = true;
                expandedPart = this;
                clickHandled = true;
                break block8;
            }
            if (mouseX > boxX && mouseX < boxX + boxWidth && mouseY > topY && mouseY < topY + boxHeight) {
                this.expanded = false;
                if (expandedPart == this) {
                    expandedPart = null;
                }
                clickHandled = true;
            }
            List<String> modes = this.value.getModes();
            String current = this.value.getValue();
            ArrayList<String> displayModes = new ArrayList<String>();
            for (String m : modes) {
                if (m.equals(current)) continue;
                displayModes.add(m);
            }
            for (int i = 0; i < displayModes.size(); ++i) {
                int boxY = topY + (i + 1) * 10;
                if (mouseX <= boxX || mouseX >= boxX + boxWidth || mouseY <= boxY || mouseY >= boxY + boxHeight) continue;
                this.value.setValue((String)displayModes.get(i));
                this.expanded = false;
                if (expandedPart == this) {
                    expandedPart = null;
                }
                clickHandled = true;
                break;
            }
        }
        if (clickHandled) {
            return clickHandled;
        }
        return clickHandled;
    }

    private boolean isMouseOnCollapsed(int x, int y) {
        int boxX = this.parent.parent.getX() - 1;
        int boxY = this.parent.parent.getY() + 4 + this.offset;
        return x > boxX && x < boxX + 80 && y > boxY && y < boxY + 14;
    }
}
