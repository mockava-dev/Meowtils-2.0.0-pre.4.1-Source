package wtf.tatp.meowtils.mixins;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.advanced.InventoryMove;

@SideOnly(value=Side.CLIENT)
@Mixin(value={KeyBinding.class})
public class MixinKeyBinding_InventoryMove {
    @Inject(method={"isKeyDown"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsKeyDown(CallbackInfoReturnable<Boolean> cir) {
        KeyBinding self = (KeyBinding)(Object)this;
        if (cfg.v.inventoryMove && InventoryMove.isMovementKey(self) && InventoryMove.isInSurvivalInventory() && !InventoryMove.clickedItem) {
            cir.setReturnValue(Keyboard.isKeyDown((int)self.getKeyCode()));
        }
    }
}
