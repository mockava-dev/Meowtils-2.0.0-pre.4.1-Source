package wtf.tatp.meowtils.gui;

import java.awt.Color;
import java.lang.reflect.Field;
import wtf.tatp.meowtils.config.cfg;

public class ColorComponent {
    private final Field redField;
    private final Field greenField;
    private final Field blueField;
    private final Object configInstance;
    private float hue;
    private float saturation;
    private float brightness;

    public ColorComponent(String red, String green, String blue) {
        try {
            this.configInstance = cfg.v;
            this.redField = this.configInstance.getClass().getField(red);
            this.greenField = this.configInstance.getClass().getField(green);
            this.blueField = this.configInstance.getClass().getField(blue);
            this.redField.setAccessible(true);
            this.greenField.setAccessible(true);
            this.blueField.setAccessible(true);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to link config field: ", e);
        }
        this.syncFromConfig();
    }

    public void syncFromConfig() {
        try {
            int r = this.redField.getInt(this.configInstance);
            int g = this.greenField.getInt(this.configInstance);
            int b = this.blueField.getInt(this.configInstance);
            float[] hsb = Color.RGBtoHSB(r, g, b, null);
            this.hue = hsb[0];
            this.saturation = hsb[1];
            this.brightness = hsb[2];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apply(float hue, float sat, float bri) {
        this.hue = hue;
        this.saturation = sat;
        this.brightness = bri;
        try {
            int rgb = Color.HSBtoRGB(hue, sat, bri) & 0xFFFFFF;
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            this.redField.setInt(this.configInstance, r);
            this.greenField.setInt(this.configInstance, g);
            this.blueField.setInt(this.configInstance, b);
            cfg.save();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getHue() {
        return this.hue;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public int getPureHueRGB() {
        return Color.HSBtoRGB(this.hue, 1.0f, 1.0f) & 0xFFFFFF;
    }

    public int getRGB() {
        int rgb = Color.HSBtoRGB(this.hue, this.saturation, this.brightness) & 0xFFFFFF;
        return rgb;
    }
}
