package wtf.tatp.meowtils.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityLivingBase.class})
public interface EntityLivingBaseAccessor {
    @Accessor(value="jumpTicks")
    public void setJumpTicks(int var1);
}
