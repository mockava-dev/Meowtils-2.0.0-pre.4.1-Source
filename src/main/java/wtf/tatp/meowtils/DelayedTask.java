package wtf.tatp.meowtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DelayedTask {
    private final Runnable runnable;
    private int counter;

    public DelayedTask(Runnable task, int ticks) {
        this.runnable = task;
        this.counter = ticks;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (this.counter-- <= 0) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            try {
                if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) {
                    return;
                }
                this.runnable.run();
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
