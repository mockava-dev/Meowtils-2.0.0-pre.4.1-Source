package wtf.tatp.meowtils.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.render.FakeAutoBlock;
import wtf.tatp.meowtils.util.User32Util;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP_FakeAutoBlock
extends AbstractClientPlayer {
    public MixinEntityPlayerSP_FakeAutoBlock(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method={"onUpdate"}, at={@At(value="HEAD")})
    public void onUpdate(CallbackInfo ci) {
    }

    public int getItemInUseCount() {
        ItemStack heldItem = this.getHeldItem();
        boolean fakeAutoblock = cfg.v.fakeAutoBlock && FakeAutoBlock.isSword(heldItem) && (cfg.v.blockhitOnly && User32Util.holdingRightClick() || !cfg.v.blockhitOnly && this.isSwingInProgress || cfg.v.fakeAutoBlockKillauraOnly && FakeAutoBlock.isKillaura());
        return fakeAutoblock ? 10 : super.getItemInUseCount();
    }

    public boolean isBlocking() {
        ItemStack heldItem = this.getHeldItem();
        boolean fakeAutoblock = cfg.v.fakeAutoBlock && FakeAutoBlock.isSword(heldItem) && (cfg.v.blockhitOnly && User32Util.holdingRightClick() || !cfg.v.blockhitOnly && this.isSwingInProgress || cfg.v.fakeAutoBlockKillauraOnly && FakeAutoBlock.isKillaura());
        return fakeAutoblock || super.isBlocking();
    }
}
