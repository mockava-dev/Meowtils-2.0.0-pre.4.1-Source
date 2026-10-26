package wtf.tatp.meowtils.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.GamemodeUtil;

public class QueueHandler {
    private String detectedMode = "";
    private int ticks = 0;
    private boolean scoreboardAvailable = false;
    private boolean awaitingLocraw = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null) {
            return;
        }
        if (this.awaitingLocraw && !this.scoreboardAvailable && mc.theWorld.getScoreboard() != null) {
            this.scoreboardAvailable = true;
        }
        if (this.scoreboardAvailable && ++this.ticks == 20 && GamemodeUtil.hypixel) {
            Meowtils.sendCleanMessage("/locraw");
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (this.awaitingLocraw) {
            String locraw = msg.trim();
            if (!locraw.startsWith("{")) {
                return;
            }
            this.awaitingLocraw = false;
            try {
                int index;
                if (!(locraw.contains("dynamic") || locraw.contains("REPLAY") || locraw.contains("hub") || locraw.equals("{\"server\":\"limbo\"}") || locraw.equals("HOUSING") || (index = locraw.indexOf("mode\":\"")) == -1)) {
                    cfg.v.lastPlayCommand = this.detectedMode = locraw.substring(index + 7).split("\"")[0];
                    if (cfg.v.requeueSavedCommandMessage) {
                        Meowtils.addMessage(EnumChatFormatting.GREEN + "Saved play command: " + this.detectedMode);
                        cfg.save();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.entity == mc.thePlayer) {
            this.scoreboardAvailable = false;
            this.awaitingLocraw = true;
            this.ticks = 0;
        }
    }
}
