package wtf.tatp.meowtils.gui.values;

import java.lang.reflect.Field;
import java.util.Locale;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;

public class OpacityValue {
    private final String name;
    private final double min;
    private final double max;
    private final double increment;
    private final String valueType;
    private double value;
    private final Field configField;
    private final Object configInstance;
    private final Class<?> targetType;
    private final ColorComponent link;

    public OpacityValue(String name, String fieldName, ColorComponent link) {
        this.name = name;
        this.min = 0.0;
        this.max = 100.0;
        this.increment = 5.0;
        this.valueType = "%";
        this.targetType = Integer.TYPE;
        this.link = link;
        try {
            this.configInstance = cfg.v;
            this.configField = this.configInstance.getClass().getField(fieldName);
            this.configField.setAccessible(true);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to link config field: " + fieldName, e);
        }
        this.syncFromConfig();
    }

    public String getName() {
        return this.name;
    }

    public double get() {
        return this.value;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public double getIncrement() {
        return this.increment;
    }

    public String getValueType() {
        return this.valueType;
    }

    public ColorComponent getLink() {
        return this.link;
    }

    public void set(double newValue) {
        this.value = this.snap(this.clamp(newValue, this.min, this.max));
        this.syncToConfig();
    }

    public void syncFromConfig() {
        try {
            Object fieldValue = this.configField.get(this.configInstance);
            if (fieldValue instanceof Number) {
                this.value = this.snap(((Number)fieldValue).doubleValue());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncToConfig() {
        try {
            Object castedValue = this.castValue(this.value, this.targetType);
            this.configField.set(this.configInstance, castedValue);
            cfg.save();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object castValue(double value, Class<?> type) {
        if (type == Integer.TYPE || type == Integer.class) {
            return (int)value;
        }
        if (type == Float.TYPE || type == Float.class) {
            return Float.valueOf((float)value);
        }
        if (type == Long.TYPE || type == Long.class) {
            return (long)value;
        }
        if (type == Double.TYPE || type == Double.class) {
            return value;
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private double snap(double v) {
        double steps = Math.round((v - this.min) / this.increment);
        double snapped = this.min + steps * this.increment;
        int decimals = Math.max(0, this.getDecimalPlaces(this.increment));
        double scale = Math.pow(10.0, decimals);
        return (double)Math.round(snapped * scale) / scale;
    }

    private int getDecimalPlaces(double value) {
        String text = Double.toString(value);
        int index = text.indexOf(46);
        return index < 0 ? 0 : text.length() - index - 1;
    }

    public String getFormattedValue() {
        if (this.targetType == Integer.TYPE || this.targetType == Integer.class || this.value == Math.floor(this.value)) {
            return String.valueOf((int)this.value);
        }
        int decimals = 0;
        for (double inc = this.increment; inc < 1.0 && decimals < 6; inc *= 10.0, ++decimals) {
        }
        String raw = String.format(Locale.US, "%." + decimals + "f", this.value);
        raw = raw.replaceAll("0*$", "").replaceAll("\\.$", "");
        return raw;
    }
}
