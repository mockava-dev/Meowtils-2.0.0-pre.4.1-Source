package wtf.tatp.meowtils.modules.render;

import wtf.tatp.meowtils.gui.Module;

public class ShinyPots
extends Module {
    public ShinyPots() {
        super("ShinyPots", "shinyPotsKey", "shinyPots", Module.Category.Render);
        this.tooltip("Renders potion color as slot background.");
    }
}
