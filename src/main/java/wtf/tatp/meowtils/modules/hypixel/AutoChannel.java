package wtf.tatp.meowtils.modules.hypixel;

import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.gui.Module;

public class AutoChannel
extends Module {
    public AutoChannel() {
        super("AutoChannel", "autoChannelKey", "autoChannel", Module.Category.Hypixel);
        this.tooltip("Automatically switches chat channel when joining/leaving a party.");
    }

    public static void swapToAll() {
        Meowtils.sendCleanMessage("/chat all");
    }

    public static void swapToParty() {
        Meowtils.sendCleanMessage("/chat party");
    }
}
