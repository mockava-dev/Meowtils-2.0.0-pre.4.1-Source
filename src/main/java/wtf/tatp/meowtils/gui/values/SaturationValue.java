package wtf.tatp.meowtils.gui.values;

import wtf.tatp.meowtils.gui.ColorComponent;

public class SaturationValue {
    private final double min = 0.0;
    private final double max = 100.0;
    private double saturation;
    private final ColorComponent link;

    public SaturationValue(ColorComponent link) {
        this.link = link;
        this.saturation = (double)link.getSaturation() * 100.0;
    }

    public double get() {
        return this.saturation;
    }

    public void set(double newSaturation) {
        this.saturation = this.clamp(newSaturation, 0.0, 100.0);
        float s = (float)(this.saturation / 100.0);
        this.link.apply(this.link.getHue(), s, this.link.getBrightness());
    }

    public void syncFromConfig() {
        this.link.syncFromConfig();
        this.saturation = (double)this.link.getSaturation() * 100.0;
    }

    public ColorComponent getLink() {
        return this.link;
    }

    public int getCurrentRGB() {
        return this.link.getRGB();
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
