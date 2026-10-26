package wtf.tatp.meowtils.modules.meowtils;

import java.util.Arrays;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;

public class GUI
extends Module {
    private ArrayValue test;
    private ArrayValue mode;
    private BooleanValue guiTooltips;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;

    public GUI() {
        super("GUI", "guiBind", null, Module.Category.Meowtils, true);
        this.tooltip("GUI related settings.\nYou may also middle-click to bind GUI or any other module.");
        this.color = new ColorComponent("meowtils_red", "meowtils_green", "meowtils_blue");
        this.rgb = new ColorValue("GUI color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.guiTooltips = new BooleanValue("Show tooltips", "guiTooltips");
        this.addBoolean(this.guiTooltips);
        this.mode = new ArrayValue("GUI Scale", Arrays.asList("Tiny", "Small", "Normal", "Large", "Huge"), "guiScale");
        this.addArray(this.mode);
    }
}
