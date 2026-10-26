package wtf.tatp.meowtils.modules.antisnipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.scoreboard.ScorePlayerTeam;
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
import wtf.tatp.meowtils.util.TeamUtil;

public class SniperWarning
extends Module {
    private BooleanValue sniperPingSound;
    private BooleanValue alertParty;
    private int tickCounter = 0;
    private static final Map<String, Boolean> notifiedPlayers = new HashMap<String, Boolean>();

    public SniperWarning() {
        super("SniperWarning", "sniperWarningKey", "sniperWarning", Module.Category.Antisnipe);
        this.tooltip("Warns you of potential snipers in your game.");
        this.sniperPingSound = new BooleanValue("Ping sound", "sniperWarningSound");
        this.addBoolean(this.sniperPingSound);
        this.alertParty = new BooleanValue("Alert party", "sniperWarningAlertParty");
        this.addBoolean(this.alertParty);
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
                this.checkGear(player);
            }
        }
    }

    private void checkGear(EntityPlayer player) {
        ItemSword sword;
        ItemStack heldItem;
        if (player == null || player.inventory == null) {
            return;
        }
        if (player == this.mc.thePlayer) {
            return;
        }
        if (TeamUtil.ignoreTeam(player.getName())) {
            return;
        }
        String name = player.getName();
        if ((name.contains("mcalt_") || name.contains("mcalts_") || name.contains("hassalt_") || name.contains("dogalt_") || name.contains("mal_") || name.contains("bym_") || name.contains("jy6_") || name.contains("lf_") || name.contains("wg_") || name.contains("ggnekito") || name.contains("dahai_") || name.contains("tzi_") || name.contains("nicegen") || name.contains("opalalts") || name.contains("tzi") || name.contains("msmc")) && !notifiedPlayers.containsKey(name)) {
            this.notifyPlayer(name);
            notifiedPlayers.put(name, true);
            return;
        }
        boolean hasChain = false;
        boolean hasIronSword = false;
        for (ItemStack armor : player.inventory.armorInventory) {
            ItemArmor itemArmor;
            if (armor == null || !(armor.getItem() instanceof ItemArmor) || (itemArmor = (ItemArmor)armor.getItem()).getArmorMaterial() != ItemArmor.ArmorMaterial.CHAIN) continue;
            hasChain = true;
            break;
        }
        if ((heldItem = player.getHeldItem()) != null && heldItem.getItem() instanceof ItemSword && (sword = (ItemSword)heldItem.getItem()).getToolMaterialName().equals("IRON")) {
            hasIronSword = true;
        }
        if (hasChain && hasIronSword && !notifiedPlayers.containsKey(player.getName())) {
            this.notifyPlayer(player.getName());
            notifiedPlayers.put(player.getName(), true);
        }
    }

    private void notifyPlayer(String playerName) {
        String displayName = NameUtil.getTabDisplayName(playerName);
        String displayNameClean = this.getCleanTabDisplayName(playerName);
        if (!"NONE".equals(displayName)) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Warning: " + displayName + EnumChatFormatting.GRAY + " might be a sniper!");
            if (cfg.v.sniperWarningSound) {
                PlaySound.getInstance().playPingSoundDeep();
            }
            if (cfg.v.sniperWarningAlertParty) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc [M] \u00bb Warning: " + displayNameClean + " might be a sniper!");
            }
        }
    }

    private boolean isPlayerInGame() {
        return this.mc.thePlayer != null && this.mc.theWorld != null;
    }

    private String getCleanTabDisplayName(String playerName) {
        ScorePlayerTeam playerTeam = Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(playerName);
        if (playerTeam == null) {
            return "NONE";
        }
        String prefix = playerTeam.getColorPrefix().replaceAll("\u00a7.", "");
        String suffix = playerTeam.getColorSuffix().replaceAll("\u00a7.", "");
        return prefix + playerName + suffix;
    }

    public static void clear() {
        notifiedPlayers.clear();
    }
}
