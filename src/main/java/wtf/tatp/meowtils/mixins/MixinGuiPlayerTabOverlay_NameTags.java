package wtf.tatp.meowtils.mixins;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.util.NameUtil;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTabOverlay_NameTags {
    @Inject(method={"getPlayerName"}, at={@At(value="RETURN")}, cancellable=true)
    public void injectIconsIntoTab(NetworkPlayerInfo info, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(NameUtil.appendTabName(info, cir.getReturnValue()));
    }
}
