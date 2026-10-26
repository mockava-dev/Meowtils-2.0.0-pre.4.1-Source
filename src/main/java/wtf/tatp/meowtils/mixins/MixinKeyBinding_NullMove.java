package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.utility.NullMove;

@SideOnly(value=Side.CLIENT)
@Mixin(value={KeyBinding.class})
public abstract class MixinKeyBinding_NullMove {
    @Shadow
    private int keyCode;
    @Shadow
    public boolean pressed;

    @Inject(method={"isKeyDown"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsKeyDown(CallbackInfoReturnable<Boolean> cir) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        if (!cfg.v.nullMove) {
            return;
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
            return;
        }
        if (this.keyCode == settings.keyBindLeft.getKeyCode()) {
            if (this.pressed) {
                if (NullMove.RIGHT_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(NullMove.RIGHT_STRAFE_LAST_PRESS_TIME <= NullMove.LEFT_STRAFE_LAST_PRESS_TIME);
            } else {
                cir.setReturnValue(false);
            }
            cir.cancel();
        } else if (this.keyCode == settings.keyBindRight.getKeyCode()) {
            if (this.pressed) {
                if (NullMove.LEFT_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(NullMove.LEFT_STRAFE_LAST_PRESS_TIME <= NullMove.RIGHT_STRAFE_LAST_PRESS_TIME);
            } else {
                cir.setReturnValue(false);
            }
            cir.cancel();
        } else if (this.keyCode == settings.keyBindForward.getKeyCode()) {
            if (this.pressed) {
                if (NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME <= NullMove.FORWARD_STRAFE_LAST_PRESS_TIME);
            } else {
                cir.setReturnValue(false);
            }
            cir.cancel();
        } else if (this.keyCode == settings.keyBindBack.getKeyCode()) {
            if (this.pressed) {
                if (NullMove.FORWARD_STRAFE_LAST_PRESS_TIME == 0L) {
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(NullMove.FORWARD_STRAFE_LAST_PRESS_TIME <= NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME);
            } else {
                cir.setReturnValue(false);
            }
            cir.cancel();
        }
    }

    @Inject(method={"setKeyBindState"}, at={@At(value="HEAD")})
    private static void onSetKeyBindState(int keyCode, boolean pressed, CallbackInfo ci) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        if (!cfg.v.nullMove) {
            return;
        }
        if (keyCode == settings.keyBindLeft.getKeyCode()) {
            NullMove.LEFT_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
        } else if (keyCode == settings.keyBindRight.getKeyCode()) {
            NullMove.RIGHT_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
        } else if (keyCode == settings.keyBindForward.getKeyCode()) {
            NullMove.FORWARD_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
        } else if (keyCode == settings.keyBindBack.getKeyCode()) {
            NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
        }
    }
}
