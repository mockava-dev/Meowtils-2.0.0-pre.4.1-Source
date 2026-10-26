package wtf.tatp.meowtils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class Wrapper {
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return Wrapper.getMinecraft().thePlayer;
    }

    public static WorldClient getWorld() {
        return Wrapper.getMinecraft().theWorld;
    }

    public static void drawCenteredString(String text, int x, int y, int color) {
        Wrapper.getMinecraft().fontRendererObj.drawString(text, x - Wrapper.getMinecraft().fontRendererObj.getStringWidth(text) / 2, y, color);
    }
}
