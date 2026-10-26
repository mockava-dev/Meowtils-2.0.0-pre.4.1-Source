package wtf.tatp.meowtils.mixins;

import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.advanced.InventoryMove;

@SideOnly(value=Side.CLIENT)
@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient_InventoryMove {
    @Unique
    private final Queue<C0EPacketClickWindow> delayedClick = new LinkedList<C0EPacketClickWindow>();

    @Inject(method={"addToSendQueue"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (InventoryMove.isInSurvivalInventory() && packet instanceof C0DPacketCloseWindow && cfg.v.inventoryMove && !InventoryMove.clickedItem) {
            ci.cancel();
        }
        if (packet instanceof C0DPacketCloseWindow) {
            InventoryMove.clickedItem = false;
        }
        if (packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT && cfg.v.inventoryMove && !InventoryMove.clickedItem) {
            ci.cancel();
        }
        if (packet instanceof C0EPacketClickWindow && !InventoryMove.clickedItem && cfg.v.inventoryMove && InventoryMove.isInSurvivalInventory()) {
            ci.cancel();
            InventoryMove.clickedItem = true;
            Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            this.delayedClick.add((C0EPacketClickWindow)packet);
            new DelayedTask(() -> {
                if (!this.delayedClick.isEmpty()) {
                    C0EPacketClickWindow click = this.delayedClick.poll();
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)click);
                }
            }, cfg.v.inventoryMoveClickDelay);
        }
    }
}
