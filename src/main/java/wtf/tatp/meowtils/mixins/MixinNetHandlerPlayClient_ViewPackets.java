package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

@SideOnly(value=Side.CLIENT)
@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient_ViewPackets {
    @Inject(method={"addToSendQueue"}, at={@At(value="HEAD")})
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (!cfg.v.packetDebugger) {
            return;
        }
        if (!cfg.v.packetDirection.equals("Outgoing") && !cfg.v.packetDirection.equals("Both")) {
            return;
        }
        if (cfg.v.ignorePacketSpam && this.showAll(packet)) {
            return;
        }
        Meowtils.addMessage(EnumChatFormatting.GREEN + "\u00ab " + EnumChatFormatting.WHITE + packet.getClass().getSimpleName());
    }

    private boolean showAll(Packet<?> packet) {
        if (packet instanceof C03PacketPlayer) {
            return true;
        }
        if (packet instanceof C0FPacketConfirmTransaction) {
            return true;
        }
        return packet instanceof C00PacketKeepAlive;
    }
}
