package wtf.tatp.meowtils.mixins;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wtf.tatp.meowtils.config.cfg;

@SideOnly(value=Side.CLIENT)
@Mixin(value={FontRenderer.class})
public class MixinFontRenderer_AntiObfuscate {
    @ModifyVariable(method={"renderString"}, at=@At(value="HEAD"), ordinal=0, argsOnly=true)
    private String modifyRenderString(String string) {
        if (string == null) {
            return null;
        }
        if (cfg.v.antiObfuscate) {
            string = string.replaceAll("\u00a7k", "");
        }
        return string;
    }

    @ModifyVariable(method={"getStringWidth"}, at=@At(value="HEAD"), ordinal=0, argsOnly=true)
    private String modifyStringWidth(String string) {
        if (string == null) {
            return null;
        }
        if (cfg.v.antiObfuscate) {
            string = string.replaceAll("\u00a7k", "");
        }
        return string;
    }
}
