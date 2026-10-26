package wtf.tatp.meowtils.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.util.ColorUtil;

public class FontRenderer {
    private final Font font;
    private final int textureSize = 1024;
    private final Map<Character, CharacterInfo> charMap = new HashMap<Character, CharacterInfo>();
    private final ResourceLocation textureLocation;
    private final float originalFontSize;
    private final net.minecraft.client.gui.FontRenderer mcFontRenderer;
    private static final int PADDING = 2;

    public FontRenderer(Font font) {
        this.mcFontRenderer = Minecraft.getMinecraft().fontRendererObj;
        this.font = font;
        this.originalFontSize = font.getSize2D();
        DynamicTexture texture = this.bakeFontAtlas();
        this.textureLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("font_renderer", texture);
    }

    private DynamicTexture bakeFontAtlas() {
        BufferedImage atlas = new BufferedImage(1024, 1024, 2);
        Graphics2D g = atlas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setFont(this.font);
        g.setColor(Color.WHITE);
        FontRenderContext frc = g.getFontRenderContext();
        int x = 2;
        int y = 2;
        int lineHeight = 0;
        for (char c = ' '; c < '\u007f'; c = (char)(c + '\u0001')) {
            GlyphVector gv = this.font.createGlyphVector(frc, String.valueOf(c));
            Rectangle bounds = gv.getPixelBounds(null, 0.0f, 0.0f);
            float advance = gv.getGlyphMetrics(0).getAdvanceX();
            if (c == ' ') {
                this.charMap.put(Character.valueOf(c), new CharacterInfo(0, 0, 0, 0, 0.0f, 0.0f, advance));
                continue;
            }
            if (bounds.width <= 0 || bounds.height <= 0) continue;
            if (x + bounds.width + 2 >= 1024) {
                x = 2;
                y += lineHeight + 2;
                lineHeight = 0;
            }
            g.drawGlyphVector(gv, x - bounds.x, y - bounds.y);
            if (bounds.height > lineHeight) {
                lineHeight = bounds.height;
            }
            this.charMap.put(Character.valueOf(c), new CharacterInfo(x, y, bounds.width, bounds.height, bounds.x, bounds.y, advance));
            x += bounds.width + 2;
        }
        g.dispose();
        return new DynamicTexture(atlas);
    }

    public void drawString(String text, float x, float y, int color, float size) {
        this.drawColoredText(text, x, y, color, size, false);
    }

    public void drawStringWithShadow(String text, float x, float y, int color, float size) {
        if (text == null || text.isEmpty()) {
            return;
        }
        float shadowOffset = 0.4f;
        this.drawColoredText(text, x + shadowOffset, y + shadowOffset, color, size, true);
        this.drawColoredText(text, x, y, color, size, false);
    }

    public void drawStringWithLightShadow(String text, float x, float y, int color, float size) {
        if (text == null || text.isEmpty()) {
            return;
        }
        float shadowOffset = 0.1f;
        this.drawColoredText(text, x + shadowOffset, y + shadowOffset, color, size, true);
        this.drawColoredText(text, x, y, color, size, false);
    }

    public void drawScaledString(String text, float x, float y, int color, float size) {
        this.drawColoredText(text, x, y + 7.5f * size / 10.0f, color, size, false);
    }

    public void drawScaledStringWithShadow(String text, float x, float y, int color, float size) {
        if (text == null || text.isEmpty()) {
            return;
        }
        float shadowOffset = 0.4f;
        this.drawColoredText(text, x + shadowOffset, y + shadowOffset + 7.5f * size / 10.0f, color, size, true);
        this.drawColoredText(text, x, y + 7.5f * size / 10.0f, color, size, false);
    }

    public void drawScaledStringWithLightShadow(String text, float x, float y, int color, float size) {
        if (text == null || text.isEmpty()) {
            return;
        }
        float shadowOffset = 0.1f;
        this.drawColoredText(text, x + shadowOffset, y + shadowOffset + 7.5f * size / 10.0f, color, size, true);
        this.drawColoredText(text, x, y + 7.5f * size / 10.0f, color, size, false);
    }

