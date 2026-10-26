package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.events.DelayedClick;
import wtf.tatp.meowtils.modules.advanced.InventoryMove;
import wtf.tatp.meowtils.modules.bedwars.AutoClaim;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer_InventoryClickCancel {
    private boolean scheduledClick;
    private int clickDelay;

    public MixinGuiContainer_InventoryClickCancel() {
        this.clickDelay = cfg.v.inventoryMoveClickDelay;
    }

    @Inject(method={"handleMouseClick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleMouseClick(Slot slot, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (!cfg.v.inventoryMove) {
            return;
        }
        if (!InventoryMove.isInSurvivalInventory()) {
            return;
        }
        if (this.scheduledClick) {
            ci.cancel();
        }
        if (!InventoryMove.clickedItem) {
            ci.cancel();
            InventoryMove.clickedItem = true;
            this.scheduledClick = true;
            GuiContainer gui = (GuiContainer)(Object)this;
            Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            new DelayedTask(() -> {
                this.scheduledClick = false;
            }, this.clickDelay);
            DelayedClick.windowClick(gui.inventorySlots.windowId, slotId, clickedButton, clickType, this.clickDelay, (EntityPlayer)Minecraft.getMinecraft().thePlayer);
        }
        if (cfg.v.autoClaim && AutoClaim.autoClaimPending) {
            ci.cancel();
        }
    }
}
