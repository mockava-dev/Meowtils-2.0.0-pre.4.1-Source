package wtf.tatp.meowtils.modules.antisnipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.BlacklistUtil;
import wtf.tatp.meowtils.util.ColorUtil;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.Prefix;
import wtf.tatp.meowtils.util.TeamUtil;
import wtf.tatp.meowtils.util.anticheat.AntiCheatData;
import wtf.tatp.meowtils.util.anticheat.PlayerData;

public class AntiCheat
extends Module {
    private BooleanValue detectAutoClicker;
    private BooleanValue detectKillaura;
    private BooleanValue detectAutoBlock;
    private BooleanValue detectLegitScaffold;
    private BooleanValue detectNoSlow;
    private BooleanValue wdrButton;
    private BooleanValue pingSound;
    private BooleanValue autoBlacklist;
    private NumberValue violationLevel;
    private static final Map<String, AntiCheatData> antiCheatDataMap = new HashMap<String, AntiCheatData>();
    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<UUID, PlayerData>();
    private static final Map<String, Map<String, Integer>> violationLevels = new HashMap<String, Map<String, Integer>>();

    public AntiCheat() {
        super("AntiCheat", "antiCheatKey", "antiCheat", Module.Category.Antisnipe);
        this.tooltip("Detects suspicious behaviour of players around you.");
        this.violationLevel = new NumberValue("Violation level", 0.0, 10.0, 1.0, null, "violationLevel", Integer.TYPE);
        this.addValue(this.violationLevel);
        this.pingSound = new BooleanValue("Flag sound", "flagPingSound");
        this.addBoolean(this.pingSound);
        this.autoBlacklist = new BooleanValue("Blacklist on flag", "antiCheatAutoBlacklist");
        this.addBoolean(this.autoBlacklist);
        this.wdrButton = new BooleanValue("WDR Button", "flagWDRButton");
        this.addBoolean(this.wdrButton);
        this.detectAutoBlock = new BooleanValue("AutoBlock", "detectAutoBlock");
        this.addBoolean(this.detectAutoBlock);
        this.detectNoSlow = new BooleanValue("NoSlow", "detectNoSlow");
        this.addBoolean(this.detectNoSlow);
        this.detectKillaura = new BooleanValue("Killaura", "detectKillaura");
        this.addBoolean(this.detectKillaura);
        this.detectAutoClicker = new BooleanValue("AutoClicker", "detectAutoClicker");
        this.addBoolean(this.detectAutoClicker);
        this.detectLegitScaffold = new BooleanValue("Legit Scaffold", "detectLegitScaffold");
        this.addBoolean(this.detectLegitScaffold);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (player == this.mc.thePlayer) continue;
            PlayerData data = playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());
            data.update(player);
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (player == this.mc.thePlayer || player.getName() == null || TeamUtil.ignoreTeam(player.getName())) continue;
            AntiCheatData data = antiCheatDataMap.computeIfAbsent(player.getName(), k -> new AntiCheatData());
            data.anticheatCheck(player);
            String playerName = player.getName();
            if (data.failedAutoBlock() && this.incrementViolation(playerName, "AutoBlock")) {
                this.sendFlagMessage(playerName, "AutoBlock");
                data.autoBlockCheck.reset();
                this.tryAutoBlacklist(playerName, "autoblock");
            }
            if (data.failedNoSlow() && this.incrementViolation(playerName, "NoSlow")) {
                this.sendFlagMessage(playerName, "NoSlow");
                data.noSlowCheck.reset();
                this.tryAutoBlacklist(playerName, "noslow");
            }
            if (data.failedLegitScaffold() && this.incrementViolation(playerName, "Legit Scaffold")) {
                this.sendFlagMessage(playerName, "Legit Scaffold");
                data.legitScaffoldCheck.reset();
                this.tryAutoBlacklist(playerName, "legit scaffold");
            }
            if (!data.failedKillauraB() || !this.incrementViolation(playerName, "KillAura")) continue;
            this.sendFlagMessage(playerName, "Killaura");
            data.killauraBCheck.reset();
            this.tryAutoBlacklist(playerName, "killaura");
        }
    }

    private boolean incrementViolation(String playerName, String checkType) {
        Map<String, Integer> playerViolations = violationLevels.computeIfAbsent(playerName, k -> new HashMap<String, Integer>());
        int newLevel = playerViolations.getOrDefault(checkType, 0) + 1;
        playerViolations.put(checkType, newLevel);
        if (newLevel >= cfg.v.violationLevel) {
            playerViolations.put(checkType, 0);
            return true;
        }
        return false;
    }

    private void tryAutoBlacklist(String playerName, String reason) {
        if (!cfg.v.antiCheatAutoBlacklist) {
            return;
        }
        if (GamemodeUtil.replay) {
            return;
        }
        for (NetworkPlayerInfo netInfo : this.mc.getNetHandler().getPlayerInfoMap()) {
            if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playerName)) continue;
            String uuid = netInfo.getGameProfile().getId().toString();
            this.handleBlacklist(uuid, playerName, reason);
            return;
        }
        this.handleBlacklist(null, playerName, reason);
    }

    private void handleBlacklist(String uuid, String name, String newReason) {
        String combinedReasons;
        String key = uuid != null ? uuid : name;
        boolean alreadyBlacklisted = BlacklistUtil.isBlacklisted(key);
        String existingEntry = BlacklistUtil.getEntry(key);
        if (alreadyBlacklisted && existingEntry != null) {
            String[] parts = existingEntry.split(" ", 2);
            String oldReasons = parts.length > 1 ? parts[1] : "";
            LinkedHashSet<String> reasonSet = new LinkedHashSet<String>(Arrays.asList(oldReasons.split(" \\| ")));
            reasonSet.add(newReason);
            combinedReasons = String.join((CharSequence)" | ", reasonSet);
            BlacklistUtil.removeFromBlacklist(key);
        } else {
            combinedReasons = newReason;
        }
        BlacklistUtil.addToBlacklist(key, combinedReasons);
    }

    private void sendFlagMessage(String playerName, String checkType) {
        String msg = NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GRAY + " failed " + ColorUtil.getColorFromString(cfg.v.flagMessageComponentColor) + checkType;
        if (cfg.v.flagWDRButton) {
            ChatComponentText message = new ChatComponentText(Prefix.getPrefix() + msg + " ");
            ChatComponentText wdrButton = new ChatComponentText(ColorUtil.getColorFromString(cfg.v.flagMessageBracketColor) + "[" + ColorUtil.getColorFromString(cfg.v.flagMessageButtonColor) + "WDR" + ColorUtil.getColorFromString(cfg.v.flagMessageBracketColor) + "]");
            wdrButton.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wdr " + playerName)).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Click to report this player."))));
            message.appendSibling((IChatComponent)wdrButton);
            this.mc.thePlayer.addChatMessage((IChatComponent)message);
        } else {
            Meowtils.addMessage(msg);
        }
        if (cfg.v.flagPingSound) {
            PlaySound.getInstance().playPingSound();
        }
        if (cfg.v.notify && cfg.v.notifyAntiCheat) {
            NotifyUtil.notifyParty(playerName + " failed " + checkType);
        }
    }

    public static void clear() {
        antiCheatDataMap.clear();
        playerDataMap.clear();
        violationLevels.clear();
    }
}
