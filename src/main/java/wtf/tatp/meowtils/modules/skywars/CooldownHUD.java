package wtf.tatp.meowtils.modules.skywars;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import wtf.tatp.meowtils.DelayedTask;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.util.GamemodeUtil;

public class CooldownHUD
extends Module {
    private NumberValue scale;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private BooleanValue hide;
    private ColorComponent color;
    private static int cooldown = 0;
    private static boolean ready = false;
    private static boolean thrownPearl = false;
    private static int tickCounter = 0;
    private String lastActionbar = "";
    private static String itemName = "";

    public CooldownHUD() {
        super("CooldownHUD", "cooldownHUDKey", "cooldownHUD", Module.Category.Skywars);
        this.tooltip("Display item cooldowns on screen.");
        this.color = new ColorComponent("cooldownHUDRed", "cooldownHUDGreen", "cooldownHUDBlue");
        this.rgb = new ColorValue("Text color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "cooldownHUDScale", Float.TYPE);
        this.addValue(this.scale);
        this.hide = new BooleanValue("Hide after cooldown", "cooldownHUDHideWhenReady");
        this.addBoolean(this.hide);
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        if (mc.currentScreen != null) {
            return;
        }
        if ((ready || thrownPearl) && cfg.v.cooldownHUDHideWhenReady) {
            return;
        }
        float scale = cfg.v.cooldownHUDScale;
        if (!itemName.isEmpty()) {
            String displayCooldown = ready ? EnumChatFormatting.GREEN + "Ready" : (thrownPearl ? EnumChatFormatting.RED + "Thrown" : EnumChatFormatting.RED.toString() + cooldown + "s");
            int color = new Color(cfg.v.cooldownHUDRed, cfg.v.cooldownHUDGreen, cfg.v.cooldownHUDBlue).getRGB();
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                mc.fontRendererObj.drawStringWithShadow(itemName + displayCooldown, (float)cfg.v.cooldownHUDxPos / scale, (float)cfg.v.cooldownHUDyPos / scale, color);
                GlStateManager.popMatrix();
            } else {
                Meowtils.fontRenderer.drawScaledStringWithShadow(itemName + displayCooldown, cfg.v.cooldownHUDxPos, cfg.v.cooldownHUDyPos, color, scale * 10.0f);
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (event.type == 2) {
            this.lastActionbar = message;
        } else if (event.type == 0 && message.equals("The game starts in 1 second!")) {
            if (this.lastActionbar.contains("Enderman")) {
                new DelayedTask(() -> this.startCooldown("Corrupt Pearl: ", 30), 20);
            } else if (this.lastActionbar.contains("Enderchest")) {
                new DelayedTask(() -> this.startCooldown("Enderchest: ", 60), 20);
            } else if (this.lastActionbar.contains("End Lord")) {
                new DelayedTask(() -> this.startCooldown("End Lord: ", 30), 20);
            } else if (this.lastActionbar.contains("Cryomancer")) {
                new DelayedTask(() -> this.startCooldown("Ice Bridge: ", 30), 20);
            } else if (this.lastActionbar.contains("Chronobreaker")) {
                new DelayedTask(() -> this.startCooldown("Echo: ", 8), 20);
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (cooldown > 0 && ++tickCounter >= 20) {
            tickCounter = 0;
            if (--cooldown <= 0) {
                cooldown = 0;
                ready = true;
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (Mouse.isButtonDown((int)1)) {
            ItemStack held = mc.thePlayer.getHeldItem();
            if (held != null && held.getItem() == Items.ender_pearl && held.hasEffect() && itemName.equals("Corrupt Pearl: ")) {
                if (ready) {
                    thrownPearl = true;
                    ready = false;
                }
            } else if (held != null && held.getItem() == Items.clock && itemName.equals("Echo: ") && ready) {
                ready = false;
                this.startCooldown("Echo: ", 40);
            }
        }
    }

    public void startCooldown(String name, int seconds) {
        itemName = name;
        cooldown = seconds;
        ready = false;
        tickCounter = 0;
    }

    public static void clear() {
        cooldown = 0;
        ready = false;
        thrownPearl = false;
        tickCounter = 0;
        itemName = "";
    }
}
