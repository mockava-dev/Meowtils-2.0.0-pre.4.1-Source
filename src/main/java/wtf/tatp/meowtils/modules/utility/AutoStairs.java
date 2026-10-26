package wtf.tatp.meowtils.modules.utility;

import net.minecraft.block.BlockStairs;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.gui.Module;

public class AutoStairs
extends Module {
    private double lastY = 0.0;

    public AutoStairs() {
        super("AutoStairs", "autoStairsKey", "autoStairs", Module.Category.Utility);
        this.tooltip("Automatically jumps when going up stairs, for faster movement.");
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        double currentY = this.mc.thePlayer.posY;
        if (this.mc.thePlayer.onGround && this.mc.thePlayer.isSprinting()) {
            double heightGain = currentY - this.lastY;
            BlockPos below = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.1, this.mc.thePlayer.posZ);
            if (heightGain == 0.5 && this.mc.theWorld.getBlockState(below).getBlock() instanceof BlockStairs) {
                KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindJump.getKeyCode(), (boolean)true);
                Meowtils.debugMessage(EnumChatFormatting.YELLOW + "[AutoStairs]: " + EnumChatFormatting.RESET + "Jumped!");
                new DelayedTask(() -> KeyBinding.setKeyBindState((int)this.mc.gameSettings.keyBindJump.getKeyCode(), (boolean)false), 1);
            }
        }
        this.lastY = currentY;
    }
}
