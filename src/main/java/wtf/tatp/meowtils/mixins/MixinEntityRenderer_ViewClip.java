package wtf.tatp.meowtils.mixins;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wtf.tatp.meowtils.config.cfg;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityRenderer.class})
public class MixinEntityRenderer_ViewClip {
    @ModifyVariable(method={"orientCamera"}, at=@At(value="STORE"), ordinal=0)
    private double modifyCameraDistance(double original) {
        return cfg.v.viewClip ? 10000.0 : original;
    }
}
