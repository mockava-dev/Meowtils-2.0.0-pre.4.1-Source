package wtf.tatp.meowtils.gui.values;

import java.lang.reflect.Field;
import java.util.List;
import wtf.tatp.meowtils.config.cfg;

public class BooleanValue {
    private final String name;
    private boolean value;
    private final Field configField;
    private final Object configInstance;
    private final List<BooleanValue> group;

    public BooleanValue(String name, String fieldName) {
        this(name, fieldName, null);
    }

    public BooleanValue(String name, String fieldName, List<BooleanValue> group) {
        this.name = name;
        this.group = group;
        try {
            this.configInstance = cfg.v;
            this.configField = this.configInstance.getClass().getField(fieldName);
            this.configField.setAccessible(true);
            Object fieldValue = this.configField.get(this.configInstance);
            if (fieldValue instanceof Boolean) {
                this.value = (Boolean)fieldValue;
            } else {
                this.value = false;
                this.syncToConfig();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to link config field: " + fieldName, e);
        }
        if (this.group != null) {
            this.group.add(this);
        }
        this.syncToConfig();
    }

    public String getName() {
        return this.name;
    }

    public boolean getState() {
        return this.value;
    }

    public void setState(boolean state) {
        if (state == this.value) {
            return;
        }
        if (state && this.group != null) {
            for (BooleanValue other : this.group) {
                if (other == this || !other.getState()) continue;
                other.setState(false);
            }
        }
        this.value = state;
        this.syncToConfig();
    }

    public void toggle() {
        this.setState(!this.value);
    }

    private void syncToConfig() {
        try {
            this.configField.set(this.configInstance, this.value);
            cfg.save();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
