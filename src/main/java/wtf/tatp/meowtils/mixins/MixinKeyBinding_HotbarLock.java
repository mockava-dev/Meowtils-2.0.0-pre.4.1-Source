package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.utility.HotbarLock;

@SideOnly(value=Side.CLIENT)
@Mixin(value={KeyBinding.class})
public class MixinKeyBinding_HotbarLock {
    @Shadow
    private boolean pressed;
    @Shadow
    private int pressTime;

    @Inject(method={"isPressed"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsPressed(CallbackInfoReturnable<Boolean> cir) {
        KeyBinding self = (KeyBinding)(Object)this;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            return;
        }
        if (mc.currentScreen != null) {
            return;
        }
        if (!cfg.v.hotbarLock) {
            return;
        }
        if (self == mc.gameSettings.keyBindDrop) {
            boolean cancel = false;
            if (cfg.v.hotbarLockMode.equals("Manual") && HotbarLock.isSlotLocked(mc.thePlayer.inventory.currentItem + 1)) {
                cancel = true;
            }
            if (cfg.v.hotbarLockMode.equals("Swords") && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                cancel = true;
            }
            if (cancel) {
                this.pressed = false;
                this.pressTime = 0;
                cir.setReturnValue(false);
            }
        }
    }
}
