package wtf.tatp.meowtils.modules.bedwars;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.mixins.GuiContainerAccessor;

public class AutoClaim
extends Module {
    public static boolean autoClaimPending;
    private NumberValue delay;

    public AutoClaim() {
        super("AutoClaim", "autoClaimKey", "autoClaim", Module.Category.Bedwars);
        this.tooltip("Automatically accepts when claiming tickets from\nSlumber Hotel NPC's.");
        this.delay = new NumberValue("Delay", 100.0, 1000.0, 50.0, "ms", "autoClaimDelay", Integer.TYPE);
        this.addValue(this.delay);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        int clickDelay = cfg.v.autoClaimDelay / 50;
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest)event.gui;
            if (chest.inventorySlots instanceof ContainerChest) {
                ContainerChest container = (ContainerChest)chest.inventorySlots;
                IInventory containerName = container.getLowerChestInventory();
                if ("Item Submission".equals(containerName.getDisplayName().getUnformattedText())) {
                    autoClaimPending = true;
                    new DelayedTask(() -> {
                        if (mc.currentScreen instanceof GuiChest) {
                            Slot slot = container.getSlot(11);
                            ((GuiContainerAccessor)chest).clickSlot(slot, slot.slotNumber, 0, 0);
                            autoClaimPending = false;
                        }
                    }, clickDelay);
                } else if ("Trade Emeralds".equals(containerName.getDisplayName().getUnformattedText())) {
                    new DelayedTask(() -> {
                        autoClaimPending = true;
                        if (mc.currentScreen instanceof GuiChest) {
                            Slot slot = container.getSlot(22);
                            ((GuiContainerAccessor)chest).clickSlot(slot, slot.slotNumber, 0, 0);
                            autoClaimPending = false;
                        }
                    }, clickDelay);
                }
            }
        }
    }
}
