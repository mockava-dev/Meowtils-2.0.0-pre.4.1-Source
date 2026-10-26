package wtf.tatp.meowtils.modules.skywars;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.StripFormattingCodes;

public class SkywarsAlerts
extends Module {
    private ArrayValue soundMode;
    private BooleanValue diamondSword;
    private BooleanValue enderPearls;
    private BooleanValue fireAspectSword;
    private BooleanValue knockbackItems;
    private NumberValue cooldown;
    private BooleanValue showDistance;
    private final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<String, Map<String, Long>> cooldowns = new HashMap<String, Map<String, Long>>();

    public SkywarsAlerts() {
        super("SkywarsAlerts", "skywarsAlertsKey", "skywarsAlerts", Module.Category.Skywars);
        this.tooltip("Alerts you of items players have in Skywars.");
        this.cooldown = new NumberValue("Cooldown", 1.0, 30.0, 1.0, "s", "skywarsAlertsCooldown", Integer.TYPE);
        this.addValue(this.cooldown);
        this.soundMode = new ArrayValue("Ping sound", Arrays.asList("All", "Important", "None"), "skywarsAlertsSoundMode");
        this.addArray(this.soundMode);
        this.showDistance = new BooleanValue("Show distance", "skywarsAlertsDistance");
        this.addBoolean(this.showDistance);
        this.enderPearls = new BooleanValue("\u00a75Ender Pearls", "skywarsAlerts_EnderPearls");
        this.addBoolean(this.enderPearls);
        this.diamondSword = new BooleanValue("\u00a7bDiamond Sword", "skywarsAlerts_DiamondSword");
        this.addBoolean(this.diamondSword);
        this.fireAspectSword = new BooleanValue("\u00a7cFire Sword", "skywarsAlerts_FireAspectSword");
        this.addBoolean(this.fireAspectSword);
        this.knockbackItems = new BooleanValue("\u00a7eKnockback Items", "skywarsAlerts_KnockbackItems");
        this.addBoolean(this.knockbackItems);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (player == null || player == this.mc.thePlayer) continue;
            ItemStack held = player.getHeldItem();
            String itemName = null;
            if (held == null) continue;
            if (held.getItem() == Items.ender_pearl && held.hasEffect() && cfg.v.skywarsAlerts_EnderPearls) {
                itemName = held.getTooltip((EntityPlayer)this.mc.thePlayer, false).stream().anyMatch(s -> s.contains("Teleport back")) ? EnumChatFormatting.LIGHT_PURPLE + "Time Warp Pearl" : EnumChatFormatting.DARK_PURPLE + "Corrupt Pearl";
            } else if (held.getItem() == Items.ender_pearl && cfg.v.skywarsAlerts_EnderPearls) {
                itemName = EnumChatFormatting.DARK_PURPLE + "Ender Pearl";
            } else if (held.getItem() == Items.diamond_sword && cfg.v.skywarsAlerts_DiamondSword) {
                itemName = EnumChatFormatting.AQUA + "Diamond Sword";
            } else if (held.getItem() == Items.iron_sword && held.hasEffect() && cfg.v.skywarsAlerts_FireAspectSword) {
                itemName = EnumChatFormatting.RED + "Fire Sword";
            } else if (held.getItem() == Items.fishing_rod && held.hasEffect() && cfg.v.skywarsAlerts_KnockbackItems) {
                if (held.getEnchantmentTagList().tagCount() == 1) {
                    itemName = EnumChatFormatting.GOLD + "Knockback Rod";
                }
            } else if (held.getItem() == Items.wooden_sword && held.hasEffect() && cfg.v.skywarsAlerts_KnockbackItems) {
                itemName = EnumChatFormatting.YELLOW + "Knockback Sword";
            }
            if (itemName == null || this.hasCooldown(player.getName(), itemName)) continue;
            this.alert(player, itemName);
            this.setCooldown(player.getName(), itemName);
        }
    }

    private void alert(EntityPlayer player, String itemName) {
        String showDistance;
        float distanceToEntity = player.getDistanceToEntity((Entity)this.mc.thePlayer);
        DecimalFormat df = new DecimalFormat("#");
        String distance = df.format(distanceToEntity);
        String rawItemName = StripFormattingCodes.stripFormattingCodes(itemName).toLowerCase();
        String string = showDistance = cfg.v.skywarsAlertsDistance ? EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + distance + "m" + EnumChatFormatting.GRAY + ")" : "";
        if (!this.hasCooldown(player.getName(), itemName)) {
            Meowtils.addMessage(NameUtil.getTabDisplayName(player.getName()) + EnumChatFormatting.GRAY + " has " + itemName + showDistance);
            this.setCooldown(player.getName(), itemName);
        }
        if (cfg.v.skywarsAlertsSoundMode.equals("All")) {
            this.sound();
        } else if (cfg.v.skywarsAlertsSoundMode.equals("Important") && rawItemName.equalsIgnoreCase("ender pearl") || rawItemName.equalsIgnoreCase("diamond sword") || rawItemName.equalsIgnoreCase("knockback rod")) {
            this.sound();
        }
    }

    private boolean hasCooldown(String playerName, String itemName) {
        long ALERT_COOLDOWN = (long)cfg.v.skywarsAlertsCooldown * 1000L;
        Map<String, Long> playerCooldowns = cooldowns.get(playerName);
        if (playerCooldowns == null) {
            return false;
        }
        Long lastTime = playerCooldowns.get(itemName);
        return lastTime != null && System.currentTimeMillis() - lastTime < ALERT_COOLDOWN;
    }

    private void setCooldown(String playerName, String itemName) {
        cooldowns.computeIfAbsent(playerName, k -> new HashMap()).put(itemName, System.currentTimeMillis());
    }

    private void sound() {
        PlaySound.getInstance().playPingSound();
    }

    public static void clear() {
        cooldowns.clear();
    }
}
