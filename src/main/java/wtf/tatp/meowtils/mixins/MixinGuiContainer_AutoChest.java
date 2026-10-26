package wtf.tatp.meowtils.mixins;

import java.awt.Color;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.bedwars.AutoChest;
import wtf.tatp.meowtils.util.Render;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer_AutoChest {
    @Inject(method={"drawSlot"}, at={@At(value="HEAD")})
    private void onDrawSlot(Slot slot, CallbackInfo ci) {
        if (!cfg.v.autoChestRenderClicked) {
            return;
        }
        if (slot == null) {
            return;
        }
        if (AutoChest.clickedSlots.contains(slot.slotNumber)) {
            Render.drawSlotBackground(slot.xDisplayPosition, slot.yDisplayPosition, new Color(255, 0, 255, 255).getRGB());
        }
    }
}
