package wtf.tatp.meowtils.modules.meowtils;

import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;

public class Notifications
extends Module {
    private BooleanValue failedQueueMessage;
    private BooleanValue requeueFeedbackMessage;
    private BooleanValue playerRemovedMessage;
    private BooleanValue toggleNotification;
    private BooleanValue warnBlacklistedPlayer;

    public Notifications() {
        super("Notifications", null, null, Module.Category.Meowtils, true);
        this.tooltip("Select which notifications to send in chat.");
        this.toggleNotification = new BooleanValue("Module Toggle", "toggleNotifications");
        this.addBoolean(this.toggleNotification);
        this.warnBlacklistedPlayer = new BooleanValue("Blacklisted Player", "warnBlacklistedPlayer");
        this.addBoolean(this.warnBlacklistedPlayer);
        this.playerRemovedMessage = new BooleanValue("Reveal Banned Player", "playerRemovedMessage");
        this.addBoolean(this.playerRemovedMessage);
        this.requeueFeedbackMessage = new BooleanValue("Requeue Feedback", "requeueSavedCommandMessage");
        this.addBoolean(this.requeueFeedbackMessage);
        this.failedQueueMessage = new BooleanValue("Failed to Queue", "failedQueueMessage");
        this.addBoolean(this.failedQueueMessage);
    }
}
