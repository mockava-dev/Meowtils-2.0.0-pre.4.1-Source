package wtf.tatp.meowtils.gui;

import net.minecraft.client.Minecraft;
import wtf.tatp.meowtils.config.cfg;

public class GuiScale {
    public static int getEffectiveGuiScale() {
        int scale;
        Minecraft mc = Minecraft.getMinecraft();
        int scaleSetting = mc.gameSettings.guiScale;
        int scaled = scaleSetting == 0 ? 1000 : scaleSetting;
        for (scale = 0; scale < scaled && mc.displayWidth / (scale + 1) >= 320 && mc.displayHeight / (scale + 1) >= 240; ++scale) {
        }
        return scale;
    }

    public static float customGuiScale() {
        String guiScale;
        switch (guiScale = cfg.v.guiScale) {
            case "Tiny": {
                return 0.8f;
            }
            case "Small": {
                return 0.9f;
            }
            case "Normal": {
                return 1.0f;
            }
            case "Large": {
                return 1.1f;
            }
            case "Huge": {
                return 1.2f;
            }
        }
        return 1.0f;
    }
}
