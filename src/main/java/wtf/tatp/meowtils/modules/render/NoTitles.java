package wtf.tatp.meowtils.modules.render;

import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.events.ReceivePacketEvent;
import wtf.tatp.meowtils.gui.Module;

public class NoTitles
extends Module {
    public NoTitles() {
        super("NoTitles", "noTitlesKey", "noTitles", Module.Category.Render);
        this.tooltip("Disables titles");
    }

    @SubscribeEvent
    public void onPacketReceived(ReceivePacketEvent event) {
        if (event.getPacket() instanceof S45PacketTitle) {
            event.setCanceled(true);
        }
    }
}
