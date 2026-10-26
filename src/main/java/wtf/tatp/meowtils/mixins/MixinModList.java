package wtf.tatp.meowtils.mixins;

import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.config.cfg;

@SideOnly(value=Side.CLIENT)
@Mixin(value={FMLHandshakeMessage.ModList.class})
public abstract class MixinModList {
    @Shadow(remap=false)
    private Map<String, String> modTags;

    @Inject(method={"<init>(Ljava/util/List;)V"}, at={@At(value="RETURN")}, remap=false)
    public void onInit(CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return;
        }
        if (cfg.v.modID.equals("None")) {
            return;
        }
        if (cfg.v.modID.equals("Meowtils")) {
            this.modTags.remove("meowtils");
        }
    }
}
