package wtf.tatp.meowtils.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.Render;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer_ShinyPots {
    @Shadow
    protected abstract void drawSlot(Slot var1);

    @Inject(method={"drawSlot"}, at={@At(value="HEAD")})
    private void onDrawSlot(Slot slot, CallbackInfo ci) {
        if (!cfg.v.shinyPots) {
            return;
        }
        if (slot == null || !slot.getHasStack()) {
            return;
        }
        ItemStack stack = slot.getStack();
        if (stack.getItem() instanceof ItemPotion) {
            int color = ((ItemPotion)stack.getItem()).getColorFromItemStack(stack, 0);
            Render.drawSlotBackground(slot.xDisplayPosition, slot.yDisplayPosition, color | 0xCC000000);
        }
    }
}
