package wtf.tatp.meowtils.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import wtf.tatp.meowtils.DelayedTask;

public class DelayedClick {
    public static void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, int delayTicks, EntityPlayer player) {
        short transactionID = player.openContainer.getNextTransactionID(player.inventory);
        ItemStack itemstack = player.openContainer.slotClick(slotId, mouseButtonClicked, mode, player);
        new DelayedTask(() -> Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, transactionID)), delayTicks);
    }
}
