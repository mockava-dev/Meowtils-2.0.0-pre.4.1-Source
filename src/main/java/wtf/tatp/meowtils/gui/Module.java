package wtf.tatp.meowtils.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ModuleManager;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.gui.values.OpacityValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.util.Wrapper;

public abstract class Module {
    protected Minecraft mc;
    private String name;
    private int key;
    private boolean state;
    private Category category;
    private ArrayList<ColorValue> rgb;
    private ArrayList<SaturationValue> saturation;
    private ArrayList<BrightnessValue> brightness;
    private ArrayList<BooleanValue> booleans;
    private ArrayList<ArrayValue> arrays;
    private ArrayList<NumberValue> values;
    private ArrayList<OpacityValue> opacity;
    public String tooltip;
    protected String moduleName;
    private boolean alwaysEnabled = false;
    private final Field keyField;
    private final Field stateField;
    private final Object configInstance = cfg.v;

    public Module(String name, String keyFieldName, String stateFieldName, Category category) {
        this.mc = Wrapper.getMinecraft();
        this.booleans = new ArrayList();
        this.values = new ArrayList();
        this.arrays = new ArrayList();
        this.rgb = new ArrayList();
        this.saturation = new ArrayList();
        this.brightness = new ArrayList();
        this.opacity = new ArrayList();
        this.name = name;
        this.state = false;
        this.category = category;
        this.tooltip = null;
        this.moduleName = name;
        int tempKey = 0;
        boolean tempState = false;
        Field tmpKeyField = null;
        Field tmpStateField = null;
        try {
            Object value;
            if (keyFieldName != null && !keyFieldName.isEmpty()) {
                tmpKeyField = this.configInstance.getClass().getField(keyFieldName);
                tmpKeyField.setAccessible(true);
                value = tmpKeyField.get(this.configInstance);
                if (value instanceof Integer) {
                    tempKey = (Integer)value;
                }
            }
            if (stateFieldName != null && !stateFieldName.isEmpty()) {
                tmpStateField = this.configInstance.getClass().getField(stateFieldName);
                tmpStateField.setAccessible(true);
                value = tmpStateField.get(this.configInstance);
                if (value instanceof Boolean) {
                    tempState = (Boolean)value;
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to link config fields: " + keyFieldName + ", " + stateFieldName, e);
        }
        this.key = tempKey;
        this.keyField = tmpKeyField;
        this.stateField = tmpStateField;
        if (tempState) {
            this.setState(true);
        }
    }

    public Module(String name, String keyFieldName, String stateFieldName, Category category, boolean alwaysEnabled) {
        this(name, keyFieldName, stateFieldName, category);
        this.alwaysEnabled = alwaysEnabled;
        if (alwaysEnabled) {
            this.state = true;
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
    }

    public void setState(boolean enabled) {
        if (this.alwaysEnabled && !enabled) {
            return;
        }
        if (this.state == enabled) {
            return;
        }
        this.state = enabled;
        if (enabled) {
            MinecraftForge.EVENT_BUS.register((Object)this);
            this.onEnable();
        } else {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            this.onDisable();
        }
        if (!this.alwaysEnabled && this.stateField != null) {
            try {
                this.stateField.set(this.configInstance, enabled);
                cfg.save();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!this.alwaysEnabled && Minecraft.getMinecraft().thePlayer != null && cfg.v.toggleNotifications) {
            String message = this.state ? this.moduleName + Meowtils.onMessage : this.moduleName + Meowtils.offMessage;
            Meowtils.addMessage(message);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(int key) {
        this.key = key;
        if (this.keyField != null) {
            try {
                this.keyField.set(this.configInstance, key);
                cfg.save();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean setToggled(boolean toggled) {
        this.state = toggled;
        return this.state;
    }

    public boolean getState() {
        return this.state;
    }

    public int getKey() {
        return this.key;
    }

    public Category getCategory() {
        return this.category;
    }

    public ArrayList<BooleanValue> getBooleans() {
        return this.booleans;
    }

    public ArrayList<ArrayValue> getArrays() {
        return this.arrays;
    }

    public ArrayList<NumberValue> getValues() {
        return this.values;
    }

    public ArrayList<ColorValue> getRgb() {
        return this.rgb;
    }

    public ArrayList<SaturationValue> getSaturation() {
        return this.saturation;
    }

    public ArrayList<BrightnessValue> getBrightness() {
        return this.brightness;
    }

    public ArrayList<OpacityValue> getOpacity() {
        return this.opacity;
    }

    public void toggle() {
        this.setState(!this.state);
    }

    public void addBoolean(BooleanValue booleans) {
        this.booleans.add(booleans);
    }

    public void addArray(ArrayValue array) {
        this.arrays.add(array);
    }

    public void addValue(NumberValue values) {
        this.values.add(values);
    }

    public void addColor(ColorValue colorValue) {
        this.rgb.add(colorValue);
    }

    public void addSaturation(SaturationValue saturationValue) {
        this.saturation.add(saturationValue);
    }

    public void addBrightness(BrightnessValue brightnessValue) {
        this.brightness.add(brightnessValue);
    }

    public void addOpacity(OpacityValue opacityValue) {
        this.opacity.add(opacityValue);
    }

    public static ArrayList<Module> getCategoryModules(Category cat) {
        ArrayList<Module> modsInCategory = new ArrayList<Module>();
        for (Module mod : ModuleManager.getModules()) {
            if (mod.getCategory() != cat) continue;
            modsInCategory.add(mod);
        }
        return modsInCategory;
    }

    public static Module getModule(Class<? extends Module> clazz) {
        for (Module mod : ModuleManager.getModules()) {
            if (mod.getClass() != clazz) continue;
            return mod;
        }
        return null;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onClientTick(TickEvent.ClientTickEvent e) {
    }

    public void onRenderTick(TickEvent.RenderTickEvent e) {
    }

    public void onRenderText(RenderGameOverlayEvent.Post e) throws IOException {
    }

    public S18PacketEntityTeleport onEntityTeleport(S18PacketEntityTeleport packet) {
        return packet;
    }

    public void onRenderEvent(RenderWorldLastEvent e) {
    }

    public Module tooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public static enum Category {
        Meowtils,
        Hypixel,
        Skywars,
        Bedwars,
        Render,
        Antisnipe,
        Utility,
        Advanced;

    }
}
