package wtf.tatp.meowtils.util.anticheat.checks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class AutoBlockCheck {
    private int autoBlockTicks = 0;

    public void anticheatCheck(EntityPlayer player) {
        if (!cfg.v.detectAutoBlock) {
            return;
        }
        if (player.isSwingInProgress && player.isBlocking()) {
            ++this.autoBlockTicks;
            if (cfg.v.debugMessages && this.autoBlockTicks > 5) {
                Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AntiCheat]: " + EnumChatFormatting.WHITE + player.getName() + " AutoBlock ticks: " + this.autoBlockTicks);
            }
        } else {
            this.autoBlockTicks = 0;
        }
    }

    public boolean failedAutoBlock() {
        return this.autoBlockTicks > 10;
    }

    public void reset() {
        this.autoBlockTicks = 0;
    }
}
