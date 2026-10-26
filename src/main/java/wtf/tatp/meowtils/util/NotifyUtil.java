package wtf.tatp.meowtils.util;

import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.handlers.PartyHandler;
import wtf.tatp.meowtils.util.GamemodeUtil;

public class NotifyUtil {
    private static Queue<String> messageQueue = new LinkedList<String>();
    private static int tickCounter = 0;

    public static void notifyPartyQueue(String message) {
        messageQueue.add(message);
    }

    public static void notifyParty(String text) {
        if (!GamemodeUtil.hypixel) {
            return;
        }
        if (!cfg.v.notify) {
            return;
        }
        Meowtils.debugMessage(EnumChatFormatting.YELLOW + "[Notify]: " + EnumChatFormatting.WHITE + "Sent: " + text);
        if (!PartyHandler.inParty()) {
            return;
        }
        Meowtils.sendMessage(text);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (++tickCounter < 20) {
            return;
        }
        tickCounter = 0;
        if (messageQueue.size() > 20) {
            messageQueue.clear();
            Meowtils.addMessage(EnumChatFormatting.RED + "Error notifying party. Cleared queue.");
        }
        if (!messageQueue.isEmpty()) {
            NotifyUtil.notifyParty(messageQueue.poll());
        }
    }
}
