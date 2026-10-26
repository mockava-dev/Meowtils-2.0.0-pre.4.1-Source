package wtf.tatp.meowtils.modules.antisnipe;

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

public class AutoReport
extends Module {
    private NumberValue delay;

    public AutoReport() {
        super("AutoReport", "autoReportKey", "autoReport", Module.Category.Antisnipe);
        this.tooltip("Automatically confirms a report on Hypixel.");
        this.delay = new NumberValue("Delay", 100.0, 1000.0, 50.0, "ms", "autoReportClickDelay", Integer.TYPE);
        this.addValue(this.delay);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest)event.gui;
            if (chest.inventorySlots instanceof ContainerChest) {
                int clickDelay = cfg.v.autoReportClickDelay / 50;
                ContainerChest container = (ContainerChest)chest.inventorySlots;
                IInventory lower = container.getLowerChestInventory();
                if (lower.getDisplayName().getUnformattedText().contains("Report")) {
                    new DelayedTask(() -> {
                        if (this.mc.currentScreen instanceof GuiChest) {
                            Slot slot = container.getSlot(11);
                            ((GuiContainerAccessor)chest).clickSlot(slot, slot.slotNumber, 0, 0);
                        }
                    }, clickDelay);
                }
            }
        }
    }
}
