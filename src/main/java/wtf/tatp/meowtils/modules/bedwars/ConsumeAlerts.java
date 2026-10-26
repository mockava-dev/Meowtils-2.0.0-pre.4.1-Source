package wtf.tatp.meowtils.modules.bedwars;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.Entity;
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
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.TeamUtil;

public class ConsumeAlerts
extends Module {
    private static final Map<UUID, Long> lastAlertTime = new HashMap<UUID, Long>();
    private static final long ALERT_COOLDOWN = 1600L;
    private NumberValue distance;
    private BooleanValue showDistance;
    private BooleanValue pingSound;
    private BooleanValue detectApple;
    private BooleanValue detectMilk;
    private BooleanValue detectInvis;
    private BooleanValue detectSpeed;
    private BooleanValue detectJump;
    private static final Map<UUID, TrackedUse> usingItems = new HashMap<UUID, TrackedUse>();

    public ConsumeAlerts() {
        super("ConsumeAlerts", "consumeAlertsKey", "consumeAlerts", Module.Category.Bedwars);
        this.tooltip("Alerts you of players gaining specific potion effects.");
        this.distance = new NumberValue("Max distance", 0.0, 250.0, 5.0, "m", "consumeAlerts_distance", Integer.TYPE);
        this.addValue(this.distance);
        this.pingSound = new BooleanValue("Ping sound", "consumeAlertsSound");
        this.addBoolean(this.pingSound);
        this.showDistance = new BooleanValue("Show distance", "consumeAlertsShowDistance");
        this.addBoolean(this.showDistance);
        this.detectApple = new BooleanValue("\u00a76Golden Apple ", "consumeAlerts_goldenapple");
        this.addBoolean(this.detectApple);
        this.detectMilk = new BooleanValue("Milk", "consumeAlerts_milk");
        this.addBoolean(this.detectMilk);
        this.detectSpeed = new BooleanValue("\u00a7eSpeed Potion", "consumeAlerts_speed");
        this.addBoolean(this.detectSpeed);
        this.detectJump = new BooleanValue("\u00a7aJump Potion", "consumeAlerts_jump");
        this.addBoolean(this.detectJump);
        this.detectInvis = new BooleanValue("\u00a7bInvis Potion", "consumeAlerts_invis");
        this.addBoolean(this.detectInvis);
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || this.mc.theWorld == null) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (player == this.mc.thePlayer || TeamUtil.ignoreTeam(player.getName())) continue;
            if (TeamUtil.inSpectator()) {
                return;
            }
            float maxDistance = cfg.v.consumeAlerts_distance;
            float distanceToPlayer = player.getDistanceToEntity((Entity)this.mc.thePlayer);
            if (maxDistance > 0.0f && distanceToPlayer > maxDistance) continue;
            UUID uuid = player.getUniqueID();
            ItemStack heldItem = player.getHeldItem();
            boolean isUsing = player.isUsingItem();
            TrackedUse previous = usingItems.get(uuid);
            if (isUsing && heldItem != null && this.isTrackedItem(heldItem.getItem())) {
                if (previous != null && ItemStack.areItemStacksEqual((ItemStack)heldItem, (ItemStack)previous.item)) continue;
                usingItems.put(uuid, new TrackedUse(heldItem.copy(), (int)System.currentTimeMillis()));
                continue;
            }
            if (previous == null) continue;
            usingItems.remove(uuid);
            if (heldItem != null && ItemStack.areItemStacksEqual((ItemStack)heldItem, (ItemStack)previous.item)) continue;
            this.alert(player, previous.item);
        }
    }

    private boolean isTrackedItem(Item item) {
        return item == Items.golden_apple || item == Items.potionitem || item == Items.milk_bucket;
    }

    private void alert(EntityPlayer player, ItemStack item) {
        Long lastAlert;
        UUID uuid = player.getUniqueID();
        long now = System.currentTimeMillis();
        if (now - (lastAlert = lastAlertTime.getOrDefault(uuid, 0L)) < 1600L) {
            return;
        }
        lastAlertTime.put(uuid, now);
        String name = NameUtil.getTabDisplayName(player.getName());
        float distanceToEntity = player.getDistanceToEntity((Entity)this.mc.thePlayer);
        DecimalFormat df = new DecimalFormat("#");
        String distanceStr = df.format(distanceToEntity);
        String distance = cfg.v.consumeAlertsShowDistance ? EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + distanceStr + "m" + EnumChatFormatting.GRAY + ")" : "";
        Item heldItem = item.getItem();
        if (heldItem == Items.golden_apple && cfg.v.consumeAlerts_goldenapple) {
            Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.GOLD + "Golden Apple" + distance);
            if (cfg.v.notify && cfg.v.notifyConsumeAlerts) {
                NotifyUtil.notifyParty(player.getName() + " consumed Golden Apple");
            }
            this.ping();
        } else if (heldItem == Items.milk_bucket && cfg.v.consumeAlerts_milk) {
            Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.WHITE + "Milk" + distance);
            if (cfg.v.notify && cfg.v.notifyConsumeAlerts) {
                NotifyUtil.notifyParty(player.getName() + " consumed Milk");
            }
            this.ping();
        } else if (heldItem == Items.potionitem) {
            String lowerName = item.getDisplayName().toLowerCase();
            if (lowerName.contains("speed") && cfg.v.consumeAlerts_speed) {
                Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.YELLOW + "Speed Potion" + distance);
                if (cfg.v.notify && cfg.v.notifyConsumeAlerts) {
                    NotifyUtil.notifyParty(player.getName() + " consumed Speed Potion");
                }
                this.ping();
            } else if (lowerName.contains("jump") && cfg.v.consumeAlerts_jump) {
                Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.GREEN + "Jump Potion" + distance);
                if (cfg.v.notify && cfg.v.notifyConsumeAlerts) {
                    NotifyUtil.notifyParty(player.getName() + " consumed Jump Potion");
                }
                this.ping();
            } else if (lowerName.contains("invis") && cfg.v.consumeAlerts_invis) {
                Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.AQUA + "Invis Potion" + distance);
                if (cfg.v.notify && cfg.v.notifyConsumeAlerts) {
                    NotifyUtil.notifyParty(player.getName() + " consumed Invis Potion");
                }
                this.ping();
            }
        }
    }

    private void ping() {
        if (cfg.v.consumeAlertsSound) {
            PlaySound.getInstance().playPingSoundMedium();
        }
    }

    public static void clear() {
        usingItems.clear();
        lastAlertTime.clear();
    }

    private static class TrackedUse {
        final ItemStack item;
        final int startUseDuration;

        TrackedUse(ItemStack item, int useDuration) {
            this.item = item;
            this.startUseDuration = useDuration;
        }
    }
}
