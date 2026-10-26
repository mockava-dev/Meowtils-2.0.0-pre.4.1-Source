package wtf.tatp.meowtils.modules.hypixel;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.GamemodeUtil;

public class AutoTip
extends Module {
    private BooleanValue hideMessages;
    private NumberValue delay;
    private int tickCounter = 0;
    private long lastTipTime = 0L;

    public AutoTip() {
        super("AutoTip", "autoTipKey", "autoTip", Module.Category.Hypixel);
        this.tooltip("Automatically runs /tipall every x minutes.");
        this.hideMessages = new BooleanValue("Hide messages", "autoTipHideMessages");
        this.addBoolean(this.hideMessages);
        this.delay = new NumberValue("Delay", 1.0, 10.0, 1.0, "min", "autoTipDelay", Integer.TYPE);
        this.addValue(this.delay);
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        long delay;
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (!GamemodeUtil.hypixel) {
            return;
        }
        ++this.tickCounter;
        if (this.tickCounter < 200) {
            return;
        }
        this.tickCounter = 0;
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastTipTime >= (delay = (long)cfg.v.autoTipDelay * 60000L)) {
            Meowtils.sendCleanMessage("/tipall");
            this.lastTipTime = currentTime;
            if (cfg.v.debugMessages) {
                Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AutoTip] " + EnumChatFormatting.WHITE + " Tipped all players.");
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.hypixel) {
            return;
        }
        if (msg.contains("You tipped") || msg.contains("You already tipped everyone") && cfg.v.autoTipHideMessages) {
            event.setCanceled(true);
        }
    }
}
