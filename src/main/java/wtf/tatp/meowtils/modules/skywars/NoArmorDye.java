package wtf.tatp.meowtils.modules.skywars;

import java.util.Arrays;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;

public class NoArmorDye
extends Module {
    private ArrayValue mode;

    public NoArmorDye() {
        super("NoArmorDye", "noArmorDyeKey", "noArmorDye", Module.Category.Skywars);
        this.tooltip("Removes dye from leather armor.");
        this.mode = new ArrayValue("Mode", Arrays.asList("Both", "Model", "Item"), "noArmorDyeMode");
        this.addArray(this.mode);
    }
}
