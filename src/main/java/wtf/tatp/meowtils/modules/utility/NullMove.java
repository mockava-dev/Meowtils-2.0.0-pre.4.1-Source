package wtf.tatp.meowtils.modules.utility;

import wtf.tatp.meowtils.gui.Module;

public class NullMove
extends Module {
    public static long LEFT_STRAFE_LAST_PRESS_TIME = 0L;
    public static long RIGHT_STRAFE_LAST_PRESS_TIME = 0L;
    public static long FORWARD_STRAFE_LAST_PRESS_TIME = 0L;
    public static long BACKWARD_STRAFE_LAST_PRESS_TIME = 0L;

    public NullMove() {
        super("NullMove", "nullMoveKey", "nullMove", Module.Category.Utility);
        this.tooltip("Does not prevent movement if several movement keys are held at once.");
    }
}
