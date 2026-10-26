package wtf.tatp.meowtils.modules.hypixel;

import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class Notify
extends Module {
    private BooleanValue antiCheat;
    private BooleanValue denicker;
    private BooleanValue armorAlerts;
    private BooleanValue bedTracker;
    private BooleanValue consumeAlerts;
    private BooleanValue itemAlerts;
    private BooleanValue upgradeAlerts;
    private BooleanValue notifyParty;

    public Notify() {
        super("Notify", "notifyKey", "notify", Module.Category.Hypixel);
        this.tooltip("Sends a notification to chat from selected modules.\n" + EnumChatFormatting.RED + "BETA");
        this.antiCheat = new BooleanValue("AntiCheat notify", "notifyAntiCheat");
        this.addBoolean(this.antiCheat);
        this.denicker = new BooleanValue("Denicker notify", "notifyDenicker");
        this.addBoolean(this.denicker);
        this.armorAlerts = new BooleanValue("ArmorAlerts notify", "notifyArmorAlerts");
        this.addBoolean(this.armorAlerts);
        this.bedTracker = new BooleanValue("BedTracker notify", "notifyBedTracker");
        this.addBoolean(this.bedTracker);
        this.consumeAlerts = new BooleanValue("ConsumeAlerts notify", "notifyConsumeAlerts");
        this.addBoolean(this.consumeAlerts);
        this.itemAlerts = new BooleanValue("ItemAlerts notify", "notifyItemAlerts");
        this.addBoolean(this.itemAlerts);
        this.upgradeAlerts = new BooleanValue("UpgradeAlerts notify", "notifyUpgradeAlerts");
        this.addBoolean(this.upgradeAlerts);
        this.notifyParty = new BooleanValue("PartyWarning alert", "notifyPartyWarning");
        this.addBoolean(this.notifyParty);
    }
}
