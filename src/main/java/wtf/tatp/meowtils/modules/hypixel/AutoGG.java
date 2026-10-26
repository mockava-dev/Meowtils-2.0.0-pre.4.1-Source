package wtf.tatp.meowtils.modules.hypixel;

import java.util.Arrays;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;

public class AutoGG
extends Module {
    private NumberValue firstMessageDelay;
    private NumberValue secondMessageDelay;
    private BooleanValue secondMessageToggle;
    private BooleanValue allowInAnyChat;
    private NumberValue autoGLDelay;
    private BooleanValue autoGL;
    private static final String[] TRIGGERS = new String[]{"1st Killer -", "1st Place -", "Winner:", "- Damage Dealt -", "Winning Team -", "1st -", "Winners:", "Winning Team:", " won the game!", "Top Seeker:", "1st Place:", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners -", "Sumo Duel -"};

    public AutoGG() {
        super("AutoGG", "autoGGKey", "autoGG", Module.Category.Hypixel);
        this.tooltip("Automatically sends a message after game end or before game start.\n\u00a75/autogg<1-2> \u00a77| \u00a75/autogl \u00a7ffor setting custom messages.");
        this.firstMessageDelay = new NumberValue("#1 message delay", 0.0, 1000.0, 50.0, "ms", "firstMessageDelay", Integer.TYPE);
        this.addValue(this.firstMessageDelay);
        this.secondMessageDelay = new NumberValue("#2 message delay", 0.0, 1000.0, 50.0, "ms", "secondMessageDelay", Integer.TYPE);
        this.addValue(this.secondMessageDelay);
        this.autoGLDelay = new NumberValue("Send GL at", 1.0, 15.0, 1.0, "s", "autoGLDelay", Integer.TYPE);
        this.addValue(this.autoGLDelay);
        this.secondMessageToggle = new BooleanValue("Send 2nd message", "secondMessageToggle");
        this.addBoolean(this.secondMessageToggle);
        this.allowInAnyChat = new BooleanValue("Allow in any chat", "allowInAnyChat");
        this.addBoolean(this.allowInAnyChat);
        this.autoGL = new BooleanValue("Auto GL", "autoGL");
        this.addBoolean(this.autoGL);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String end;
        String message = event.message.getUnformattedText();
        String sendAnyChat = cfg.v.allowInAnyChat ? "" : "/achat ";
        String string = end = cfg.v.autoGLDelay == 1 ? " second!" : " seconds!";
        if (message.contains(":")) {
            return;
        }
        if (Arrays.stream(TRIGGERS).anyMatch(message::contains)) {
            int firstDelay = cfg.v.firstMessageDelay / 50;
            int secondDelay = cfg.v.secondMessageDelay / 50;
            new DelayedTask(() -> Meowtils.sendCleanMessage(sendAnyChat + cfg.v.firstMessage), firstDelay);
            if (cfg.v.secondMessageToggle) {
                new DelayedTask(() -> Meowtils.sendCleanMessage(sendAnyChat + cfg.v.secondMessage), secondDelay);
            }
        }
        if (message.contains("The game starts in " + cfg.v.autoGLDelay + end) && !message.contains(":") && cfg.v.autoGL) {
            Meowtils.sendCleanMessage("/ac " + cfg.v.autoGLMessage);
        }
    }
}
