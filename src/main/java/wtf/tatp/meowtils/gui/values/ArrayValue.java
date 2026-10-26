package wtf.tatp.meowtils.gui.values;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wtf.tatp.meowtils.config.cfg;

public class ArrayValue {
    private final String name;
    private final List<String> modes;
    private String current;
    private final Field configField;
    private final Object configInstance;

    public ArrayValue(String name, List<String> modes, String fieldName) {
        if (modes.isEmpty()) {
            throw new IllegalArgumentException("Mode list must not be empty!");
        }
        this.name = name;
        this.modes = new ArrayList<String>(modes);
        try {
            this.configInstance = cfg.v;
            this.configField = this.configInstance.getClass().getField(fieldName);
            this.configField.setAccessible(true);
            Object fieldValue = this.configField.get(this.configInstance);
            if (fieldValue instanceof String && this.modes.contains(fieldValue)) {
                this.current = (String)fieldValue;
            } else {
                this.current = this.modes.get(0);
                this.syncToConfig();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to link config field: " + fieldName, e);
        }
    }

    public String getName() {
        return this.name;
    }

    public List<String> getModes() {
        return Collections.unmodifiableList(this.modes);
    }

    public String getValue() {
        return this.current;
    }

    public int getIndex() {
        return this.modes.indexOf(this.current);
    }

    public boolean is(String mode) {
        return this.current.equals(mode);
    }

    public void setValue(String value) {
        if (!this.modes.contains(value)) {
            throw new IllegalArgumentException("Mode not found: " + value);
        }
        this.current = value;
        this.syncToConfig();
    }

    public void setValue(int index) {
        if (index < 0 || index >= this.modes.size()) {
            throw new IndexOutOfBoundsException("Mode index out of range");
        }
        this.current = this.modes.get(index);
        this.syncToConfig();
    }

    private void syncToConfig() {
        try {
            this.configField.set(this.configInstance, this.current);
            cfg.save();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
