package wtf.tatp.meowtils.gui.values;

import wtf.tatp.meowtils.gui.ColorComponent;

public class ColorValue {
    private final String name;
    private final double min = 0.0;
    private final double max = 360.0;
    private double hue;
    private final ColorComponent link;

    public ColorValue(String name, ColorComponent link) {
        this.name = name;
        this.link = link;
        this.hue = (double)link.getHue() * 360.0;
    }

    public double get() {
        return this.hue;
    }

    public String getName() {
        return this.name;
    }

    public void set(double newHue) {
        this.hue = this.clamp(newHue, 0.0, 360.0);
        float h = (float)(this.hue / 360.0);
        this.link.apply(h, this.link.getSaturation(), this.link.getBrightness());
    }

    public ColorComponent getLink() {
        return this.link;
    }

    public void syncFromConfig() {
        this.link.syncFromConfig();
        this.hue = (double)this.link.getHue() * 360.0;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
