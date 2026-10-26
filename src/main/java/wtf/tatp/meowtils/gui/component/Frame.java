package wtf.tatp.meowtils.gui.component;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.component.Component;
import wtf.tatp.meowtils.gui.parts.ModulesPart;
import wtf.tatp.meowtils.util.Wrapper;

public class Frame {
    public ArrayList<Component> components = new ArrayList();
    public Module.Category category;
    public boolean open;
    private int width;
    private int y;
    private int x;
    private int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private boolean wasDragging = false;

    public Frame(Module.Category cat) {
        this.category = cat;
        this.width = 85;
        this.barHeight = 11;
        this.dragX = 0;
        this.loadFromConfig();
        int tY = this.barHeight;
        for (Module mod : Module.getCategoryModules(this.category)) {
            if (mod.getName().equalsIgnoreCase("ClickGUI")) continue;
            this.components.add(new ModulesPart(mod, this, tY));
            tY += 12;
        }
        this.isDragging = false;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
        this.saveToConfig();
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        if (this.wasDragging && !this.isDragging) {
            this.saveToConfig();
        }
        this.wasDragging = this.isDragging;
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x - 1 && x <= this.x + this.width - 19 && y >= this.y - 3 && y <= this.y + this.barHeight + 2;
    }

    public void renderFrame(FontRenderer fontRenderer) {
        Minecraft mc = Minecraft.getMinecraft();
        this.width = 100;
        ResourceLocation category = new ResourceLocation("meowtils", this.open ? "textures/gui/category_expanded.png" : "textures/gui/category_not_expanded.png");
        mc.getTextureManager().bindTexture(category);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture((int)(this.x - 1), (int)(this.y - 3), (float)0.0f, (float)0.0f, (int)90, (int)20, (float)90.0f, (float)20.0f);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GL11.glPushMatrix();
        int centeredX = this.x;
        Color categoryColor = new Color(255, 255, 255);
        Meowtils.fontRenderer.drawStringWithShadow(this.category.name(), centeredX + 15, this.y + 9, categoryColor.getRGB(), 9.0f);
        ResourceLocation arrow = new ResourceLocation("meowtils", this.open ? "textures/gui/arrow_up.png" : "textures/gui/arrow_down.png");
        mc.getTextureManager().bindTexture(arrow);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawModalRectWithCustomSizedTexture((int)(this.x + 70), (int)(this.y + (this.open ? 2 : 3)), (float)0.0f, (float)0.0f, (int)5, (int)5, (float)5.0f, (float)5.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glPopMatrix();
        if (this.open && !this.components.isEmpty()) {
            for (Component component : this.components) {
                GlStateManager.pushMatrix();
                component.render();
                GlStateManager.popMatrix();
            }
        }
        String categoryName = this.category.name().toLowerCase();
        ResourceLocation icon = new ResourceLocation("meowtils", "textures/gui/icons/" + categoryName + ".png");
        try {
            Wrapper.getMinecraft().getResourceManager().getResource(icon);
        }
        catch (IOException e) {
            icon = new ResourceLocation("meowtils", "textures/gui/icons/default.png");
        }
        GL11.glPushAttrib((int)1048575);
        Wrapper.getMinecraft().getTextureManager().bindTexture(icon);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        Gui.drawScaledCustomSizeModalRect((int)(this.x + 3), (int)(this.y + 1), (float)0.0f, (float)0.0f, (int)26, (int)26, (int)9, (int)9, (float)26.0f, (float)26.0f);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glPopAttrib();
    }

    public void refresh() {
        int off = this.barHeight;
        for (Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    private void loadFromConfig() {
        switch (this.category) {
            case Meowtils: {
                this.x = cfg.v.meowtilsCategoryX;
                this.y = cfg.v.meowtilsCategoryY;
                this.open = cfg.v.meowtilsCategoryExpanded;
                break;
            }
            case Hypixel: {
                this.x = cfg.v.hypixelCategoryX;
                this.y = cfg.v.hypixelCategoryY;
                this.open = cfg.v.hypixelCategoryExpanded;
                break;
            }
            case Bedwars: {
                this.x = cfg.v.bedwarsCategoryX;
                this.y = cfg.v.bedwarsCategoryY;
                this.open = cfg.v.bedwarsCategoryExpanded;
                break;
            }
            case Skywars: {
                this.x = cfg.v.skywarsCategoryX;
                this.y = cfg.v.skywarsCategoryY;
                this.open = cfg.v.skywarsCategoryExpanded;
                break;
            }
            case Render: {
                this.x = cfg.v.renderCategoryX;
                this.y = cfg.v.renderCategoryY;
                this.open = cfg.v.renderCategoryExpanded;
                break;
            }
            case Antisnipe: {
                this.x = cfg.v.antisnipeCategoryX;
                this.y = cfg.v.antisnipeCategoryY;
                this.open = cfg.v.antisnipeCategoryExpanded;
                break;
            }
            case Utility: {
                this.x = cfg.v.utilityCategoryX;
                this.y = cfg.v.utilityCategoryY;
                this.open = cfg.v.utilityCategoryExpanded;
                break;
            }
            case Advanced: {
                this.x = cfg.v.advancedCategoryX;
                this.y = cfg.v.advancedCategoryY;
                this.open = cfg.v.advancedCategoryExpanded;
                break;
            }
            default: {
                this.x = 5;
                this.y = 5;
                this.open = false;
            }
        }
    }

    private void saveToConfig() {
        switch (this.category) {
            case Meowtils: {
                cfg.v.meowtilsCategoryX = this.x;
                cfg.v.meowtilsCategoryY = this.y;
                cfg.v.meowtilsCategoryExpanded = this.open;
                break;
            }
            case Hypixel: {
                cfg.v.hypixelCategoryX = this.x;
                cfg.v.hypixelCategoryY = this.y;
                cfg.v.hypixelCategoryExpanded = this.open;
                break;
            }
            case Skywars: {
                cfg.v.skywarsCategoryX = this.x;
                cfg.v.skywarsCategoryY = this.y;
                cfg.v.skywarsCategoryExpanded = this.open;
                break;
            }
            case Bedwars: {
                cfg.v.bedwarsCategoryX = this.x;
                cfg.v.bedwarsCategoryY = this.y;
                cfg.v.bedwarsCategoryExpanded = this.open;
                break;
            }
            case Render: {
                cfg.v.renderCategoryX = this.x;
                cfg.v.renderCategoryY = this.y;
                cfg.v.renderCategoryExpanded = this.open;
                break;
            }
            case Antisnipe: {
                cfg.v.antisnipeCategoryX = this.x;
                cfg.v.antisnipeCategoryY = this.y;
                cfg.v.antisnipeCategoryExpanded = this.open;
                break;
            }
            case Utility: {
                cfg.v.utilityCategoryX = this.x;
                cfg.v.utilityCategoryY = this.y;
                cfg.v.utilityCategoryExpanded = this.open;
                break;
            }
            case Advanced: {
                cfg.v.advancedCategoryX = this.x;
                cfg.v.advancedCategoryY = this.y;
                cfg.v.advancedCategoryExpanded = this.open;
            }
        }
        cfg.save();
    }
}
