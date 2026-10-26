package wtf.tatp.meowtils.modules.utility;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.handlers.KeybindHandler;

public class AutoText
extends Module {
    public AutoText() {
        super("AutoText", "autoTextKey", "autoText", Module.Category.Utility);
        this.tooltip("Automatically send a message on key press.\nSet message: \u00a75/at<1-9> \u00a75<msg> \u00a77or \u00a75/autotext<1-9> \u00a75<msg>\nSet keybind: Options -> Controls");
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        for (int i = 0; i < 10; ++i) {
            if (!KeybindHandler.autotextKeybinds[i].isPressed()) continue;
            String msg = this.getMessageForIndex(i);
            if (msg != null && !msg.isEmpty()) {
                Meowtils.sendCleanMessage(msg);
                continue;
            }
            Meowtils.addMessage("No message set!");
        }
    }

    private String getMessageForIndex(int index) {
        switch (index) {
            case 0: {
                return cfg.v.autoText0;
            }
            case 1: {
                return cfg.v.autoText1;
            }
            case 2: {
                return cfg.v.autoText2;
            }
            case 3: {
                return cfg.v.autoText3;
            }
            case 4: {
                return cfg.v.autoText4;
            }
            case 5: {
                return cfg.v.autoText5;
            }
            case 6: {
                return cfg.v.autoText6;
            }
            case 7: {
                return cfg.v.autoText7;
            }
            case 8: {
                return cfg.v.autoText8;
            }
            case 9: {
                return cfg.v.autoText9;
            }
        }
        return "";
    }
}
