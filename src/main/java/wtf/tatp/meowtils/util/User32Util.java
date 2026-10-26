package wtf.tatp.meowtils.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import net.minecraft.client.Minecraft;

public class User32Util {
    private static User32 user32Instance;

    private static User32 getUser32() {
        Minecraft mc = Minecraft.getMinecraft();
        if (user32Instance == null && Platform.isWindows() && mc.thePlayer != null && mc.theWorld != null && mc.inGameHasFocus) {
            try {
                user32Instance = Native.loadLibrary("user32", User32.class);
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return user32Instance;
    }

    public static boolean holdingRightClick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.inGameHasFocus || mc.currentScreen != null || mc.theWorld == null || mc.thePlayer == null) {
            return false;
        }
        User32 u32 = User32Util.getUser32();
        return u32 != null && (u32.GetAsyncKeyState(2) & 0x8000) != 0;
    }

    public static boolean holdingLeftClick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.inGameHasFocus || mc.currentScreen != null || mc.theWorld == null || mc.thePlayer == null) {
            return false;
        }
        User32 u32 = User32Util.getUser32();
        return u32 != null && (u32.GetAsyncKeyState(1) & 0x8000) != 0;
    }

    public static boolean isKeyDown(int lwjglKeyCode) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.inGameHasFocus || mc.currentScreen != null || mc.theWorld == null || mc.thePlayer == null) {
            return false;
        }
        User32 u32 = User32Util.getUser32();
        if (u32 == null) {
            return false;
        }
        int vk = User32Util.lwjglToVK(lwjglKeyCode);
        if (vk == 0) {
            return false;
        }
        return (u32.GetAsyncKeyState(vk) & 0x8000) != 0;
    }

    private static int lwjglToVK(int lwjglKey) {
        switch (lwjglKey) {
            case 30: {
                return 65;
            }
            case 48: {
                return 66;
            }
            case 46: {
                return 67;
            }
            case 32: {
                return 68;
            }
            case 18: {
                return 69;
            }
            case 33: {
                return 70;
            }
            case 34: {
                return 71;
            }
            case 35: {
                return 72;
            }
            case 23: {
                return 73;
            }
            case 36: {
                return 74;
            }
            case 37: {
                return 75;
            }
            case 38: {
                return 76;
            }
            case 50: {
                return 77;
            }
            case 49: {
                return 78;
            }
            case 24: {
                return 79;
            }
            case 25: {
                return 80;
            }
            case 16: {
                return 81;
            }
            case 19: {
                return 82;
            }
            case 31: {
                return 83;
            }
            case 20: {
                return 84;
            }
            case 22: {
                return 85;
            }
            case 47: {
                return 86;
            }
            case 17: {
                return 87;
            }
            case 45: {
                return 88;
            }
            case 21: {
                return 89;
            }
            case 44: {
                return 90;
            }
            case 11: {
                return 48;
            }
            case 2: {
                return 49;
            }
            case 3: {
                return 50;
            }
            case 4: {
                return 51;
            }
            case 5: {
                return 52;
            }
            case 6: {
                return 53;
            }
            case 7: {
                return 54;
            }
            case 8: {
                return 55;
            }
            case 9: {
                return 56;
            }
            case 10: {
                return 57;
            }
            case 42: {
                return 160;
            }
            case 54: {
                return 161;
            }
            case 29: {
                return 162;
            }
            case 157: {
                return 163;
            }
            case 56: {
                return 164;
            }
            case 184: {
                return 165;
            }
            case 57: {
                return 32;
            }
            case 28: {
                return 13;
            }
            case 15: {
                return 9;
            }
            case 14: {
                return 8;
            }
            case 1: {
                return 27;
            }
            case 200: {
                return 38;
            }
            case 208: {
                return 40;
            }
            case 203: {
                return 37;
            }
            case 205: {
                return 39;
            }
        }
        return 0;
    }

    public static interface User32
    extends Library {
        public short GetAsyncKeyState(int var1);
    }
}
