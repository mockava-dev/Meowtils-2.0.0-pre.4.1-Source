package wtf.tatp.meowtils.handlers;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.BlacklistUtil;
import wtf.tatp.meowtils.util.NameUtil;

public class BlacklistHandler {
    private static final Set<String> warnedPlayers = new HashSet<String>();

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer)event.entity;
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().thePlayer;
        if (localPlayer == null || player == localPlayer) {
            return;
        }
        if (!cfg.v.warnBlacklistedPlayer) {
            return;
        }
        String name = player.getName();
        String uuid = player.getUniqueID().toString();
        if ((BlacklistUtil.isBlacklistedByUUID(uuid) || BlacklistUtil.isBlacklisted(name)) && !warnedPlayers.contains(uuid)) {
            String entry = BlacklistUtil.getEntry(uuid);
            if (entry == null) {
                entry = BlacklistUtil.getEntry(name);
            }
            if (entry != null) {
                String reason = entry.split(" ", 2)[1];
                Meowtils.addMessage(EnumChatFormatting.RED + "Warning: " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(name) + EnumChatFormatting.GRAY + " is blacklisted for: " + BlacklistUtil.getFormattedReason(reason));
                warnedPlayers.add(uuid);
            }
        }
    }

    public static void clear() {
        warnedPlayers.clear();
    }
}
