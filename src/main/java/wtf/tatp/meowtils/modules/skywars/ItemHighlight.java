package wtf.tatp.meowtils.modules.skywars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class ItemHighlight
extends Module {
    private static Set<String> blacklist = new HashSet<String>();
    private static Set<String> safelist = new HashSet<String>();
    private static final File BLACKLIST_FILE = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/items/itemblacklist.json");
    private static final File SAFELIST_FILE = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/items/itemsafelist.json");
    private BooleanValue skywarsOnly;
    private BooleanValue bestOnly;
    public static final List<String> DEFAULT_BLACKLIST = Arrays.asList("minecraft:chest", "minecraft:double_plant", "minecraft:feather", "minecraft:fireworks", "minecraft:glass_bottle", "minecraft:gunpowder", "minecraft:jukebox", "minecraft:leather", "minecraft:lever", "minecraft:magma_cream", "minecraft:noteblock", "minecraft:prismarine_crystals", "minecraft:prismarine_shard", "minecraft:rabbit_foot", "minecraft:rabbit_hide", "minecraft:record_11", "minecraft:record_13", "minecraft:record_blocks", "minecraft:record_cat", "minecraft:record_chirp", "minecraft:record_far", "minecraft:record_mall", "minecraft:record_mellohi", "minecraft:record_stal", "minecraft:record_strad", "minecraft:record_wait", "minecraft:record_ward", "minecraft:red_flower", "minecraft:redstone", "minecraft:redstone_torch", "minecraft:repeater", "minecraft:rotten_flesh", "minecraft:saddle", "minecraft:sand", "minecraft:sapling", "minecraft:spider_eye", "minecraft:stone_button", "minecraft:stone_pressure_plate", "minecraft:string", "minecraft:torch", "minecraft:tripwire_hook", "minecraft:waterlily", "minecraft:wheat", "minecraft:wooden_button", "minecraft:wooden_pressure_plate", "minecraft:yellow_flower", "minecraft:carrot_on_a_stick", "minecraft:cactus");
    public static final List<String> DEFAULT_SAFELIST = Arrays.asList("minecraft:arrow", "minecraft:bow", "minecraft:chainmail_boots", "minecraft:chainmail_chestplate", "minecraft:chainmail_helmet", "minecraft:chainmail_leggings", "minecraft:clock", "minecraft:diamond", "minecraft:diamond_axe", "minecraft:diamond_block", "minecraft:diamond_boots", "minecraft:diamond_chestplate", "minecraft:diamond_helmet", "minecraft:diamond_leggings", "minecraft:diamond_sword", "minecraft:ender_pearl", "minecraft:fishing_rod", "minecraft:golden_apple", "minecraft:golden_axe", "minecraft:golden_boots", "minecraft:golden_chestplate", "minecraft:golden_helmet", "minecraft:golden_leggings", "minecraft:golden_sword", "minecraft:iron_axe", "minecraft:iron_boots", "minecraft:iron_chestplate", "minecraft:iron_helmet", "minecraft:iron_leggings", "minecraft:iron_sword", "minecraft:leather_boots", "minecraft:leather_chestplate", "minecraft:leather_helmet", "minecraft:leather_leggings", "minecraft:snowball", "minecraft:stone_axe", "minecraft:stone_sword", "minecraft:wooden_axe", "minecraft:wooden_sword", "minecraft:egg");

    public ItemHighlight() {
        super("ItemHighlight", "itemHighlightKey", "itemHighlight", Module.Category.Skywars);
        this.tooltip("Highlights important items in your inventory.\n" + EnumChatFormatting.RED + "BETA");
        this.skywarsOnly = new BooleanValue("Skywars only", "itemHighlightSkywarsOnly");
        this.addBoolean(this.skywarsOnly);
        this.bestOnly = new BooleanValue("Highlight best only", "itemHighlightBestOnly");
        this.addBoolean(this.bestOnly);
        this.loadItemList();
    }

    private void loadItemList() {
        Gson gson = new Gson();
        try {
            List loadedBlacklist;
            if (!BLACKLIST_FILE.exists()) {
                this.saveItemList(BLACKLIST_FILE, DEFAULT_BLACKLIST);
            }
            if (!SAFELIST_FILE.exists()) {
                this.saveItemList(SAFELIST_FILE, DEFAULT_SAFELIST);
            }
            blacklist = (loadedBlacklist = (List)gson.fromJson((Reader)new FileReader(BLACKLIST_FILE), new TypeToken<List<String>>(){}.getType())) != null ? new HashSet<String>(loadedBlacklist) : new HashSet();
            List loadedSafelist = (List)gson.fromJson((Reader)new FileReader(SAFELIST_FILE), new TypeToken<List<String>>(){}.getType());
            safelist = loadedSafelist != null ? new HashSet<String>(loadedSafelist) : new HashSet();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveItemList(File file, List<String> list) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file);){
            gson.toJson(list, (Appendable)writer);
        }
    }

    public static Set<String> getItemBlacklist() {
        return blacklist;
    }

    public static Set<String> getItemSafelist() {
        return safelist;
    }

    public static boolean shouldHighlight(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        String id = ((ResourceLocation)Item.itemRegistry.getNameForObject(stack.getItem())).toString();
        if (blacklist.contains(id)) {
            return false;
        }
        if (!safelist.contains(id)) {
            return false;
        }
        if (!cfg.v.itemHighlightBestOnly) {
            return true;
        }
        Item item = stack.getItem();
        if (item instanceof ItemSword || item instanceof ItemAxe) {
            return ItemHighlight.isBestMelee(stack);
        }
        if (item instanceof ItemArmor) {
            return ItemHighlight.isBestArmor((ItemArmor)item, stack);
        }
        if (item instanceof ItemBow) {
            return ItemHighlight.isBestBow(stack);
        }
        return true;
    }

    private static boolean isBestMelee(ItemStack candidate) {
        boolean swordWins;
        double EPS = 1.0E-6;
        boolean isSword = candidate.getItem() instanceof ItemSword;
        boolean isAxe = candidate.getItem() instanceof ItemAxe;
        if (!isSword && !isAxe) {
            return false;
        }
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack equipped = mc.thePlayer.getHeldItem();
        double bestSword = 0.0;
        double bestAxe = 0.0;
        ItemStack bestSwordStack = null;
        ItemStack bestAxeStack = null;
        for (ItemStack stack : ItemHighlight.getStacks()) {
            if (stack == null) continue;
            Item item = stack.getItem();
            double score = ItemHighlight.getMeleeDamage(stack);
            if (item instanceof ItemSword) {
                if (!(score > bestSword + 1.0E-6)) continue;
                bestSword = score;
                bestSwordStack = stack;
                continue;
            }
            if (!(item instanceof ItemAxe) || !(score > bestAxe + 1.0E-6)) continue;
            bestAxe = score;
            bestAxeStack = stack;
        }
        boolean bl = swordWins = bestSword >= bestAxe - 1.0E-6;
        if (equipped != null) {
            Item eqItem = equipped.getItem();
            if (isSword && eqItem instanceof ItemSword && Math.abs(ItemHighlight.getMeleeDamage(equipped) - bestSword) < 1.0E-6 || isAxe && eqItem instanceof ItemAxe && Math.abs(ItemHighlight.getMeleeDamage(equipped) - bestAxe) < 1.0E-6) {
                return candidate == equipped;
            }
        }
        if (isSword) {
            return candidate == bestSwordStack && swordWins;
        }
        return candidate == bestAxeStack && !swordWins;
    }

    private static boolean isBestBow(ItemStack candidate) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack equipped = mc.thePlayer.getHeldItem();
        double bestScore = 0.0;
        ItemStack bestStack = null;
        for (ItemStack stack : ItemHighlight.getStacks()) {
            double score;
            if (stack == null || !(stack.getItem() instanceof ItemBow) || !((score = ItemHighlight.getBowDamage(stack)) > bestScore)) continue;
            bestScore = score;
            bestStack = stack;
        }
        if (equipped != null && equipped.getItem() instanceof ItemBow && Math.abs(ItemHighlight.getBowDamage(equipped) - bestScore) < 1.0E-6) {
            return candidate == equipped;
        }
        return candidate == bestStack;
    }

    private static boolean isBestArmor(ItemArmor candidateArmor, ItemStack candidate) {
        double eqScore;
        int armorType = candidateArmor.armorType;
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack equipped = mc.thePlayer.getCurrentArmor(3 - armorType);
        double bestScore = 0.0;
        ItemStack bestStack = null;
        for (ItemStack stack : ItemHighlight.getStacks()) {
            double score;
            if (stack == null || !(stack.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)stack.getItem();
            if (armor.armorType != armorType || !((score = ItemHighlight.getArmorValue(stack)) > bestScore)) continue;
            bestScore = score;
            bestStack = stack;
        }
        if (equipped != null && equipped.getItem() instanceof ItemArmor && (eqScore = ItemHighlight.getArmorValue(equipped)) >= bestScore - 1.0E-6) {
            return candidate == equipped;
        }
        return candidate == bestStack;
    }

    private static double getMeleeDamage(ItemStack stack) {
        Item item = stack.getItem();
        double base = 0.0;
        if (item == Items.wooden_sword || item == Items.golden_sword) {
            base = 4.0;
        } else if (item == Items.stone_sword) {
            base = 5.0;
        } else if (item == Items.iron_sword) {
            base = 6.0;
        } else if (item == Items.diamond_sword) {
            base = 7.0;
        } else if (item == Items.wooden_axe || item == Items.golden_axe) {
            base = 3.0;
        } else if (item == Items.stone_axe) {
            base = 4.0;
        } else if (item == Items.iron_axe) {
            base = 5.0;
        } else if (item == Items.diamond_axe) {
            base = 6.0;
        } else if (item instanceof ItemSword) {
            base = 4.0;
        } else if (item instanceof ItemAxe) {
            base = 3.0;
        }
        int sharp = EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)stack);
        return base + (double)sharp * 1.25;
    }

    private static double getBowDamage(ItemStack stack) {
        double base = 5.0;
        int power = EnchantmentHelper.getEnchantmentLevel((int)Enchantment.power.effectId, (ItemStack)stack);
        return base + (double)power * 0.5;
    }

    private static double getArmorValue(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor)) {
            return 0.0;
        }
        ItemArmor armor = (ItemArmor)stack.getItem();
        double base = armor.damageReduceAmount;
        int prot = EnchantmentHelper.getEnchantmentLevel((int)Enchantment.protection.effectId, (ItemStack)stack);
        return base + (double)prot * 0.75;
    }

    public static boolean isBestItem(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof ItemSword || item instanceof ItemAxe) {
            return ItemHighlight.isBestMelee(stack);
        }
        if (item instanceof ItemArmor) {
            return ItemHighlight.isBestArmor((ItemArmor)item, stack);
        }
        if (item instanceof ItemBow) {
            return ItemHighlight.isBestBow(stack);
        }
        return false;
    }

    public static List<ItemStack> getStacks() {
        ItemStack cursor;
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        Minecraft mc = Minecraft.getMinecraft();
        Collections.addAll(stacks, mc.thePlayer.inventory.mainInventory);
        Collections.addAll(stacks, mc.thePlayer.inventory.armorInventory);
        if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer != mc.thePlayer.inventoryContainer) {
            for (Object obj : mc.thePlayer.openContainer.inventorySlots) {
                ItemStack stack;
                if (!(obj instanceof Slot) || (stack = ((Slot)obj).getStack()) == null) continue;
                stacks.add(stack);
            }
        }
        if ((cursor = mc.thePlayer.inventory.getItemStack()) != null) {
            stacks.add(cursor);
        }
        return stacks;
    }
}
