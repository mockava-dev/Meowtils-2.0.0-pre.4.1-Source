package wtf.tatp.meowtils.mixins;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.GamemodeUtil;

@SideOnly(value=Side.CLIENT)
@Mixin(value={ItemArmor.class})
public abstract class MixinItemArmor_NoArmorDye {
    @Inject(method={"getColorFromItemStack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetColorFromItemStack(ItemStack stack, int renderPass, CallbackInfoReturnable<Integer> cir) {
        ItemArmor self = (ItemArmor)(Object)this;
        if (cfg.v.noArmorDye && GamemodeUtil.skywarsGame && self.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && (cfg.v.noArmorDyeMode.equals("Item") || cfg.v.noArmorDyeMode.equals("Both"))) {
            cir.setReturnValue(10511680);
        }
    }
}
