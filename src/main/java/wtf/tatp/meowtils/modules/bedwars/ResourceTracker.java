package wtf.tatp.meowtils.modules.bedwars;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class ResourceTracker
extends Module {
    private BooleanValue resourcePingSound;
    private BooleanValue trackIron;
    private BooleanValue trackGold;
    private BooleanValue trackDiamond;
    private BooleanValue trackEmerald;
    private BooleanValue hideDefault;
    private static final Map<Item, Integer> resourceAmount = new HashMap<Item, Integer>();

    public ResourceTracker() {
        super("ResourceTracker", "resourceTrackerKey", "resourceTracker", Module.Category.Bedwars);
        this.tooltip("Tracks resources in your inventory.");
        this.resourcePingSound = new BooleanValue("Ping sound", "resourceTrackerSound");
        this.addBoolean(this.resourcePingSound);
        this.hideDefault = new BooleanValue("Hide default message", "resourceTrackerHideDefault");
        this.addBoolean(this.hideDefault);
        this.trackIron = new BooleanValue("Track \u00a77Iron Ingots", "trackIron");
        this.addBoolean(this.trackIron);
        this.trackGold = new BooleanValue("Track \u00a76Gold Ingots", "trackGold");
        this.addBoolean(this.trackGold);
        this.trackDiamond = new BooleanValue("Track \u00a7bDiamonds", "trackDiamond");
        this.addBoolean(this.trackDiamond);
        this.trackEmerald = new BooleanValue("Track \u00a72Emeralds", "trackEmerald");
        this.addBoolean(this.trackEmerald);
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        HashMap<Item, Integer> newAmount = new HashMap<Item, Integer>();
        newAmount.put(Items.iron_ingot, 0);
        newAmount.put(Items.gold_ingot, 0);
        newAmount.put(Items.diamond, 0);
        newAmount.put(Items.emerald, 0);
        for (ItemStack stack : this.mc.thePlayer.inventory.mainInventory) {
            if (stack == null || !newAmount.containsKey(stack.getItem())) continue;
            newAmount.put(stack.getItem(), (Integer)newAmount.get(stack.getItem()) + stack.stackSize);
        }
        this.checkResourceChange(newAmount);
        resourceAmount.clear();
        resourceAmount.putAll(newAmount);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (!cfg.v.resourceTrackerHideDefault) {
            return;
        }
        if (msg.startsWith("+") && (msg.contains("Iron") || msg.contains("Gold") || msg.contains("Emerald") || msg.contains("Diamond"))) {
            event.setCanceled(true);
        }
    }

    private void checkResourceChange(Map<Item, Integer> newCounts) {
        for (Map.Entry<Item, Integer> entry : newCounts.entrySet()) {
            int oldCount;
            Item item = entry.getKey();
            int newCount = entry.getValue();
            if (newCount == (oldCount = resourceAmount.getOrDefault(item, 0).intValue()) || !this.trackItems(item)) continue;
            String itemName = this.getItemName(item);
            String prefix = newCount > oldCount ? EnumChatFormatting.GREEN + "[+] " : EnumChatFormatting.RED + "[-] ";
            Meowtils.addMessage(prefix + itemName + " " + EnumChatFormatting.DARK_GRAY + "(" + newCount + ")");
            if (!cfg.v.resourceTrackerSound) continue;
            PlaySound.getInstance().playPingSoundLevel();
        }
    }

    private String getItemName(Item item) {
        if (item == Items.iron_ingot) {
            return EnumChatFormatting.WHITE + "Iron";
        }
        if (item == Items.gold_ingot) {
            return EnumChatFormatting.GOLD + "Gold";
        }
        if (item == Items.diamond) {
            return EnumChatFormatting.AQUA + "Diamond";
        }
        if (item == Items.emerald) {
            return EnumChatFormatting.DARK_GREEN + "Emerald";
        }
        return "Unknown Item";
    }

    private boolean trackItems(Item item) {
        if (item == Items.iron_ingot) {
            return this.trackIron.getState();
        }
        if (item == Items.gold_ingot) {
            return this.trackGold.getState();
        }
        if (item == Items.diamond) {
            return this.trackDiamond.getState();
        }
        if (item == Items.emerald) {
            return this.trackEmerald.getState();
        }
        return false;
    }

    public static void clear() {
        resourceAmount.clear();
    }
}
