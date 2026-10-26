package wtf.tatp.meowtils.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiContainer.class})
public interface GuiContainerAccessor {
    @Invoker(value="handleMouseClick")
    public void clickSlot(Slot var1, int var2, int var3, int var4);
}
