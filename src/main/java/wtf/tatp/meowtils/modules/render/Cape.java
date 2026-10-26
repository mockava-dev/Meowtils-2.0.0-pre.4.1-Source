package wtf.tatp.meowtils.modules.render;

import java.util.Arrays;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;

public class Cape
extends Module {
    private ArrayValue cape;

    public Cape() {
        super("Cape", "capeKey", "capeSelector", Module.Category.Render);
        this.tooltip("Renders a cape on you. You may import your own \"custom\" cape file.\n\u00a75/capefolder \u00a77- \u00a7fto open");
        this.cape = new ArrayValue("Cape", Arrays.asList("2011", "2012", "2013", "2015", "2016", "Experience", "Founder", "Cobalt", "Astolfo", "Moon", "Myau", "Raven", "Custom"), "selectedCape");
        this.addArray(this.cape);
    }
}
