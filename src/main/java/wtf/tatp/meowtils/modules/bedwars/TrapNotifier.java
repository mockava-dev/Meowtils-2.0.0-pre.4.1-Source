package wtf.tatp.meowtils.modules.bedwars;

import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.PlaySound;

public class TrapNotifier
extends Module {
    private BooleanValue sound;
    private boolean notified = false;
    private int tickCounter = 0;
    private String trap = "";
    private boolean revealTrap = false;

    public TrapNotifier() {
        super("TrapNotifier", "trapNotifierKey", "trapNotifier", Module.Category.Bedwars);
        this.tooltip("Notifies you when trigger a trap.");
        this.sound = new BooleanValue("Ping sound", "trapNotifierSound");
        this.addBoolean(this.sound);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.equals("Your invisibility was removed by an Reveal Trap!")) {
            this.revealTrap = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        boolean currentTrap;
        if (this.mc.thePlayer == null || this.mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        ++this.tickCounter;
        if (this.tickCounter != 20) {
            return;
        }
        this.tickCounter = 0;
        boolean bl = currentTrap = this.mc.thePlayer.isPotionActive(Potion.digSlowdown) || this.mc.thePlayer.isPotionActive(Potion.blindness) || this.revealTrap;
        if (!this.notified) {
            if (this.mc.thePlayer.isPotionActive(Potion.digSlowdown)) {
                this.trap = "Miner Fatigue";
                this.alert();
            } else if (this.mc.thePlayer.isPotionActive(Potion.blindness)) {
                this.trap = "Blindness";
                this.alert();
            } else if (this.revealTrap) {
                this.trap = "Reveal";
                this.revealTrap = false;
                this.alert();
            }
        }
        if (!currentTrap) {
            this.notified = false;
        }
    }

    private void alert() {
        Meowtils.addMessage(EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "TRAP TRIGGERED! " + EnumChatFormatting.DARK_GRAY + "(" + EnumChatFormatting.GOLD + this.trap + EnumChatFormatting.DARK_GRAY + ")");
        this.notified = true;
        if (cfg.v.trapNotifierSound) {
            PlaySound.getInstance().playAnvilBreakSound();
        }
    }
}
