package wtf.tatp.meowtils.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class KeybindHandler {
    public static final int NUM_KEYS = 10;
    public static final KeyBinding[] autotextKeybinds = new KeyBinding[10];
    public static KeyBinding fakeAutoBlockKeybind;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (Keyboard.isKeyDown((int)cfg.v.guiBind) && mc.currentScreen == null && mc.thePlayer != null && mc.theWorld != null) {
            mc.displayGuiScreen((GuiScreen)Meowtils.getClickGUI());
        }
    }

    public static void init() {
        for (int i = 0; i < 10; ++i) {
            KeybindHandler.autotextKeybinds[i] = new KeyBinding(String.format("AutoText message %02d", i + 1), 0, "Meowtils");
            ClientRegistry.registerKeyBinding((KeyBinding)autotextKeybinds[i]);
        }
        fakeAutoBlockKeybind = new KeyBinding("FakeAutoBlock extra toggle", 0, "Meowtils");
        ClientRegistry.registerKeyBinding((KeyBinding)fakeAutoBlockKeybind);
    }
}
