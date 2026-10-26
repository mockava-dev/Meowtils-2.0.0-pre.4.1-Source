package wtf.tatp.meowtils.modules.hypixel;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class AutoWho
extends Module {
    private int ticksRemaining = -1;
    private BooleanValue hideMessage;

    public AutoWho() {
        super("AutoWho", "autoWhoKey", "autoWho", Module.Category.Hypixel);
        this.tooltip("Automatically runs /who after a game started. May be required by some modules.");
        this.hideMessage = new BooleanValue("Hide message", "hideAutoWhoMessage");
        this.addBoolean(this.hideMessage);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.equals("The game starts in 1 second!")) {
            this.ticksRemaining = 60;
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void hideAutoWhoMessage(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.contains("ONLINE:") && cfg.v.hideAutoWhoMessage) {
            event.setCanceled(true);
        }
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.ticksRemaining > 0) {
            --this.ticksRemaining;
            if (this.ticksRemaining == 0) {
                Meowtils.sendCleanMessage("/who");
                this.ticksRemaining = -1;
            }
        }
    }
}
