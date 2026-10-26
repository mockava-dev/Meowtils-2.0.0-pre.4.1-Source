package wtf.tatp.meowtils.util.anticheat;

import net.minecraft.entity.player.EntityPlayer;
import wtf.tatp.meowtils.util.anticheat.PlayerData;
import wtf.tatp.meowtils.util.anticheat.checks.AutoBlockCheck;
import wtf.tatp.meowtils.util.anticheat.checks.KillauraBCheck;
import wtf.tatp.meowtils.util.anticheat.checks.LegitScaffoldCheck;
import wtf.tatp.meowtils.util.anticheat.checks.NoSlowCheck;

public class AntiCheatData {
    public PlayerData playerData = new PlayerData();
    public AutoBlockCheck autoBlockCheck = new AutoBlockCheck();
    public NoSlowCheck noSlowCheck = new NoSlowCheck();
    public LegitScaffoldCheck legitScaffoldCheck = new LegitScaffoldCheck();
    public KillauraBCheck killauraBCheck = new KillauraBCheck();

    public void anticheatCheck(EntityPlayer player) {
        this.playerData.update(player);
        this.autoBlockCheck.anticheatCheck(player);
        this.noSlowCheck.anticheatCheck(player);
        this.legitScaffoldCheck.anticheatCheck(player);
        this.killauraBCheck.anticheatCheck(player);
    }

    public boolean failedAutoBlock() {
        return this.autoBlockCheck.failedAutoBlock();
    }

    public boolean failedNoSlow() {
        return this.noSlowCheck.failedNoSlow();
    }

    public boolean failedLegitScaffold() {
        return this.legitScaffoldCheck.failedLegitScaffold();
    }

    public boolean failedKillauraB() {
        return this.killauraBCheck.failedKillauraB();
    }
}