    public float getStringWidth(String text, float size) {
        if (text == null || text.isEmpty()) {
            return 0.0f;
        }
        float totalWidth = 0.0f;
        float customScale = size / this.originalFontSize;
        float mcScale = size / 10.0f;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '\u00a7' && i + 1 < text.length()) {
                ++i;
                continue;
            }
            CharacterInfo info = this.charMap.get(Character.valueOf(c));
            if (info != null) {
                totalWidth += info.advance;
                continue;
            }
            float mcPixelWidth = (float)this.mcFontRenderer.getCharWidth(c) * mcScale;
            totalWidth += mcPixelWidth / customScale;
        }
        return totalWidth * customScale;
    }

    private void drawColoredText(String text, float x, float y, int baseColor, float size, boolean shadow) {
        if (text == null || text.isEmpty()) {
            return;
        }
        float shadowMultiplier = shadow ? 0.25f : 1.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.enableTexture2D();
        GlStateManager.translate((float)x, (float)y, (float)0.0f);
        float scale = size / this.originalFontSize;
        GlStateManager.scale((float)scale, (float)scale, (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureLocation);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        int currentColor = baseColor;
        float r = (float)(currentColor >> 16 & 0xFF) / 255.0f;
        float g = (float)(currentColor >> 8 & 0xFF) / 255.0f;
        float b = (float)(currentColor & 0xFF) / 255.0f;
        float a = (float)(currentColor >> 24 & 0xFF) / 255.0f;
        if (shadow) {
            r *= shadowMultiplier;
            g *= shadowMultiplier;
            b *= shadowMultiplier;
        }
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        float cursorX = 0.0f;
        int baseAlpha = baseColor >> 24 & 0xFF;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '\u00a7' && i + 1 < text.length()) {
                char code = Character.toLowerCase(text.charAt(i + 1));
                ++i;
                if (code == 'r') {
                    currentColor = baseColor;
                } else {
                    boolean isColorCode;
                    boolean bl = isColorCode = code >= '0' && code <= '9' || code >= 'a' && code <= 'f';
                    if (isColorCode) {
                        EnumChatFormatting format = ColorUtil.getColorFromCode(code);
                        int rgb = ColorUtil.getRGBFromFormatting(format);
                        currentColor = (rgb >> 24 & 0xFF) == 0 ? baseAlpha << 24 | rgb & 0xFFFFFF : rgb;
                    }
                }
                float rr = (float)(currentColor >> 16 & 0xFF) / 255.0f;
                float gg = (float)(currentColor >> 8 & 0xFF) / 255.0f;
                float bb = (float)(currentColor & 0xFF) / 255.0f;
                float aa = (float)(currentColor >> 24 & 0xFF) / 255.0f;
                if (shadow) {
                    rr *= shadowMultiplier;
                    gg *= shadowMultiplier;
                    bb *= shadowMultiplier;
                }
                GlStateManager.color((float)rr, (float)gg, (float)bb, (float)aa);
                continue;
            }
            CharacterInfo info = this.charMap.get(Character.valueOf(c));
            if (info == null) {
                int finalColor = currentColor;
                if (shadow) {
                    int alpha = finalColor >> 24 & 0xFF;
                    int red = (int)((float)(finalColor >> 16 & 0xFF) * shadowMultiplier);
                    int green = (int)((float)(finalColor >> 8 & 0xFF) * shadowMultiplier);
                    int blue = (int)((float)(finalColor & 0xFF) * shadowMultiplier);
                    finalColor = alpha << 24 | red << 16 | green << 8 | blue;
                }
                float mcScale = size / 10.0f;
                float myScale = size / this.originalFontSize;
                float relativeScale = mcScale / myScale;
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)relativeScale, (float)relativeScale, (float)1.0f);
                this.mcFontRenderer.drawString(String.valueOf(c), cursorX / relativeScale, -8.0f, finalColor, false);
                GlStateManager.popMatrix();
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureLocation);
                GlStateManager.color((float)r, (float)g, (float)b, (float)a);
                cursorX += (float)this.mcFontRenderer.getCharWidth(c) * mcScale / myScale;
                continue;
            }
            if (info.width > 0 && info.height > 0) {
                float u1 = (float)info.textureX / 1024.0f;
                float v1 = (float)info.textureY / 1024.0f;
                float u2 = (float)(info.textureX + info.width) / 1024.0f;
                float v2 = (float)(info.textureY + info.height) / 1024.0f;
                float quadX = cursorX + info.offsetX;
                float quadY = info.offsetY;
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos((double)quadX, (double)(quadY + (float)info.height), 0.0).tex((double)u1, (double)v2).endVertex();
                worldRenderer.pos((double)(quadX + (float)info.width), (double)(quadY + (float)info.height), 0.0).tex((double)u2, (double)v2).endVertex();
                worldRenderer.pos((double)(quadX + (float)info.width), (double)quadY, 0.0).tex((double)u2, (double)v1).endVertex();
                worldRenderer.pos((double)quadX, (double)quadY, 0.0).tex((double)u1, (double)v1).endVertex();
                tessellator.draw();
            }
            cursorX += info.advance;
        }
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static class CharacterInfo {
        final int textureX;
        final int textureY;
        final int width;
        final int height;
        final float offsetX;
        final float offsetY;
        final float advance;

        CharacterInfo(int textureX, int textureY, int width, int height, float offsetX, float offsetY, float advance) {
            this.textureX = textureX;
            this.textureY = textureY;
            this.width = width;
            this.height = height;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.advance = advance;
        }
    }
}
