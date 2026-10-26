package wtf.tatp.meowtils.modules.render;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.modules.advanced.AntiBot;

public class HealthInfo
extends Module {
    private BooleanValue display;
    private NumberValue displayScale;
    private BooleanValue indicator;
    private NumberValue indicatorScale;
    private BooleanValue esp;
    private BooleanValue espWalls;
    private float indicatorHealth = 0.0f;
    private long lastIndicator = 0L;
    private String espMode = "Raven";
    private Field timerField;

    public HealthInfo() {
        super("HealthInfo", "healthInfoKey", "healthInfo", Module.Category.Render);
        this.tooltip("Displays various health information.");
        this.displayScale = new NumberValue("Display scale", 0.5, 1.5, 0.05, null, "healthInfoDisplayScale", Float.TYPE);
        this.addValue(this.displayScale);
        this.indicatorScale = new NumberValue("Indicator scale", 0.5, 5.0, 0.05, null, "healthInfoIndicatorScale", Float.TYPE);
        this.addValue(this.indicatorScale);
        this.display = new BooleanValue("Show own health", "healthInfoDisplay");
        this.addBoolean(this.display);
        this.indicator = new BooleanValue("Show damage indicators", "healthInfoIndicator");
        this.addBoolean(this.indicator);
        this.esp = new BooleanValue("Show healthbars", "healthESP");
        this.addBoolean(this.esp);
        this.espWalls = new BooleanValue("\u00a77^ Healthbar through walls", "healthESPThroughWalls");
        this.addBoolean(this.espWalls);
        this.initializeTimerField();
    }

    private void initializeTimerField() {
        if (this.timerField != null) {
            return;
        }
        try {
            this.timerField = Minecraft.class.getDeclaredField("field_71428_T");
            this.timerField.setAccessible(true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (mc.currentScreen != null) {
            return;
        }
        if (cfg.v.healthInfoDisplay) {
            String heart;
            int hearts = (int)mc.thePlayer.getHealth();
            int absorptionHearts = (int)mc.thePlayer.getAbsorptionAmount();
            boolean isAbsorptionActive = mc.thePlayer.isPotionActive(Potion.absorption);
            int health = isAbsorptionActive ? hearts + absorptionHearts : hearts;
            float scale = cfg.v.healthInfoDisplayScale;
            String string = heart = health < 4 ? "\u2763" : "\u2764";
            EnumChatFormatting healthColor = health < 3 ? EnumChatFormatting.DARK_RED : (health < 6 ? EnumChatFormatting.RED : (health < 10 ? EnumChatFormatting.YELLOW : (health < 15 ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_GREEN)));
            EnumChatFormatting heartColor = isAbsorptionActive ? EnumChatFormatting.GOLD : EnumChatFormatting.RED;
            String text = healthColor + String.valueOf(health) + heartColor + heart;
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                mc.fontRendererObj.drawStringWithShadow(text, (float)cfg.v.healthInfoDisplay_x / scale, (float)cfg.v.healthInfoDisplay_y / scale, -1);
                GlStateManager.popMatrix();
            } else {
                Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.healthInfoDisplay_x, cfg.v.healthInfoDisplay_y, -1, scale * 10.0f);
            }
        }
        if (cfg.v.healthInfoIndicator && System.currentTimeMillis() - this.lastIndicator <= 2000L) {
            float scale = cfg.v.healthInfoIndicatorScale;
            EnumChatFormatting healthColor = this.indicatorHealth < 3.0f ? EnumChatFormatting.DARK_RED : (this.indicatorHealth < 6.0f ? EnumChatFormatting.RED : (this.indicatorHealth < 10.0f ? EnumChatFormatting.YELLOW : (this.indicatorHealth < 15.0f ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_GREEN)));
            String text = healthColor + String.valueOf(this.indicatorHealth) + " HP";
            if (!cfg.v.smoothFont) {
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)scale, (float)scale, (float)scale);
                mc.fontRendererObj.drawStringWithShadow(text, (float)cfg.v.healthInfoIndicator_x / scale, (float)cfg.v.healthInfoIndicator_y / scale, -1);
                GlStateManager.popMatrix();
            } else {
                Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.healthInfoIndicator_x, cfg.v.healthInfoIndicator_y, -1, scale * 10.0f);
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!cfg.v.healthInfoIndicator) {
            return;
        }
        String msg = event.message.getUnformattedText();
        Pattern pattern = Pattern.compile("is on ([0-9]+(?:\\.[0-9]+)?) HP!");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find() && !msg.contains(":")) {
            String indicatorString = matcher.group(1);
            this.indicatorHealth = Float.parseFloat(indicatorString);
            this.lastIndicator = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!this.isWorldReady()) {
            return;
        }
        if (!cfg.v.healthESP) {
            return;
        }
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            if (cfg.v.antiBot && AntiBot.isBot(player) || player == this.mc.thePlayer || player.deathTime != 0) continue;
            this.renderHealthBar(player);
        }
    }

    private void renderHealthBar(EntityPlayer player) {
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)this.getRenderPartialTicks() - this.mc.getRenderManager().viewerPosX;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)this.getRenderPartialTicks() - this.mc.getRenderManager().viewerPosY;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)this.getRenderPartialTicks() - this.mc.getRenderManager().viewerPosZ;
        double healthRatio = player.getHealth() / player.getMaxHealth();
        int barHeight = (int)(74.0 * healthRatio);
        barHeight = Math.max(0, Math.min(barHeight, 74));
        int ravenBarColor = healthRatio < 0.3 ? Color.red.getRGB() : (healthRatio < 0.5 ? Color.orange.getRGB() : (healthRatio < 0.7 ? Color.yellow.getRGB() : Color.green.getRGB()));
        if (this.espMode.equals("Raven")) {
            boolean depthEnabled = GL11.glIsEnabled((int)2929);
            GlStateManager.pushMatrix();
            GL11.glTranslated((double)x, (double)(y - 0.2), (double)z);
            GL11.glRotatef((float)(-this.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
            if (cfg.v.healthESPThroughWalls) {
                GlStateManager.disableDepth();
            }
            GL11.glScalef((float)0.03f, (float)0.03f, (float)0.03f);
            Gui.drawRect((int)21, (int)-1, (int)25, (int)75, (int)Color.black.getRGB());
            Gui.drawRect((int)22, (int)barHeight, (int)24, (int)74, (int)Color.darkGray.getRGB());
            Gui.drawRect((int)22, (int)0, (int)24, (int)barHeight, (int)ravenBarColor);
            if (depthEnabled) {
                GlStateManager.enableDepth();
            } else {
                GlStateManager.disableDepth();
            }
            GlStateManager.popMatrix();
        }
    }

    private float getRenderPartialTicks() {
        if (this.timerField == null) {
            this.initializeTimerField();
        }
        if (this.timerField != null) {
            try {
                Timer timer = (Timer)this.timerField.get(this.mc);
                return timer.renderPartialTicks;
            }
            catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return 0.0f;
    }

    private boolean isWorldReady() {
        return this.mc.thePlayer != null && this.mc.theWorld != null;
    }
}
