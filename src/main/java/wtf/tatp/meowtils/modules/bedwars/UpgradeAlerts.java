package wtf.tatp.meowtils.modules.bedwars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.ColorUtil;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.TeamUtil;

public class UpgradeAlerts
extends Module {
    private BooleanValue upgradeTrackerSound;
    private int tickCounter = 0;
    private static Map<String, Set<String>> playerUpgradeMap = new HashMap<String, Set<String>>();
    private static Map<String, Set<String>> teamUpgradeMap = new HashMap<String, Set<String>>();

    public UpgradeAlerts() {
        super("UpgradeAlerts", "upgradeTrackerKey", "upgradeTracker", Module.Category.Bedwars);
        this.tooltip("Alerts when a team buys a team upgrade.");
        this.upgradeTrackerSound = new BooleanValue("Ping sound", "upgradeTrackerSound");
        this.addBoolean(this.upgradeTrackerSound);
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (event.phase == TickEvent.Phase.END && this.mc.theWorld != null) {
            List<EntityPlayer> playerList = this.mc.theWorld.playerEntities;
            if (++this.tickCounter % 20 != 0) {
                return;
            }
            for (EntityPlayer player : playerList) {
                this.checkForEnchantments(player);
            }
        }
    }

    private void checkForEnchantments(EntityPlayer player) {
        ItemStack chestplate;
        if (player == null || player.inventory == null) {
            return;
        }
        String playerName = player.getName();
        if (TeamUtil.ignoreSelf(playerName)) {
            return;
        }
        if (TeamUtil.ignoreTeam(playerName)) {
            return;
        }
        if (TeamUtil.inSpectator()) {
            return;
        }
        Set upgrades = playerUpgradeMap.getOrDefault(playerName, new HashSet());
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() instanceof ItemSword && heldItem.isItemEnchanted()) {
            this.notifyUpgrade(playerName, "Sharpened Swords", upgrades);
        }
        if ((chestplate = player.inventory.armorInventory[2]) != null && chestplate.getItem() instanceof ItemArmor && chestplate.isItemEnchanted()) {
            this.notifyUpgrade(playerName, "Reinforced Armor", upgrades);
        }
        playerUpgradeMap.put(playerName, upgrades);
    }

    private void notifyUpgrade(String playerName, String upgrade, Set<String> upgrades) {
        String displayName = this.getTabDisplayName(playerName);
        if (!"NONE".equals(displayName)) {
            ScorePlayerTeam playerTeam = this.mc.theWorld.getScoreboard().getPlayersTeam(playerName);
            if (playerTeam == null) {
                return;
            }
            String formattedName = ScorePlayerTeam.formatPlayerName((Team)playerTeam, (String)playerName);
            EnumChatFormatting colorCode = NameUtil.getNameColor(formattedName);
            String teamName = this.getFormattedTeamName(colorCode);
            if (teamName == null) {
                return;
            }
            Set teamUpgrades = teamUpgradeMap.getOrDefault(teamName, new HashSet());
            if (teamUpgrades.contains(upgrade)) {
                return;
            }
            EnumChatFormatting color = EnumChatFormatting.WHITE;
            if (upgrade.contains("Sharpened Swords")) {
                color = EnumChatFormatting.DARK_AQUA;
            } else if (upgrade.contains("Reinforced Armor")) {
                color = EnumChatFormatting.DARK_AQUA;
            }
            Meowtils.addMessage(teamName + EnumChatFormatting.GRAY + " purchased " + color + upgrade);
            teamUpgrades.add(upgrade);
            teamUpgradeMap.put(teamName, teamUpgrades);
            if (cfg.v.notify && cfg.v.notifyUpgradeAlerts) {
                NotifyUtil.notifyParty(ColorUtil.unformattedText(teamName) + " purchased " + upgrade);
            }
            if (cfg.v.upgradeTrackerSound) {
                PlaySound.getInstance().playPingSound();
            }
        }
    }

    private String getTabDisplayName(String playerName) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().theWorld.getScoreboard() == null) {
            return "NONE";
        }
        ScorePlayerTeam playerTeam = Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(playerName);
        if (playerTeam == null) {
            return "NONE";
        }
        return ScorePlayerTeam.formatPlayerName((Team)playerTeam, (String)playerName);
    }

    private String getFormattedTeamName(EnumChatFormatting color) {
        switch (color) {
            case RED: {
                return EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "Red Team";
            }
            case BLUE: {
                return EnumChatFormatting.BLUE.toString() + EnumChatFormatting.BOLD + "Blue Team";
            }
            case GREEN: {
                return EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "Green Team";
            }
            case YELLOW: {
                return EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.BOLD + "Yellow Team";
            }
            case AQUA: {
                return EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Aqua Team";
            }
            case WHITE: {
                return EnumChatFormatting.WHITE.toString() + EnumChatFormatting.BOLD + "White Team";
            }
            case LIGHT_PURPLE: {
                return EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD + "Pink Team";
            }
            case DARK_GRAY: {
                return EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.BOLD + "Gray Team";
            }
        }
        return null;
    }

    public static void clear() {
        teamUpgradeMap.clear();
        playerUpgradeMap.clear();
    }
}
