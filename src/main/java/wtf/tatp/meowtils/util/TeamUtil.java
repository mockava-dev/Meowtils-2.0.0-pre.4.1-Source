package wtf.tatp.meowtils.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.config.cfg;

public class TeamUtil {
    private static boolean spectator = false;
    private long spectatorTime = 0L;

    public static boolean inSpectator() {
        return spectator;
    }

    public static boolean ignoreSelf(String playerName) {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.thePlayer != null && playerName != null && playerName.equalsIgnoreCase(mc.thePlayer.getName());
    }

    public static boolean ignoreTeam(String playerName) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer target = mc.theWorld.playerEntities.stream().filter(p -> p.getName().equalsIgnoreCase(playerName)).findFirst().orElse(null);
        if (mc.thePlayer == null || mc.theWorld == null || playerName == null) {
            return false;
        }
        if (!cfg.v.ignoreTeam) {
            return false;
        }
        if (target == null) {
            return false;
        }
        if (target.capabilities.allowFlying) {
            return true;
        }
        try {
            if (mc.thePlayer.isOnSameTeam((EntityLivingBase)target)) {
                return true;
            }
            String selfFormatted = mc.thePlayer.getDisplayName().getFormattedText();
            String targetFormatted = target.getDisplayName().getFormattedText();
            String selfColor = TeamUtil.getMostFrequentColor(selfFormatted);
            String targetColor = TeamUtil.getMostFrequentColor(targetFormatted);
            if (selfColor != null && selfColor.equals(targetColor)) {
                return true;
            }
        }
        catch (Exception exception) {
            
        }
        return false;
    }

    private static String getMostFrequentColor(String text) {
        if (text == null) {
            return null;
        }
        HashMap<String, Integer> colorCounts = new HashMap<String, Integer>();
        for (int i = 0; i < text.length() - 1; ++i) {
            char code;
            char c = text.charAt(i);
            if (c != '\u00a7' || ((code = text.charAt(i + 1)) < '0' || code > '9') && (code < 'a' || code > 'f')) continue;
            String currentColor = "\u00a7" + code;
            colorCounts.put(currentColor, colorCounts.getOrDefault(currentColor, 0) + 1);
        }
        return colorCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        long now = System.currentTimeMillis();
        if (mc.thePlayer.capabilities.allowFlying) {
            spectator = true;
            this.spectatorTime = now;
        } else {
            spectator = now - this.spectatorTime < 8000L;
        }
    }
}
