package wtf.tatp.meowtils.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathTracker {
    private static final Pattern deathMessagePattern = Pattern.compile("^([A-Za-z0-9_]{1,16})\\b.*");
    private static final Map<UUID, Long> recentlyDead = new ConcurrentHashMap<UUID, Long>();
    private static final long DEATH_TIMEOUT = 10000L;

    public static void markPlayerDead(EntityPlayer player) {
        recentlyDead.put(player.getUniqueID(), System.currentTimeMillis());
    }

    public static boolean isRecentlyDead(EntityPlayer player) {
        Long deathTime = recentlyDead.get(player.getUniqueID());
        if (deathTime == null) {
            return false;
        }
        return System.currentTimeMillis() - deathTime <= 10000L;
    }

    public static void clear() {
        recentlyDead.clear();
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        Matcher matcher = deathMessagePattern.matcher(message);
        if (!matcher.find()) {
            return;
        }
        String playerName = matcher.group(1);
        EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(playerName);
        if (player != null && !(player instanceof EntityPlayerSP)) {
            DeathTracker.markPlayerDead(player);
        }
    }
}
