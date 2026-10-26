package wtf.tatp.meowtils.util.anticheat.checks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class NoSlowCheck {
    private int noSlowTicks = 0;
    private double lastPosX;
    private double lastPosZ;

    public void anticheatCheck(EntityPlayer player) {
        if (!cfg.v.detectNoSlow) {
            return;
        }
        double deltaX = player.posX - this.lastPosX;
        double deltaZ = player.posZ - this.lastPosZ;
        double speed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        if (player.isSprinting() && player.isUsingItem() && !player.isRiding()) {
            double baseThreshold = 0.05;
            PotionEffect speedEffect = player.getActivePotionEffect(Potion.moveSpeed);
            if (speedEffect != null) {
                int amplifier = speedEffect.getAmplifier();
                baseThreshold *= 1.0 + 0.2 * (double)(amplifier + 1);
            }
            if (speed > baseThreshold) {
                ++this.noSlowTicks;
                if (cfg.v.debugMessages && this.noSlowTicks > 5) {
                    Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AntiCheat]: " + EnumChatFormatting.WHITE + player.getName() + " NoSlow ticks: " + this.noSlowTicks + " | Speed: " + speed + " | Threshold: " + baseThreshold);
                }
            } else {
                this.noSlowTicks = 0;
            }
        } else {
            this.noSlowTicks = 0;
        }
    }

    public boolean failedNoSlow() {
        return this.noSlowTicks > 20;
    }

    public void reset() {
        this.noSlowTicks = 0;
    }
}
