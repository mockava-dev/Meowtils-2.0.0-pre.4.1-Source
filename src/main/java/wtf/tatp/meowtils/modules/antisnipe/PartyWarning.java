package wtf.tatp.meowtils.modules.antisnipe;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;

public class PartyWarning
extends Module {
    private BooleanValue sound;
    private BooleanValue bedwars2;
    private BooleanValue bedwars3;
    private BooleanValue bedwars4;
    private int tickCounter = 0;
    private int messageCounter = 0;
    private boolean alerted = false;
    private boolean counting = false;

    public PartyWarning() {
        super("PartyWarning", "partyWarningKey", "partyWarning", Module.Category.Antisnipe);
        this.tooltip("Alerts you when a potential party joins your pre-game lobby.");
        this.sound = new BooleanValue("Ping sound", "partyWarningSound");
        this.addBoolean(this.sound);
        this.bedwars2 = new BooleanValue("Bedwars 2s", "partyWarning_bedwars2");
        this.addBoolean(this.bedwars2);
        this.bedwars3 = new BooleanValue("Bedwars 3s", "partyWarning_bedwars3");
        this.addBoolean(this.bedwars3);
        this.bedwars4 = new BooleanValue("Bedwars 4s", "partyWarning_bedwars4");
        this.addBoolean(this.bedwars4);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (this.counting) {
            --this.tickCounter;
            if (this.tickCounter <= 0) {
                this.counting = false;
                this.alerted = false;
                this.messageCounter = 0;
                this.tickCounter = 0;
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (msg.contains(":")) {
            return;
        }
        if (!msg.contains("has joined")) {
            return;
        }
        int joinCount = this.getBedwarsMode();
        if (!this.counting) {
            this.counting = true;
            this.tickCounter = 20;
            this.messageCounter = 1;
            this.alerted = false;
        } else {
            ++this.messageCounter;
            this.tickCounter = 20;
        }
        if (this.messageCounter >= joinCount && !this.alerted && joinCount > 0) {
            new DelayedTask(() -> {
                Meowtils.addMessage(EnumChatFormatting.RED + "Warning: " + EnumChatFormatting.BLUE + "Suspected party joined! " + EnumChatFormatting.DARK_GRAY + "(" + EnumChatFormatting.DARK_RED + joinCount + EnumChatFormatting.DARK_GRAY + ")");
                if (cfg.v.partyWarningSound) {
                    PlaySound.getInstance().playPingSoundDeep();
                }
                if (cfg.v.notify && cfg.v.notifyPartyWarning) {
                    NotifyUtil.notifyParty("Warning: Suspected party joined! (" + joinCount + ")");
                }
            }, 1);
            this.alerted = true;
        }
    }

    private int getBedwarsMode() {
        if (GamemodeUtil.bedwars4 && cfg.v.partyWarning_bedwars4) {
            return 4;
        }
        if (GamemodeUtil.bedwars3 && cfg.v.partyWarning_bedwars3) {
            return 3;
        }
        if (GamemodeUtil.bedwars2 && cfg.v.partyWarning_bedwars2) {
            return 2;
        }
        return 0;
    }
}
