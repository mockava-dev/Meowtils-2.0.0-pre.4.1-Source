package wtf.tatp.meowtils.modules.utility;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.gui.Module;

public class Sprint
extends Module {
    private boolean toggled = true;

    public Sprint() {
        super("Sprint", "sprintKey", "sprint", Module.Category.Utility);
        this.tooltip("Automatically toggles sprint for you.");
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindSprint.getKeyCode(), (this.toggled && this.mc.gameSettings.keyBindForward.isKeyDown() ? 1 : 0) != 0);
    }
}
