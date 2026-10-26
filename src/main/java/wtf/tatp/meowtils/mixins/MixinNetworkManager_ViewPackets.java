package wtf.tatp.meowtils.mixins;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
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
@Mixin(value={NetworkManager.class})
public class MixinNetworkManager_ViewPackets {
    @Inject(method={"channelRead0"}, at={@At(value="HEAD")})
    private void onReceivePacket(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (!cfg.v.packetDebugger) {
            return;
        }
        if (!cfg.v.packetDirection.equals("Incoming") && !cfg.v.packetDirection.equals("Both")) {
            return;
        }
        if (cfg.v.ignorePacketSpam && this.showAll(packet)) {
            return;
        }
        Meowtils.addMessage(EnumChatFormatting.RED + "\u00bb " + EnumChatFormatting.WHITE + packet.getClass().getSimpleName());
    }

    private boolean showAll(Packet<?> packet) {
        if (packet instanceof S14PacketEntity) {
            return true;
        }
        if (packet instanceof S18PacketEntityTeleport) {
            return true;
        }
        if (packet instanceof S08PacketPlayerPosLook) {
            return true;
        }
        if (packet instanceof S32PacketConfirmTransaction) {
            return true;
        }
        if (packet instanceof S00PacketKeepAlive) {
            return true;
        }
        if (packet instanceof S19PacketEntityHeadLook) {
            return true;
        }
        if (packet instanceof S3EPacketTeams) {
            return true;
        }
        if (packet instanceof S29PacketSoundEffect) {
            return true;
        }
        if (packet instanceof S03PacketTimeUpdate) {
            return true;
        }
        if (packet instanceof S1CPacketEntityMetadata) {
            return true;
        }
        if (packet instanceof S0BPacketAnimation) {
            return true;
        }
        if (packet instanceof S3CPacketUpdateScore) {
            return true;
        }
        if (packet instanceof S21PacketChunkData) {
            return true;
        }
        if (packet instanceof S38PacketPlayerListItem) {
            return true;
        }
        if (packet instanceof S04PacketEntityEquipment) {
            return true;
        }
        if (packet instanceof S20PacketEntityProperties) {
            return true;
        }
        if (packet instanceof S26PacketMapChunkBulk) {
            return true;
        }
        if (packet instanceof S47PacketPlayerListHeaderFooter) {
            return true;
        }
        return packet instanceof C03PacketPlayer;
    }
}
