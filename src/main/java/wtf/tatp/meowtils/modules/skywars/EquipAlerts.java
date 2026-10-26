package wtf.tatp.meowtils.modules.skywars;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.PlaySound;

public class EquipAlerts
extends Module {
    private BooleanValue sound;
    private BooleanValue helmet;
    private BooleanValue chestplate;
    private BooleanValue leggings;
    private BooleanValue boots;
    private static final Map<UUID, ItemStack[]> lastArmor = new HashMap<UUID, ItemStack[]>();

    public EquipAlerts() {
        super("EquipAlerts", "equipAlertsKey", "equipAlerts", Module.Category.Skywars);
        this.tooltip("Alerts when a player equips selected armor piece.");
        this.sound = new BooleanValue("Ping sound", "equipAlertsSound");
        this.addBoolean(this.sound);
        this.helmet = new BooleanValue("\u00a7bDiamond Helmet", "equipAlerts_helmet");
        this.addBoolean(this.helmet);
        this.chestplate = new BooleanValue("\u00a7bDiamond Chestplate", "equipAlerts_chestplate");
        this.addBoolean(this.chestplate);
        this.leggings = new BooleanValue("\u00a7bDiamond Leggings", "equipAlerts_leggings");
        this.addBoolean(this.leggings);
        this.boots = new BooleanValue("\u00a7bDiamond Boots", "equipAlerts_boots");
        this.addBoolean(this.boots);
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            ItemStack[] currentArmor = (ItemStack[])player.inventory.armorInventory.clone();
            ItemStack[] previousArmor = lastArmor.get(player.getUniqueID());
            if (previousArmor != null) {
                for (int i = 0; i < currentArmor.length; ++i) {
                    String armorName;
                    ItemStack before = previousArmor[i];
                    ItemStack now = currentArmor[i];
                    if (ItemStack.areItemStacksEqual((ItemStack)before, (ItemStack)now) || now == null || (armorName = this.allowedArmor(now.getItem())) == null) continue;
                    Meowtils.addMessage(NameUtil.getTabDisplayName(player.getName()) + EnumChatFormatting.GRAY + " equipped " + EnumChatFormatting.DARK_AQUA + armorName);
                    if (!cfg.v.equipAlertsSound) continue;
                    PlaySound.getInstance().playPingSoundMedium();
                }
            }
            lastArmor.put(player.getUniqueID(), currentArmor);
        }
    }

    private String allowedArmor(Item item) {
        if (item == Items.diamond_helmet && cfg.v.equipAlerts_helmet) {
            return "Diamond Helmet";
        }
        if (item == Items.diamond_chestplate && cfg.v.equipAlerts_chestplate) {
            return "Diamond Chestplate";
        }
        if (item == Items.diamond_leggings && cfg.v.equipAlerts_leggings) {
            return "Diamond Leggings";
        }
        if (item == Items.diamond_boots && cfg.v.equipAlerts_boots) {
            return "Diamond Boots";
        }
        return null;
    }

    public static void clear() {
        lastArmor.clear();
    }
}
