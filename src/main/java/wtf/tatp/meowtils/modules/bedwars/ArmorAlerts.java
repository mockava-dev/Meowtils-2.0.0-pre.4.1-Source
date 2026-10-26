package wtf.tatp.meowtils.modules.bedwars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
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
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.TeamUtil;

public class ArmorAlerts
extends Module {
    private BooleanValue armorPingSound;
    private BooleanValue detectDiamondArmor;
    private BooleanValue detectIronArmor;
    private BooleanValue detectChainArmor;
    private BooleanValue alertParty;
    private int tickCounter = 0;
    Minecraft mc;
    private static Map<String, String> currentPlayerArmorMap;
    List<Integer> cache;
    static Map<String, Integer> playerItemMap;
    static Map<String, String> playerItemNameMap;
    EntityPlayer you;

    public ArmorAlerts() {
        super("ArmorAlerts", "armorDetectorKey", "armorDetector", Module.Category.Bedwars);
        this.tooltip("Alerts you when players buy an armor upgrade.");
        this.armorPingSound = new BooleanValue("Ping sound", "armorDetectorSound");
        this.addBoolean(this.armorPingSound);
        this.detectChainArmor = new BooleanValue("\u00a77Chainmail Armor", "detectChainArmor");
        this.addBoolean(this.detectChainArmor);
        this.detectIronArmor = new BooleanValue("Iron Armor", "detectIronArmor");
        this.addBoolean(this.detectIronArmor);
        this.detectDiamondArmor = new BooleanValue("\u00a7bDiamond Armor", "detectDiamondArmor");
        this.addBoolean(this.detectDiamondArmor);
        this.cache = new ArrayList<Integer>();
        playerItemMap = new HashMap<String, Integer>();
        playerItemNameMap = new HashMap<String, String>();
        this.mc = Minecraft.getMinecraft();
        currentPlayerArmorMap = new HashMap<String, String>();
        this.you = Minecraft.getMinecraft().thePlayer;
    }

    public boolean isPlayerInGame() {
        return this.mc.thePlayer != null && this.mc.theWorld != null;
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld != null) {
                List<EntityPlayer> playerList = mc.theWorld.playerEntities;
                if (++this.tickCounter % 20 != 0) {
                    return;
                }
                for (EntityPlayer player : playerList) {
                    String playerName = player.getName();
                    this.processArmor(player);
                }
            }
        }
    }

    public void processArmor(EntityPlayer player) {
        ItemStack[] armorInventory = player.inventory.armorInventory;
        ItemStack leggingsStack = armorInventory[1];
        String playerName = player.getName();
        if (TeamUtil.inSpectator()) {
            return;
        }
        if (TeamUtil.ignoreSelf(playerName)) {
            return;
        }
        if (TeamUtil.ignoreTeam(playerName)) {
            return;
        }
        if (leggingsStack != null && leggingsStack.getItem() instanceof ItemArmor && ((ItemArmor)leggingsStack.getItem()).armorType == 2) {
            String check;
            String armorType = ((ItemArmor)leggingsStack.getItem()).getArmorMaterial().name();
            if (!currentPlayerArmorMap.containsKey(playerName)) {
                currentPlayerArmorMap.put(playerName, armorType);
            }
            String currentArmorType = currentPlayerArmorMap.get(playerName);
            if (cfg.v.detectDiamondArmor && !currentArmorType.equals(armorType) && armorType == "DIAMOND") {
                String check2 = NameUtil.getTabDisplayName(playerName);
                if (check2 != "NONE") {
                    Meowtils.addMessage(check2 + EnumChatFormatting.GRAY + " purchased " + EnumChatFormatting.AQUA + "Diamond Armor");
                    currentPlayerArmorMap.put(playerName, armorType);
                    if (cfg.v.armorDetectorSound) {
                        PlaySound.getInstance().playPingSound();
                    }
                    if (cfg.v.notify && cfg.v.notifyArmorAlerts) {
                        NotifyUtil.notifyParty(playerName + " purchased Diamond Armor");
                    }
                }
            } else if (cfg.v.detectIronArmor && !currentArmorType.equals(armorType) && armorType == "IRON") {
                String check3 = NameUtil.getTabDisplayName(playerName);
                if (check3 != "NONE") {
                    Meowtils.addMessage(check3 + EnumChatFormatting.GRAY + " purchased " + EnumChatFormatting.WHITE + "Iron Armor");
                    currentPlayerArmorMap.put(playerName, armorType);
                    if (cfg.v.armorDetectorSound) {
                        PlaySound.getInstance().playPingSound();
                    }
                    if (cfg.v.notify && cfg.v.notifyArmorAlerts) {
                        NotifyUtil.notifyParty(playerName + " purchased Iron Armor");
                    }
                }
            } else if (cfg.v.detectChainArmor && !currentArmorType.equals(armorType) && armorType == "CHAIN") {
                String check4 = NameUtil.getTabDisplayName(playerName);
                if (check4 != "NONE") {
                    Meowtils.addMessage(check4 + EnumChatFormatting.GRAY + " purchased " + EnumChatFormatting.DARK_GRAY + "Chainmail Armor");
                    currentPlayerArmorMap.put(playerName, armorType);
                    if (cfg.v.armorDetectorSound) {
                        PlaySound.getInstance().playPingSound();
                    }
                    if (cfg.v.notify && cfg.v.notifyArmorAlerts) {
                        NotifyUtil.notifyParty(playerName + " purchased Chainmail Armor");
                    }
                }
            } else if (!currentArmorType.equals(armorType) && armorType == "LEATHER" && (check = NameUtil.getTabDisplayName(playerName)) != "NONE") {
                currentPlayerArmorMap.put(playerName, armorType);
            }
        }
    }

    public static void clear() {
        currentPlayerArmorMap.clear();
        playerItemMap.clear();
        playerItemNameMap.clear();
    }
}
