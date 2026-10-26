package wtf.tatp.meowtils.util;

import net.minecraft.client.Minecraft;

public class PlaySound {
    private static final PlaySound INSTANCE = new PlaySound();
    Minecraft mc = Minecraft.getMinecraft();

    private PlaySound() {
    }

    public static PlaySound getInstance() {
        return INSTANCE;
    }

    public void playPingSound() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("random.orb", 1.0f, 1.0f);
        }
    }

    public void playPingSoundDeep() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("random.orb", 1.0f, 0.2f);
        }
    }

    public void playPingSoundMedium() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("random.orb", 1.0f, 0.5f);
        }
    }

    public void playPingSoundLevel() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("random.levelup", 1.0f, 2.0f);
        }
    }

    public void playAnvilSound() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("random.anvil_land", 0.7f, 1.8f);
        }
    }

    public void playCritSound() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("meowtils:crit", 1.0f, 1.0f);
        }
    }

    public void playMeowSound() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("mob.cat.meow", 1.0f, 1.0f);
        }
    }

    public void playAnvilBreakSound() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.playSound("random.anvil_use", 1.0f, 1.0f);
        }
    }
}
