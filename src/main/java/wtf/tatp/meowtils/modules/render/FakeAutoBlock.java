package wtf.tatp.meowtils.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.util.User32Util;

public class FakeAutoBlock
extends Module {
    private BooleanValue blockhitOnly;
    private BooleanValue killauraOnly;
    private NumberValue delay;
    private static long lastClickTime = 0L;
    private static boolean isKillaura = false;

    public FakeAutoBlock() {
        super("FakeAutoBlock", "fakeAutoBlockKey", "fakeAutoBlock", Module.Category.Render);
        this.tooltip("Renders your sword blocked while swinging.\n" + EnumChatFormatting.YELLOW + "\u00a7eNote: " + EnumChatFormatting.YELLOW + "Require " + EnumChatFormatting.YELLOW + "mouse " + EnumChatFormatting.YELLOW + "down " + EnumChatFormatting.YELLOW + "only " + EnumChatFormatting.YELLOW + "works " + EnumChatFormatting.YELLOW + "on " + EnumChatFormatting.YELLOW + "windows");
        this.blockhitOnly = new BooleanValue("Require mouse down", "blockhitOnly");
        this.addBoolean(this.blockhitOnly);
        this.killauraOnly = new BooleanValue("Killaura only", "fakeAutoBlockKillauraOnly");
        this.addBoolean(this.killauraOnly);
        this.delay = new NumberValue("Release delay", 0.0, 1000.0, 50.0, "ms", "fakeAutoBlockDelay", Integer.TYPE);
        this.addValue(this.delay);
    }

    public static boolean isSword(ItemStack item) {
        return item != null && item.getItem() instanceof ItemSword;
    }

    public static boolean isKillaura() {
        return isKillaura;
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        boolean left = User32Util.holdingLeftClick();
        boolean right = User32Util.holdingRightClick();
        if (left || right) {
            lastClickTime = System.currentTimeMillis();
        }
        long timeSinceClick = System.currentTimeMillis() - lastClickTime;
        boolean swing = mc.thePlayer.isSwingInProgress;
        isKillaura = swing && !left && !right && timeSinceClick > (long)cfg.v.fakeAutoBlockDelay;
    }
}
