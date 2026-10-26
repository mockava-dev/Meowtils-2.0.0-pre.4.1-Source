package wtf.tatp.meowtils.modules.advanced;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.mixins.GuiContainerAccessor;
import wtf.tatp.meowtils.modules.skywars.ItemHighlight;

public class InventoryFill
extends Module {
    public static final Set<Integer> clickedSlots = new HashSet<Integer>();
    private long nextClickTime = 0L;
    private long nextAutoClickTime = 0L;
    private ArrayValue clickMode;
    private BooleanValue renderClicked;
    private NumberValue cps;
    private NumberValue autoDelay;
    private ArrayValue mode;

    public InventoryFill() {
        super("InventoryFill", "inventoryFillKey", "inventoryFill", Module.Category.Advanced);
        this.tooltip("Automatically clicks items in inventories for you.\n-Auto/Manual mode, grab items from chests automatically or manually\n\u00a7cWARNING: \u00a7cThis \u00a7cmight \u00a7cflag \u00a7con \u00a7clow \u00a7cdelay.");
        this.cps = new NumberValue("CPS", 1.0, 20.0, 1.0, null, "inventoryFillCps", Integer.TYPE);
        this.addValue(this.cps);
        this.autoDelay = new NumberValue("Auto delay", 50.0, 1000.0, 50.0, "ms", "inventoryFillAutoDelay", Integer.TYPE);
        this.addValue(this.autoDelay);
        this.renderClicked = new BooleanValue("Render clicked", "inventoryFillRenderClicked");
        this.addBoolean(this.renderClicked);
        this.clickMode = new ArrayValue("Click Mode", Arrays.asList("Left", "Drop", "Both"), "inventoryFillClickMode");
        this.addArray(this.clickMode);
        this.mode = new ArrayValue("Mode", Arrays.asList("Manual", "Auto"), "inventoryFillMode");
        this.addArray(this.mode);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        boolean allowDrop;
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (mc.thePlayer == null || mc.currentScreen == null || mc.playerController == null) {
            return;
        }
        if (!(mc.currentScreen instanceof GuiContainer)) {
            return;
        }
        if (!mc.playerController.getCurrentGameType().isSurvivalOrAdventure()) {
            return;
        }
        GuiContainer gui = (GuiContainer)mc.currentScreen;
        Slot hovered = gui.getSlotUnderMouse();
        boolean shiftDown = Keyboard.isKeyDown((int)42);
        boolean leftClickDown = Mouse.isButtonDown((int)0);
        boolean dropKeyDown = Keyboard.isKeyDown((int)mc.gameSettings.keyBindDrop.getKeyCode());
        boolean ctrlKeyDown = Keyboard.isKeyDown((int)29);
        long now = System.currentTimeMillis();
        String clickMode = cfg.v.inventoryFillClickMode;
        boolean allowLeft = clickMode.equals("Left") || clickMode.equals("Both");
        boolean bl = allowDrop = clickMode.equals("Drop") || clickMode.equals("Both");
        if (cfg.v.inventoryFillRenderClicked) {
            clickedSlots.clear();
        }
        if (cfg.v.inventoryFillMode.equals("Auto") && allowLeft && leftClickDown && shiftDown && now >= this.nextAutoClickTime) {
            this.nextAutoClickTime = now + (long)cfg.v.inventoryFillAutoDelay;
            this.autoHandler(gui);
        }
        if (allowLeft && leftClickDown && shiftDown && now >= this.nextClickTime) {
            this.nextClickTime = now + 1000L / (long)cfg.v.inventoryFillCps;
            this.leftClickHandler(gui, hovered);
        }
        if (allowDrop && dropKeyDown && hovered != null && hovered.getHasStack()) {
            this.dropHandler(gui, hovered, ctrlKeyDown);
        }
    }

    private void autoHandler(GuiContainer gui) {
        if (!(gui instanceof GuiChest)) {
            return;
        }
        GuiChest guiChest = (GuiChest)Minecraft.getMinecraft().currentScreen;
        IInventory lowerInventory = ((ContainerChest)guiChest.inventorySlots).getLowerChestInventory();
        String inventoryName = lowerInventory.getDisplayName().getUnformattedText();
        String localizedChest = I18n.format((String)"container.chest", (Object[])new Object[0]);
        String localizedLargeChest = I18n.format((String)"container.chestDouble", (Object[])new Object[0]);
        if (!inventoryName.equals(localizedChest) && !inventoryName.equals(localizedLargeChest)) {
            return;
        }
        for (Slot chestSlot : gui.inventorySlots.inventorySlots) {
            ItemStack stack;
            if (chestSlot == null || !chestSlot.getHasStack() || chestSlot.inventory == Minecraft.getMinecraft().thePlayer.inventory || (stack = chestSlot.getStack()) == null) continue;
            String id = ((ResourceLocation)Item.itemRegistry.getNameForObject(stack.getItem())).toString();
            if (ItemHighlight.getItemBlacklist().contains(id)) continue;
            this.click(gui, chestSlot, 0, 1);
            break;
        }
    }

    private void leftClickHandler(GuiContainer gui, Slot hovered) {
        if (hovered == null || !hovered.getHasStack()) {
            return;
        }
        this.click(gui, hovered, 0, 1);
    }

    private void dropHandler(GuiContainer gui, Slot hovered, boolean ctrlDown) {
        int type = 4;
        int button = ctrlDown ? 1 : 0;
        this.click(gui, hovered, button, type);
    }

    private void click(GuiContainer gui, Slot slot, int mouseButton, int type) {
        ((GuiContainerAccessor)gui).clickSlot(slot, slot.slotNumber, mouseButton, type);
        if (cfg.v.inventoryFillRenderClicked) {
            clickedSlots.add(slot.slotNumber);
        }
    }
}
