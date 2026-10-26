package wtf.tatp.meowtils.modules.meowtils;

import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class Teams
extends Module {
    private BooleanValue ignoreTeam;
    private BooleanValue ignoreSpectator;

    public Teams() {
        super("Teams", null, null, Module.Category.Meowtils, true);
        this.tooltip("Makes certain modules ignore teammates.\n-Disable while spectator: Disables certain modules while you are a spectator");
        this.ignoreTeam = new BooleanValue("Ignore team", "ignoreTeam");
        this.addBoolean(this.ignoreTeam);
        this.ignoreSpectator = new BooleanValue("Disable while spectator", "ignoreTeamSpectator");
        this.addBoolean(this.ignoreSpectator);
    }
}
