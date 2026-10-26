package wtf.tatp.meowtils.util;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.mixins.MinecraftAccessor;

public class Render {
    public static void drawSlotBackground(int x, int y, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)x, (double)(y + 16), 0.0).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        worldrenderer.pos((double)(x + 16), (double)(y + 16), 0.0).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        worldrenderer.pos((double)(x + 16), (double)y, 0.0).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }

    public static void draw3DBlockBox(AxisAlignedBB box, boolean fill, Color fillColor, boolean outline, Color outlineColor, double expandX, double expandY, double expandZ) {
        float a;
        float b;
        float g;
        float r;
        Minecraft mc = Minecraft.getMinecraft();
        double dx = mc.getRenderManager().viewerPosX;
        double dy = mc.getRenderManager().viewerPosY;
        double dz = mc.getRenderManager().viewerPosZ;
        AxisAlignedBB bb = box.expand(expandX, expandY, expandZ).offset(-dx, -dy, -dz);
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        if (fill) {
            r = (float)fillColor.getRed() / 255.0f;
            g = (float)fillColor.getGreen() / 255.0f;
            b = (float)fillColor.getBlue() / 255.0f;
            a = (float)fillColor.getAlpha() / 255.0f;
            Render.drawFilledBox(bb, r, g, b, a);
        }
        if (outline) {
            r = (float)outlineColor.getRed() / 255.0f;
            g = (float)outlineColor.getGreen() / 255.0f;
            b = (float)outlineColor.getBlue() / 255.0f;
            a = (float)outlineColor.getAlpha() / 255.0f;
            GlStateManager.color((float)r, (float)g, (float)b, (float)a);
            RenderGlobal.drawSelectionBoundingBox((AxisAlignedBB)bb);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static AxisAlignedBB getAdjustedBox(AxisAlignedBB original, Block block) {
        if (block instanceof BlockChest) {
            return original.expand(-0.05, -0.05, -0.05);
        }
        if (block instanceof BlockBed) {
            return original.expand(0.0, -0.4, 0.0);
        }
        if (block instanceof BlockSlab) {
            return original.expand(0.0, -0.25, 0.0);
        }
        return original;
    }

    public static void draw3DEntityBox(Entity entity, boolean fill, Color fillColor, boolean outline, Color outlineColor, double expandX, double expandY, double expandZ) {
        float a;
        float b;
        float g;
        float r;
        Minecraft mc = Minecraft.getMinecraft();
        float partialTicks = ((MinecraftAccessor)mc).getTimer().renderPartialTicks;
        double dx = mc.getRenderManager().viewerPosX;
        double dy = mc.getRenderManager().viewerPosY;
        double dz = mc.getRenderManager().viewerPosZ;
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        AxisAlignedBB bb = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x - dx, y - dy, z - dz).expand(expandX, expandY, expandZ);
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.disableLighting();
        if (fill) {
            r = (float)fillColor.getRed() / 255.0f;
            g = (float)fillColor.getGreen() / 255.0f;
            b = (float)fillColor.getBlue() / 255.0f;
            a = (float)fillColor.getAlpha() / 255.0f;
            Render.drawFilledBox(bb, r, g, b, a);
        }
        if (outline) {
            r = (float)outlineColor.getRed() / 255.0f;
            g = (float)outlineColor.getGreen() / 255.0f;
            b = (float)outlineColor.getBlue() / 255.0f;
            a = (float)outlineColor.getAlpha() / 255.0f;
            GlStateManager.color((float)r, (float)g, (float)b, (float)a);
            GL11.glLineWidth((float)1.5f);
            Render.drawOutlined3DEntityBox(bb);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void drawOutlined3DEntityBox(AxisAlignedBB bb) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(1, DefaultVertexFormats.POSITION);
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        tess.draw();
    }

    public static void draw2DEntityBox(double x, double y, double z, float width, float height, Color color, boolean fill) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)x, (double)y, (double)z);
        GlStateManager.rotate((float)(-mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)mc.getRenderManager().playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.disableDepth();
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        float a = (float)color.getAlpha() / 255.0f;
        if (fill) {
            GL11.glColor4f((float)r, (float)g, (float)b, (float)(a * 0.5f));
            GL11.glBegin((int)7);
            GL11.glVertex3d((double)(-width), (double)height, (double)0.0);
            GL11.glVertex3d((double)width, (double)height, (double)0.0);
            GL11.glVertex3d((double)width, (double)(-height), (double)0.0);
            GL11.glVertex3d((double)(-width), (double)(-height), (double)0.0);
            GL11.glEnd();
        }
        GL11.glColor4f((float)r, (float)g, (float)b, (float)1.0f);
        GL11.glLineWidth((float)1.5f);
        GL11.glBegin((int)2);
        GL11.glVertex3d((double)(-width), (double)height, (double)0.0);
        GL11.glVertex3d((double)width, (double)height, (double)0.0);
        GL11.glVertex3d((double)width, (double)(-height), (double)0.0);
        GL11.glVertex3d((double)(-width), (double)(-height), (double)0.0);
        GL11.glEnd();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void drawFilledBox(AxisAlignedBB bb, float r, float g, float b, float a) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        tess.draw();
    }

    public static void drawRectFloat(float x1, float y1, float x2, float y2, int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer buffer = tessellator.getWorldRenderer();
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        buffer.begin(7, DefaultVertexFormats.POSITION);
        buffer.pos((double)x1, (double)y2, 0.0).endVertex();
        buffer.pos((double)x2, (double)y2, 0.0).endVertex();
        buffer.pos((double)x2, (double)y1, 0.0).endVertex();
        buffer.pos((double)x1, (double)y1, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
