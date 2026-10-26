package wtf.tatp.meowtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;

public class MeowtilsAlert {
    private int tickCounter = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        ++this.tickCounter;
        if (this.tickCounter < 60) {
            return;
        }
        Meowtils.addMessage("\u00a77This is a \u00a75\u00a7lMeowtils \u00a77pre-release. \u00a77Some features are subject to change. \u00a7c\u00a7lThis version may not be complete!");
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
