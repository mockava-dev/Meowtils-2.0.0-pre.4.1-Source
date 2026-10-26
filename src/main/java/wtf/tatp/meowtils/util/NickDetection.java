package wtf.tatp.meowtils.util;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class NickDetection {
    public static boolean isNicked(EntityPlayer player) {
        if (player == null || player.getGameProfile() == null) {
            return false;
        }
        UUID uuid = player.getGameProfile().getId();
        if (uuid == null) {
            return false;
        }
        return uuid.version() == 1;
    }
}
