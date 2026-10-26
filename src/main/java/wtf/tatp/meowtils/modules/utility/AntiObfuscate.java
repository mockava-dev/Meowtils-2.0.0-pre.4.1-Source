package wtf.tatp.meowtils.modules.utility;

import wtf.tatp.meowtils.gui.Module;

public class AntiObfuscate
extends Module {
    public AntiObfuscate() {
        super("AntiObfuscate", "antiObfuscateKey", "antiObfuscate", Module.Category.Utility);
        this.tooltip("Deobfuscates text.");
    }
}
