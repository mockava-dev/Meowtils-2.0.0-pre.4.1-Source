package wtf.tatp.meowtils.gui.values;

import wtf.tatp.meowtils.gui.ColorComponent;

public class BrightnessValue {
    private final double min = 0.0;
    private final double max = 100.0;
    private double brightness;
    private final ColorComponent link;

    public BrightnessValue(ColorComponent link) {
        this.link = link;
        this.brightness = (double)link.getBrightness() * 100.0;
    }

    public double get() {
        return this.brightness;
    }

    public void set(double newBrightness) {
        this.brightness = this.clamp(newBrightness, 0.0, 100.0);
        float b = (float)(this.brightness / 100.0);
        this.link.apply(this.link.getHue(), this.link.getSaturation(), b);
    }

    public void syncFromConfig() {
        this.link.syncFromConfig();
        this.brightness = (double)this.link.getBrightness() * 100.0;
    }

    public int getCurrentRGB() {
        return this.link.getRGB();
    }

    public ColorComponent getLink() {
        return this.link;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
