package wtf.tatp.meowtils.mixins;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.GamemodeUtil;

@SideOnly(value=Side.CLIENT)
@Mixin(value={LayerArmorBase.class})
public abstract class MixinLayerArmorBase_NoArmorDye<T extends ModelBase> {
    @Redirect(method={"renderLayer"}, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemArmor;getColor(Lnet/minecraft/item/ItemStack;)I"))
    private int redirectLeatherColor(ItemArmor item, ItemStack stack) {
        if (cfg.v.noArmorDye && GamemodeUtil.skywarsGame && item.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && (cfg.v.noArmorDyeMode.equals("Model") || cfg.v.noArmorDyeMode.equals("Both"))) {
            return 10511680;
        }
        return item.getColor(stack);
    }
}
