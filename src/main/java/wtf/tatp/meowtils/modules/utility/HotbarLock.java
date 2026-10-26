package wtf.tatp.meowtils.modules.utility;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class HotbarLock
extends Module {
    private BooleanValue slot1;
    private BooleanValue slot2;
    private BooleanValue slot3;
    private BooleanValue slot4;
    private BooleanValue slot5;
    private BooleanValue slot6;
    private BooleanValue slot7;
    private BooleanValue slot8;
    private BooleanValue slot9;
    private ArrayValue mode;

    public HotbarLock() {
        super("HotbarLock", "hotbarLockKey", "hotbarLock", Module.Category.Utility);
        this.tooltip("Lock slots in your hotbar.");
        this.slot1 = new BooleanValue("Slot 1", "hotbarLock_slot1");
        this.addBoolean(this.slot1);
        this.slot2 = new BooleanValue("Slot 2", "hotbarLock_slot2");
        this.addBoolean(this.slot2);
        this.slot3 = new BooleanValue("Slot 3", "hotbarLock_slot3");
        this.addBoolean(this.slot3);
        this.slot4 = new BooleanValue("Slot 4", "hotbarLock_slot4");
        this.addBoolean(this.slot4);
        this.slot5 = new BooleanValue("Slot 5", "hotbarLock_slot5");
        this.addBoolean(this.slot5);
        this.slot6 = new BooleanValue("Slot 6", "hotbarLock_slot6");
        this.addBoolean(this.slot6);
        this.slot7 = new BooleanValue("Slot 7", "hotbarLock_slot7");
        this.addBoolean(this.slot7);
        this.slot8 = new BooleanValue("Slot 8", "hotbarLock_slot8");
        this.addBoolean(this.slot8);
        this.slot9 = new BooleanValue("Slot 9", "hotbarLock_slot9");
        this.addBoolean(this.slot9);
        this.mode = new ArrayValue("Mode", Arrays.asList("Manual", "Swords"), "hotbarLockMode");
        this.addArray(this.mode);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null) {
            return;
        }
        if (Keyboard.isKeyDown((int)mc.gameSettings.keyBindDrop.getKeyCode()) && cfg.v.hotbarLockMode.equals("Manual") && HotbarLock.isSlotLocked(mc.thePlayer.inventory.currentItem + 1)) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Prevented you from dropping item in locked slot!");
        } else if (Keyboard.isKeyDown((int)mc.gameSettings.keyBindDrop.getKeyCode()) && cfg.v.hotbarLockMode.equals("Swords") && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Prevented you from dropping your sword!");
        }
    }

    public static boolean isSlotLocked(int slot) {
        switch (slot) {
            case 1: {
                return cfg.v.hotbarLock_slot1;
            }
            case 2: {
                return cfg.v.hotbarLock_slot2;
            }
            case 3: {
                return cfg.v.hotbarLock_slot3;
            }
            case 4: {
                return cfg.v.hotbarLock_slot4;
            }
            case 5: {
                return cfg.v.hotbarLock_slot5;
            }
            case 6: {
                return cfg.v.hotbarLock_slot6;
            }
            case 7: {
                return cfg.v.hotbarLock_slot7;
            }
            case 8: {
                return cfg.v.hotbarLock_slot8;
            }
            case 9: {
                return cfg.v.hotbarLock_slot9;
            }
        }
        return false;
    }
}
