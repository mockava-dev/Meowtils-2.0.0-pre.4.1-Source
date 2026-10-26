package wtf.tatp.meowtils.modules.bedwars;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.mixins.GuiContainerAccessor;
import wtf.tatp.meowtils.util.GamemodeUtil;

public class AutoChest
extends Module {
    private NumberValue waitDelay;
    private NumberValue delay;
    private BooleanValue renderClicked;
    private BooleanValue depositIron;
    private BooleanValue depositGold;
    private BooleanValue depositDiamond;
    private BooleanValue depositEmerald;
    public static final Set<Integer> clickedSlots = new HashSet<Integer>();
    private long nextClickTime = 0L;
    private boolean shouldDeposit = false;
    private int lastCheckedSlot = 0;

    public AutoChest() {
        super("AutoChest", "autoChestKey", "autoChest", Module.Category.Bedwars);
        this.tooltip("Automatically deposit resources into a chest.\n" + EnumChatFormatting.RED + "WARNING: " + EnumChatFormatting.RED + "Low " + EnumChatFormatting.RED + "delays " + EnumChatFormatting.RED + "are " + EnumChatFormatting.RED + "detectable.");
        this.delay = new NumberValue("Delay", 0.0, 500.0, 50.0, "ms", "autoChestDelay", Integer.TYPE);
        this.addValue(this.delay);
        this.waitDelay = new NumberValue("Wait delay", 0.0, 500.0, 50.0, "ms", "autoChestWaitDelay", Integer.TYPE);
        this.addValue(this.waitDelay);
        this.renderClicked = new BooleanValue("Render clicked", "autoChestRenderClicked");
        this.addBoolean(this.renderClicked);
        this.depositIron = new BooleanValue("\u00a77Iron Ingots", "autoChestIron");
        this.addBoolean(this.depositIron);
        this.depositGold = new BooleanValue("\u00a76Gold Ingots", "autoChestGold");
        this.addBoolean(this.depositGold);
        this.depositDiamond = new BooleanValue("\u00a7bDiamonds", "autoChestDiamond");
        this.addBoolean(this.depositDiamond);
        this.depositEmerald = new BooleanValue("\u00a72Emeralds", "autoChestEmerald");
        this.addBoolean(this.depositEmerald);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (!(event.gui instanceof GuiChest)) {
            this.shouldDeposit = false;
            return;
        }
        String localizedChest = I18n.format((String)"container.chest", (Object[])new Object[0]);
        String localizedEnderChest = I18n.format((String)"container.enderchest", (Object[])new Object[0]);
        int waitDelay = cfg.v.autoChestWaitDelay / 50;
        if (event.gui instanceof GuiChest) {
            ContainerChest container;
            IInventory containerName;
            GuiChest chest = (GuiChest)event.gui;
            if (chest.inventorySlots instanceof ContainerChest && (localizedChest.equals((containerName = (container = (ContainerChest)chest.inventorySlots).getLowerChestInventory()).getDisplayName().getUnformattedText()) || localizedEnderChest.equals(containerName.getDisplayName().getUnformattedText()))) {
                new DelayedTask(() -> {
                    this.shouldDeposit = true;
                    this.lastCheckedSlot = 0;
                }, waitDelay);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        long now;
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (!(mc.currentScreen instanceof GuiChest)) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (!this.shouldDeposit) {
            return;
        }
        if (cfg.v.autoChestRenderClicked) {
            clickedSlots.clear();
        }
        if ((now = System.currentTimeMillis()) >= this.nextClickTime) {
            this.nextClickTime = now + (long)cfg.v.autoChestDelay;
            boolean clicked = this.deposit((GuiContainer)mc.currentScreen);
            if (!clicked) {
                this.shouldDeposit = false;
            }
        }
    }

    private boolean deposit(GuiContainer gui) {
        Minecraft mc = Minecraft.getMinecraft();
        for (int i = this.lastCheckedSlot; i < gui.inventorySlots.inventorySlots.size(); ++i) {
            ItemStack stack;
            Slot slot = gui.inventorySlots.getSlot(i);
            if (slot == null || !slot.getHasStack() || slot.inventory != mc.thePlayer.inventory || (stack = slot.getStack()) == null || !this.allowedItem(stack.getItem())) continue;
            this.click(gui, slot, 0, 1);
            this.lastCheckedSlot = i + 1;
            return true;
        }
        return false;
    }

    private void click(GuiContainer gui, Slot slot, int mouseButton, int type) {
        ((GuiContainerAccessor)gui).clickSlot(slot, slot.slotNumber, mouseButton, type);
        if (cfg.v.autoChestRenderClicked) {
            clickedSlots.add(slot.slotNumber);
        }
    }

    private boolean allowedItem(Item item) {
        return cfg.v.autoChestIron && item == Items.iron_ingot || cfg.v.autoChestGold && item == Items.gold_ingot || cfg.v.autoChestDiamond && item == Items.diamond || cfg.v.autoChestEmerald && item == Items.emerald;
    }
}
