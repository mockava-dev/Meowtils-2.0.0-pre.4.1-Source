package wtf.tatp.meowtils.modules.advanced;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.NumberValue;

public class InventoryMove
extends Module {
    private NumberValue clickDelay;
    public static boolean clickedItem;

    public InventoryMove() {
        super("InventoryMove", "inventoryMoveKey", "inventoryMove", Module.Category.Advanced);
        this.tooltip("Allows you to move while in your inventory, attempts to not use illegitimate methods.\n\u00a7cWARNING: \u00a7cLow \u00a7cdelay \u00a7cmight \u00a7cflag. \u00a7cMay \u00a7calso \u00a7cnot \u00a7cwork \u00a7cwith \u00a7csome \u00a7cclient \u00a7cInventoryManagers.");
        this.clickDelay = new NumberValue("Click delay", 0.0, 4.0, 1.0, "ticks", "inventoryMoveClickDelay", Integer.TYPE);
        this.addValue(this.clickDelay);
    }

    public static boolean isMovementKey(KeyBinding key) {
        Minecraft mc = Minecraft.getMinecraft();
        return key == mc.gameSettings.keyBindForward || key == mc.gameSettings.keyBindBack || key == mc.gameSettings.keyBindLeft || key == mc.gameSettings.keyBindRight || key == mc.gameSettings.keyBindJump;
    }

    public static boolean isInSurvivalInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.currentScreen instanceof GuiInventory && !mc.playerController.isInCreativeMode();
    }
}
