package wtf.tatp.meowtils.gui.parts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.GuiScale;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.component.Frame;
import wtf.tatp.meowtils.gui.parts.BrightnessPart;
import wtf.tatp.meowtils.gui.parts.ColorPart;
import wtf.tatp.meowtils.gui.parts.ModePart;
import wtf.tatp.meowtils.gui.parts.OpacityPart;
import wtf.tatp.meowtils.gui.parts.SaturationPart;
import wtf.tatp.meowtils.gui.parts.SliderPart;
import wtf.tatp.meowtils.gui.parts.TogglePart;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.gui.values.OpacityValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.util.ColorUtil;

public class ModulesPart
extends Component {
    public Module mod;
    public Frame parent;
    public int offset;
    private boolean binding;
    private boolean isHovered;
    private ArrayList<Component> subcomponents;
    public boolean open;

    public List<Component> getSubcomponents() {
        return this.subcomponents;
    }

    public boolean isHovered() {
        return this.isHovered;
    }

    public ModulesPart(Module mod, Frame parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList();
        this.open = false;
        int opY = offset + 14;
        LinkedHashSet<ColorComponent> processed = new LinkedHashSet<ColorComponent>();
        for (ColorValue colorValue : mod.getRgb()) {
            processed.add(colorValue.getLink());
        }
        for (SaturationValue satValue : mod.getSaturation()) {
            processed.add(satValue.getLink());
        }
        for (BrightnessValue briValue : mod.getBrightness()) {
            processed.add(briValue.getLink());
        }
        for (OpacityValue opaValue : mod.getOpacity()) {
            processed.add(opaValue.getLink());
        }
        for (ColorComponent comp : processed) {
            for (ColorValue colorValue : mod.getRgb()) {
                if (colorValue.getLink() != comp) continue;
                this.subcomponents.add(new ColorPart(colorValue, this, opY));
                opY += 12;
            }
            for (SaturationValue satValue : mod.getSaturation()) {
                if (satValue.getLink() != comp) continue;
                this.subcomponents.add(new SaturationPart(satValue, this, opY));
                opY += 12;
            }
            for (BrightnessValue briValue : mod.getBrightness()) {
                if (briValue.getLink() != comp) continue;
                this.subcomponents.add(new BrightnessPart(briValue, this, opY));
                opY += 12;
            }
            for (OpacityValue opaValue : mod.getOpacity()) {
                if (opaValue.getLink() != comp) continue;
                this.subcomponents.add(new OpacityPart(opaValue, this, opY));
                opY += 12;
            }
        }
        if (!mod.getArrays().isEmpty()) {
            for (ArrayValue array : mod.getArrays()) {
                this.subcomponents.add(new ModePart(array, this, opY, array.getName()));
                opY += 12;
            }
        }
        if (!mod.getValues().isEmpty()) {
            for (NumberValue num : mod.getValues()) {
                this.subcomponents.add(new SliderPart(num, this, opY));
                opY += 12;
            }
        }
        if (!mod.getBooleans().isEmpty()) {
            for (BooleanValue bool : mod.getBooleans()) {
                this.subcomponents.add(new TogglePart(bool, this, opY));
                opY += 12;
            }
        }
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
        for (Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }

    @Override
    public void render() {
        int textColor;
        int b;
        int g;
        int r;
        Minecraft mc = Minecraft.getMinecraft();
        boolean moduleAbove = this.isModuleAbove();
        boolean moduleBelow = this.isModuleBelow();
        if (this.isHovered && Mouse.isButtonDown((int)2)) {
            this.binding = true;
        }
        String background = moduleAbove & moduleBelow ? "textures/gui/module_disabled.png" : (moduleAbove & !this.open ? "textures/gui/module_disabled_bottom.png" : "textures/gui/module_disabled.png");
        mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", background));
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture((int)(this.parent.getX() - 1), (int)(this.parent.getY() + 1 + this.offset), (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        if (this.mod.getState()) {
            float r2 = (float)cfg.v.meowtils_red / 255.0f;
            float g2 = (float)cfg.v.meowtils_green / 255.0f;
            float b2 = (float)cfg.v.meowtils_blue / 255.0f;
            boolean enabledAbove = this.isAboveEnabled();
            boolean enabledBelow = this.isBelowEnabled();
            String module = enabledAbove && enabledBelow ? "textures/gui/module_connected_both.png" : (enabledAbove ? "textures/gui/module_connected_top.png" : (enabledBelow ? "textures/gui/module_connected_bottom.png" : "textures/gui/module_not_connected.png"));
            mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", module));
            GlStateManager.color((float)r2, (float)g2, (float)b2, (float)1.0f);
            Gui.drawModalRectWithCustomSizedTexture((int)(this.parent.getX() - 1), (int)(this.parent.getY() + 1 + this.offset), (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            if (GuiScale.customGuiScale() < 1.0f && enabledBelow) {
                mc.getTextureManager().bindTexture(new ResourceLocation("meowtils", "textures/gui/custom_scale_line.png"));
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
                GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
                Gui.drawModalRectWithCustomSizedTexture((int)(this.parent.getX() - 1), (int)(this.parent.getY() + 1 + this.offset), (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
                GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
                GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
                GlStateManager.disableBlend();
                GlStateManager.disableAlpha();
            }
        }
        if (ColorUtil.isColorTooBright(r = cfg.v.meowtils_red, g = cfg.v.meowtils_green, b = cfg.v.meowtils_blue) && this.mod.getState()) {
            textColor = new Color(40, 40, 40).getRGB();
            Meowtils.fontRenderer.drawStringWithLightShadow(this.binding ? "" : this.mod.getName(), this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37, this.parent.getY() + (this.offset + 3) + 7, textColor, 6.0f);
            Meowtils.fontRenderer.drawStringWithLightShadow(this.binding ? "Bound.. " + Keyboard.getKeyName((int)this.mod.getKey()) : "", this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37, this.parent.getY() + (this.offset + 3) + 7, textColor, 6.0f);
        } else {
            textColor = new Color(255, 255, 255).getRGB();
            Meowtils.fontRenderer.drawStringWithShadow(this.binding ? "" : this.mod.getName(), this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37, this.parent.getY() + (this.offset + 3) + 7, textColor, 6.0f);
            Meowtils.fontRenderer.drawStringWithShadow(this.binding ? "Bound.. " + Keyboard.getKeyName((int)this.mod.getKey()) : "", this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37, this.parent.getY() + (this.offset + 3) + 7, textColor, 6.0f);
        }
        if (!this.subcomponents.isEmpty()) {
            float color = this.mod.getState() ? 1.0f : 0.5882353f;
            int arrowX = this.parent.getX() + 70;
            int arrowY = this.parent.getY() + this.offset + 6;
            ResourceLocation tex = this.open ? new ResourceLocation("meowtils", "textures/gui/module_arrow_down.png") : new ResourceLocation("meowtils", "textures/gui/module_arrow_up.png");
            mc.getTextureManager().bindTexture(tex);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.color((float)color, (float)color, (float)color, (float)1.0f);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            Gui.drawModalRectWithCustomSizedTexture((int)arrowX, (int)arrowY, (float)0.0f, (float)0.0f, (int)5, (int)5, (float)5.0f, (float)5.0f);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.render();
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return this.subcomponents.isEmpty() ? 28 : 15 + this.subcomponents.size() * 12;
        }
        return 15;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.parent.refresh();
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 2 && this.parent.open) {
            boolean bl = this.binding = !this.binding;
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.setState(!this.mod.getState());
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1 && !this.subcomponents.isEmpty()) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() - 18 && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + this.offset;
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.binding) {
            if (key == 14) {
                this.mod.setKey(0);
                this.binding = false;
                return;
            }
            this.mod.setKey(key);
            this.binding = false;
            if (key == 42) {
                this.mod.setKey(0);
                this.binding = false;
            }
        }
    }

    private ModulesPart getConnected(int direction) {
        Component neighbor;
        int index = this.parent.getComponents().indexOf(this);
        int connectedIndex = index + direction;
        if (connectedIndex >= 0 && connectedIndex < this.parent.getComponents().size() && (neighbor = this.parent.getComponents().get(connectedIndex)) instanceof ModulesPart) {
            return (ModulesPart)neighbor;
        }
        return null;
    }

    private boolean isAboveEnabled() {
        ModulesPart above = this.getConnected(-1);
        return above != null && above.mod.getState();
    }

    private boolean isBelowEnabled() {
        ModulesPart below = this.getConnected(1);
        return below != null && below.mod.getState();
    }

    public boolean isModuleAbove() {
        ModulesPart above = this.getConnected(-1);
        return above != null;
    }

    public boolean isModuleBelow() {
        ModulesPart below = this.getConnected(1);
        return below != null;
    }

    private Component getConnectedComponent(Component current, int direction) {
        int index = this.subcomponents.indexOf(current);
        int neighborIndex = index + direction;
        if (neighborIndex >= 0 && neighborIndex < this.subcomponents.size()) {
            return this.subcomponents.get(neighborIndex);
        }
        return null;
    }

    public boolean isComponentAbove(Component comp) {
        return this.getConnectedComponent(comp, -1) != null;
    }

    public boolean isComponentBelow(Component comp) {
        return this.getConnectedComponent(comp, 1) != null;
    }
}
