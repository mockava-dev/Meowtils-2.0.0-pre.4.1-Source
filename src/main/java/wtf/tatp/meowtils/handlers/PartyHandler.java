package wtf.tatp.meowtils.handlers;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.modules.hypixel.AutoChannel;

public class PartyHandler {
    private static boolean inParty;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.contains(":")) {
            return;
        }
        if (msg.endsWith("has disbanded the party!")) {
            inParty = false;
            AutoChannel.swapToAll();
        }
        if (msg.equals("The party was disbanded because all invites expired and the party was empty.")) {
            inParty = false;
            AutoChannel.swapToAll();
        }
        if (msg.startsWith("You have joined") && msg.endsWith("party!")) {
            inParty = true;
            AutoChannel.swapToParty();
        }
        if (msg.endsWith("joined the party.")) {
            inParty = true;
            AutoChannel.swapToParty();
        }
        if (msg.equals("You left the party.")) {
            inParty = false;
            AutoChannel.swapToAll();
        }
    }

    public static boolean inParty() {
        return inParty;
    }
}
