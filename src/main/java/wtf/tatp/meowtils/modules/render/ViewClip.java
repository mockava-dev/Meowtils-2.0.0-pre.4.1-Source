package wtf.tatp.meowtils.modules.render;

import wtf.tatp.meowtils.gui.Module;

public class ViewClip
extends Module {
    public ViewClip() {
        super("ViewClip", "viewClipKey", "viewClip", Module.Category.Render);
        this.tooltip("Allows your view to clip through blocks when in third person.");
    }
}
