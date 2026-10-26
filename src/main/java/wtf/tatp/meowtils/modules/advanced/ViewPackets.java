package wtf.tatp.meowtils.modules.advanced;

import java.util.Arrays;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class ViewPackets
extends Module {
    private BooleanValue showAll;
    private ArrayValue direction;

    public ViewPackets() {
        super("ViewPackets", "packetDebuggerKey", "packetDebugger", Module.Category.Advanced);
        this.tooltip("View all outgoing/incoming packets in chat.");
        this.showAll = new BooleanValue("Ignore spam", "ignorePacketSpam");
        this.addBoolean(this.showAll);
        this.direction = new ArrayValue("Direction", Arrays.asList("Both", "Outgoing", "Incoming"), "packetDirection");
        this.addArray(this.direction);
    }
}
