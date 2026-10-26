package wtf.tatp.meowtils.modules.utility;

import java.util.Arrays;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class AutoSwap
extends Module {
    private Item lastItem;
    private int lastSlot = -1;
    private BooleanValue blocks;
    private BooleanValue projectiles;
    private BooleanValue pearls;
    private BooleanValue swords;
    private BooleanValue tools;
    private BooleanValue resources;
    private final List<String> ALLOWED_BLOCKS = Arrays.asList("stone", "grass", "dirt", "planks", "wool", "wood", "glass", "leaves", "clay", "cloth");
    private final List<String> PROJECTILES = Arrays.asList("egg", "snowball");
    private final List<String> PEARLS = Arrays.asList("pearl");
    private final List<String> SWORDS = Arrays.asList("sword");
    private final List<String> TOOLS = Arrays.asList("rod", "pickaxe", "axe", "shovel", "hoe", "flint_and_steel");
    private final List<String> RESOURCES = Arrays.asList("265", "266", "388", "264");

    public AutoSwap() {
        super("AutoSwap", "autoSwapKey", "autoSwap", Module.Category.Utility);
        this.tooltip("Automatically swap when running out of certain items.\n" + EnumChatFormatting.RED + "BETA");
        this.blocks = new BooleanValue("\u00a77Blocks", "autoSwap_blocks");
        this.addBoolean(this.blocks);
        this.projectiles = new BooleanValue("\u00a76Projectiles", "autoSwap_projectiles");
        this.addBoolean(this.projectiles);
        this.resources = new BooleanValue("\u00a72Resources", "autoSwap_resources");
        this.addBoolean(this.resources);
        this.pearls = new BooleanValue("\u00a75Pearls", "autoSwap_pearls");
        this.addBoolean(this.pearls);
        this.swords = new BooleanValue("\u00a73Swords", "autoSwap_swords");
        this.addBoolean(this.swords);
        this.tools = new BooleanValue("\u00a7eTools", "autoSwap_tools");
        this.addBoolean(this.tools);
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        int slot = this.mc.thePlayer.inventory.currentItem;
        ItemStack held = this.mc.thePlayer.inventory.getStackInSlot(slot);
        if (this.lastItem != null && slot == this.lastSlot && (held == null || held.stackSize < 1)) {
            this.swapItem(this.lastItem);
        }
        this.lastItem = held != null ? held.getItem() : null;
        this.lastSlot = slot;
    }

    private void swapItem(Item lastItem) {
        if (lastItem == null) {
            return;
        }
        int itemId = Item.getIdFromItem((Item)lastItem);
        String idString = String.valueOf(itemId);
        String lastId = lastItem.getUnlocalizedName().toLowerCase();
        boolean isBlock = lastItem instanceof ItemBlock;
        int current = this.mc.thePlayer.inventory.currentItem;
        List<String> category = null;
        if (!isBlock) {
            if (this.PROJECTILES.stream().anyMatch(lastId::contains) && !lastId.contains("leggings") && cfg.v.autoSwap_projectiles) {
                category = this.PROJECTILES;
            } else if (this.PEARLS.stream().anyMatch(lastId::contains) && cfg.v.autoSwap_pearls) {
                category = this.PEARLS;
            } else if (this.SWORDS.stream().anyMatch(lastId::contains) && cfg.v.autoSwap_swords) {
                category = this.SWORDS;
            } else if (this.TOOLS.stream().anyMatch(lastId::contains) && cfg.v.autoSwap_tools) {
                category = this.TOOLS;
            } else if (this.RESOURCES.stream().anyMatch(idString::contains) && cfg.v.autoSwap_resources) {
                category = this.RESOURCES;
            } else {
                return;
            }
        }
        for (int offset = 1; offset <= 9; ++offset) {
            int i = (current + offset) % 9;
            ItemStack stack = this.mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null || stack.stackSize < 1) continue;
            Item item = stack.getItem();
            String id = item.getUnlocalizedName().toLowerCase();
            String numericId = String.valueOf(Item.getIdFromItem((Item)item));
            if (item == lastItem) {
                this.mc.thePlayer.inventory.currentItem = i;
                return;
            }
            if (isBlock && cfg.v.autoSwap_blocks && this.isValidBlock(stack)) {
                this.mc.thePlayer.inventory.currentItem = i;
                return;
            }
            if (category == null) continue;
            if (!category.stream().anyMatch(id::contains)) {
                if (!category.stream().anyMatch(numericId::contains)) continue;
            }
            if (id.contains("leggings")) continue;
            this.mc.thePlayer.inventory.currentItem = i;
            return;
        }
    }

    private boolean isValidBlock(ItemStack stack) {
        if (!cfg.v.autoSwap_blocks) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemBlock)) {
            return false;
        }
        String id = stack.getItem().getUnlocalizedName().toLowerCase();
        return this.ALLOWED_BLOCKS.stream().anyMatch(id::contains);
    }
}
