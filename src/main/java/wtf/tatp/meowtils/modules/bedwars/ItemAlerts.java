package wtf.tatp.meowtils.modules.bedwars;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import wtf.tatp.meowtils.util.ColorUtil;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.StripFormattingCodes;
import wtf.tatp.meowtils.util.TeamUtil;

public class ItemAlerts
extends Module {
    private BooleanValue potions;
    private BooleanValue bows;
    private BooleanValue pickaxes;
    private BooleanValue mobs;
    private BooleanValue common;
    private BooleanValue important;
    private BooleanValue explosives;
    private BooleanValue rotation;
    private NumberValue cooldown;
    private ArrayValue soundMode;
    private ArrayValue distanceMode;
    private final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<String, Map<String, Long>> cooldowns = new HashMap<String, Map<String, Long>>();

    public ItemAlerts() {
        super("ItemAlerts", "itemAlertsKey", "itemAlerts", Module.Category.Bedwars);
        this.tooltip("Alerts in chat when a player holds a specific item.");
        this.soundMode = new ArrayValue("Ping sound", Arrays.asList("All", "Important", "None"), "itemAlertsSoundMode");
        this.addArray(this.soundMode);
        this.distanceMode = new ArrayValue("Distance", Arrays.asList("All", "Important", "None"), "itemAlertsDistanceMode");
        this.addArray(this.distanceMode);
        this.cooldown = new NumberValue("Cooldown", 1.0, 30.0, 1.0, "s", "itemAlertsCooldown", Integer.TYPE);
        this.addValue(this.cooldown);
        this.common = new BooleanValue("\u00a77Common Items", "itemAlerts_commonItems");
        this.addBoolean(this.common);
        this.important = new BooleanValue("\u00a7dImportant Items", "itemAlerts_importantItems");
        this.addBoolean(this.important);
        this.rotation = new BooleanValue("\u00a73Rotation Items", "itemAlerts_rotationItems");
        this.addBoolean(this.rotation);
        this.potions = new BooleanValue("\u00a7ePotions", "itemAlerts_potions");
        this.addBoolean(this.potions);
        this.pickaxes = new BooleanValue("\u00a7bPickaxes", "itemAlerts_pickaxes");
        this.addBoolean(this.pickaxes);
        this.explosives = new BooleanValue("\u00a7cExplosives", "itemAlerts_explosives");
        this.addBoolean(this.explosives);
        this.bows = new BooleanValue("\u00a76Bows", "itemAlerts_bows");
        this.addBoolean(this.bows);
        this.mobs = new BooleanValue("Mobs", "itemAlerts_mobs");
        this.addBoolean(this.mobs);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (player == null || player == this.mc.thePlayer || TeamUtil.ignoreTeam(player.getName())) continue;
            if (TeamUtil.inSpectator()) {
                return;
            }
            ItemStack held = player.getHeldItem();
            String itemName = null;
            if (held == null) continue;
            if (cfg.v.itemAlerts_commonItems) {
                if (held.getItem() == Items.iron_sword) {
                    itemName = EnumChatFormatting.WHITE + "Iron Sword";
                } else if (held.getItem() == Items.golden_apple) {
                    itemName = EnumChatFormatting.GOLD + "Golden Apple";
                }
            }
            if (cfg.v.itemAlerts_importantItems) {
                if (held.getItem() == Items.diamond_sword) {
                    itemName = EnumChatFormatting.AQUA + "Diamond Sword";
                } else if (held.getItem() == Items.stick) {
                    itemName = EnumChatFormatting.GOLD + "Knockback Stick";
                } else if (held.getItem() == Items.water_bucket) {
                    itemName = EnumChatFormatting.BLUE + "Water Bucket";
                } else if (held.getItem() == Items.ender_pearl && !held.hasEffect()) {
                    itemName = EnumChatFormatting.DARK_PURPLE + "Ender Pearl";
                } else if (held.getItem() == Items.egg) {
                    itemName = EnumChatFormatting.DARK_AQUA + "Bridge Egg";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.obsidian)) {
                    itemName = EnumChatFormatting.DARK_GRAY + "Obsidian";
                }
            }
            if (cfg.v.itemAlerts_potions) {
                if (held.getItem() == Items.milk_bucket) {
                    itemName = EnumChatFormatting.WHITE + "Milk";
                } else if (held.getDisplayName().toLowerCase().contains("jump")) {
                    itemName = EnumChatFormatting.GREEN + "Jump Potion";
                } else if (held.getDisplayName().toLowerCase().contains("speed")) {
                    itemName = EnumChatFormatting.YELLOW + "Speed Potion";
                } else if (held.getDisplayName().toLowerCase().contains("invis")) {
                    itemName = EnumChatFormatting.AQUA + "Invis Potion";
                }
            }
            if (cfg.v.itemAlerts_pickaxes) {
                if (held.getItem() == Items.diamond_pickaxe) {
                    itemName = EnumChatFormatting.AQUA + "Diamond Pickaxe";
                } else if (held.getItem() == Items.golden_pickaxe) {
                    itemName = EnumChatFormatting.GOLD + "Golden Pickaxe";
                }
            }
            if (cfg.v.itemAlerts_explosives) {
                if (held.getItem() == Items.fire_charge) {
                    itemName = EnumChatFormatting.RED + "Fireball";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.tnt) && !held.hasEffect()) {
                    itemName = EnumChatFormatting.RED + "T" + EnumChatFormatting.WHITE + "N" + EnumChatFormatting.RED + "T";
                }
            }
            if (cfg.v.itemAlerts_bows) {
                if (held.getItem() == Items.bow && held.hasEffect()) {
                    itemName = EnumChatFormatting.GOLD + "Enchanted Bow";
                } else if (held.getItem() == Items.bow) {
                    itemName = EnumChatFormatting.GOLD + "Bow";
                }
            }
            if (cfg.v.itemAlerts_mobs) {
                if (held.getItem() == Items.snowball && !held.hasEffect()) {
                    itemName = EnumChatFormatting.WHITE + "Bedbug";
                } else if (held.getItem() == Items.spawn_egg && held.getMetadata() != 93) {
                    itemName = EnumChatFormatting.WHITE + "Iron Golem";
                }
            }
            if (cfg.v.itemAlerts_rotationItems) {
                if (held.getItem() == Items.nether_star) {
                    itemName = EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.UNDERLINE + "Shuriken";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.ice) && held.hasEffect()) {
                    itemName = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.UNDERLINE + "Ice Bridge";
                } else if (held.getItem() == Items.flint) {
                    itemName = EnumChatFormatting.DARK_BLUE.toString() + EnumChatFormatting.UNDERLINE + "Bridge Zapper";
                } else if (held.getItem() == Items.golden_horse_armor) {
                    itemName = EnumChatFormatting.GOLD.toString() + EnumChatFormatting.UNDERLINE + "Charlie the Unicorn";
                } else if (held.getItem() == Items.ender_pearl && held.hasEffect()) {
                    itemName = EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.UNDERLINE + "Time Warp Pearl";
                } else if (held.getItem() == Items.cookie) {
                    itemName = EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE + "Sugar Cookie";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.web)) {
                    itemName = EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Cobweb";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.redstone_torch)) {
                    itemName = EnumChatFormatting.DARK_GREEN.toString() + EnumChatFormatting.UNDERLINE + "Teleportation Device";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.tnt) && held.hasEffect()) {
                    itemName = EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.UNDERLINE + "Mega " + EnumChatFormatting.RED + "T" + EnumChatFormatting.WHITE + "N" + EnumChatFormatting.RED + "T";
                } else if (held.getItem() == Items.stone_shovel) {
                    itemName = EnumChatFormatting.GOLD.toString() + EnumChatFormatting.UNDERLINE + "Mace";
                } else if (held.getItem() == Items.snowball && held.hasEffect()) {
                    itemName = EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Wind Charge";
                } else if (held.getItem() == Items.shears && held.hasEffect()) {
                    itemName = EnumChatFormatting.GREEN.toString() + EnumChatFormatting.UNDERLINE + "Enchanted Shears";
                } else if (held.getItem() == Item.getItemFromBlock((Block)Blocks.beacon) && !held.hasEffect()) {
                    itemName = EnumChatFormatting.RED.toString() + EnumChatFormatting.UNDERLINE + "Final Revive Beacon";
                } else if (held.getItem() == Items.prismarine_shard) {
                    itemName = EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE + "Block Zapper";
                }
            }
            if (itemName == null || this.hasCooldown(player.getName(), itemName)) continue;
            this.alert(player, itemName);
            this.setCooldown(player.getName(), itemName);
        }
    }

    private void alert(EntityPlayer player, String itemName) {
        float distanceToEntity = player.getDistanceToEntity((Entity)this.mc.thePlayer);
        DecimalFormat df = new DecimalFormat("#");
        String distanceFormatted = df.format(distanceToEntity);
        String distance = EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + distanceFormatted + "m" + EnumChatFormatting.GRAY + ")";
        String rawItemName = StripFormattingCodes.stripFormattingCodes(itemName).toLowerCase();
        String showDistance = cfg.v.itemAlertsDistanceMode.equals("All") ? distance : (cfg.v.itemAlertsDistanceMode.equals("Important") && (rawItemName.equalsIgnoreCase("fireball") || rawItemName.equalsIgnoreCase("ender pearl") || rawItemName.equalsIgnoreCase("bridge egg") || rawItemName.equalsIgnoreCase("speed potion") || rawItemName.equalsIgnoreCase("jump potion")) || rawItemName.equalsIgnoreCase("invis potion") || rawItemName.equalsIgnoreCase("charlie the unicorn") ? distance : "");
        if (!this.hasCooldown(player.getName(), itemName)) {
            Meowtils.addMessage(NameUtil.getTabDisplayName(player.getName()) + EnumChatFormatting.GRAY + " has " + itemName + showDistance);
            this.setCooldown(player.getName(), itemName);
        }
        if (cfg.v.notify && cfg.v.notifyItemAlerts) {
            NotifyUtil.notifyParty(player.getName() + " has " + ColorUtil.unformattedText(itemName));
        }
        if (cfg.v.itemAlertsSoundMode.equals("All")) {
            this.sound();
        } else if (cfg.v.itemAlertsSoundMode.equals("Important") && rawItemName.equalsIgnoreCase("jump potion") || rawItemName.equalsIgnoreCase("speed potion") || rawItemName.equalsIgnoreCase("invis potion") || rawItemName.equalsIgnoreCase("bridge egg") || rawItemName.equalsIgnoreCase("diamond sword") || rawItemName.equalsIgnoreCase("iron golem") || rawItemName.equalsIgnoreCase("bedbug") || rawItemName.equalsIgnoreCase("charlie the unicorn") || rawItemName.equalsIgnoreCase("diamond pickaxe") || rawItemName.equalsIgnoreCase("enchanted bow") || rawItemName.equalsIgnoreCase("milk") || rawItemName.equalsIgnoreCase("ender pearl")) {
            this.sound();
        }
    }

    private boolean hasCooldown(String playerName, String itemName) {
        long ALERT_COOLDOWN = (long)cfg.v.itemAlertsCooldown * 1000L;
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
