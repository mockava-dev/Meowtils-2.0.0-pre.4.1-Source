package wtf.tatp.meowtils.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.CapeCache;

@SideOnly(value=Side.CLIENT)
@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer_Cape
extends EntityPlayer {
    public MixinAbstractClientPlayer_Cape(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method={"getLocationCape"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation cape;
        if ((Object)this instanceof EntityPlayerSP && cfg.v.capeSelector && (cape = CapeCache.getCape(cfg.v.selectedCape)) != null) {
            cir.setReturnValue(cape);
        }
    }
}
