package wtf.tatp.meowtils.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class CapeCache {
    private static String lastCape = null;
    private static ResourceLocation cachedCape = null;

    public static ResourceLocation getCape(String selectedCape) {
        if (!selectedCape.equalsIgnoreCase(lastCape)) {
            lastCape = selectedCape;
            cachedCape = CapeCache.loadCape(selectedCape);
        }
        return cachedCape;
    }

    private static ResourceLocation loadCape(String selectedCape) {
        if ("Custom".equalsIgnoreCase(selectedCape)) {
            File customCapeFile = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/custom_cape/custom.png");
            if (customCapeFile.exists()) {
                try {
                    BufferedImage image = ImageIO.read(customCapeFile);
                    DynamicTexture dynamicTexture = new DynamicTexture(image);
                    return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("custom_cape", dynamicTexture);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        return new ResourceLocation("meowtils", "textures/cape/" + selectedCape + ".png");
    }
}
