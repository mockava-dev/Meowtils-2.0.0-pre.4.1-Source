package wtf.tatp.meowtils.modules.meowtils;

import java.util.Arrays;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class Settings
extends Module {
    private BooleanValue autoUpdate;
    private BooleanValue smoothFont;
    private ArrayValue hideMod;
    private ArrayValue prefix;

    public Settings() {
        super("Settings", null, null, Module.Category.Meowtils, true);
        this.tooltip("General Meowtils settings.");
        this.autoUpdate = new BooleanValue("Auto-Updates", "autoUpdate");
        this.addBoolean(this.autoUpdate);
        this.smoothFont = new BooleanValue("Smooth font", "smoothFont");
        this.addBoolean(this.smoothFont);
        this.hideMod = new ArrayValue("Hide mods", Arrays.asList("Meowtils", "None"), "modID");
        this.addArray(this.hideMod);
        this.prefix = new ArrayValue("Prefix", Arrays.asList("Default", "Myau", "Fire", "Nebula", "Air", "Custom", "Short"), "meowtilsPrefixMode");
        this.addArray(this.prefix);
    }
}
