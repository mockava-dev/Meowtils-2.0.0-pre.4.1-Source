package wtf.tatp.meowtils.modules.skywars;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
import wtf.tatp.meowtils.util.Render;

public class TimeWarpDisplay
extends Module {
    private BooleanValue others;
    private BooleanValue self;
    private NumberValue scale;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private static float cooldown = 0.0f;
    private static int tickCounter = 0;
    private static boolean thrown = false;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<EntityEnderPearl, EntityPlayer> activePearls = new HashMap<EntityEnderPearl, EntityPlayer>();
    private final List<BoxLocation> activeBoxes = new ArrayList<BoxLocation>();
    private static final long BOX_LIFETIME = 3000L;

    public TimeWarpDisplay() {
        super("TimeWarpDisplay", "timeWarpDisplayKey", "timeWarpDisplay", Module.Category.Skywars);
        this.tooltip("For others - Shows a box at the position a player might warp back to\nFor self - Shows a countdown on screen until you warp back");
        this.color = new ColorComponent("timeWarpDisplay_red", "timeWarpDisplay_green", "timeWarpDisplay_blue");
        this.rgb = new ColorValue("Color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "timeWarpDisplayScale", Float.TYPE);
        this.addValue(this.scale);
        this.self = new BooleanValue("For self", "timeWarpDisplayForSelf");
        this.addBoolean(this.self);
        this.others = new BooleanValue("For others", "timeWarpDisplayForOthers");
        this.addBoolean(this.others);
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        EnumChatFormatting cooldownColor;
        if (this.mc.theWorld == null || this.mc.thePlayer == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        if (!thrown || !cfg.v.timeWarpDisplayForSelf) {
            return;
        }
        float scale = cfg.v.timeWarpDisplayScale;
        int color = new Color(cfg.v.timeWarpDisplay_red, cfg.v.timeWarpDisplay_green, cfg.v.timeWarpDisplay_blue).getRGB();
        cooldownColor = (double)cooldown >= 2.5 ? EnumChatFormatting.DARK_GREEN : (cooldown >= 2.0f ? EnumChatFormatting.GREEN : ((double)cooldown >= 1.5 ? EnumChatFormatting.YELLOW : (cooldown >= 1.0f ? EnumChatFormatting.GOLD : ((double)cooldown >= 0.5 ? EnumChatFormatting.RED : EnumChatFormatting.DARK_RED))));
        if (!cfg.v.smoothFont) {
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)scale, (float)scale, (float)scale);
            this.mc.fontRendererObj.drawStringWithShadow("Time Warp: " + cooldownColor + String.format("%.1f", Float.valueOf(cooldown)).replace(",", "."), (float)cfg.v.timeWarpDisplay_x / scale, (float)cfg.v.timeWarpDisplay_y / scale, color);
            GlStateManager.popMatrix();
        } else {
            Meowtils.fontRenderer.drawScaledStringWithShadow("Time Warp: " + cooldownColor + String.format("%.1f", Float.valueOf(cooldown)).replace(",", "."), cfg.v.timeWarpDisplay_x, cfg.v.timeWarpDisplay_y, color, scale * 10.0f);
        }
    }

    @Override
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityEnderPearl pearl;
        if (event.phase != TickEvent.Phase.END || this.mc.theWorld == null) {
            return;
        }
        if (thrown && cooldown > 0.0f && ++tickCounter >= 2) {
            cooldown -= 0.1f;
            tickCounter = 0;
            if (cooldown <= 0.0f) {
                cooldown = 0.0f;
                thrown = false;
            }
        }
        for (Entity e : this.mc.theWorld.loadedEntityList) {
            EntityPlayer nearest;
            if (!(e instanceof EntityEnderPearl) || this.activePearls.containsKey(e) || (nearest = this.findClosestPlayer(pearl = (EntityEnderPearl)e)) == null) continue;
            this.activePearls.put(pearl, nearest);
        }
        Iterator<Map.Entry<EntityEnderPearl, EntityPlayer>> iterator = this.activePearls.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<EntityEnderPearl, EntityPlayer> entry = iterator.next();
            pearl = entry.getKey();
            EntityPlayer owner = entry.getValue();
            if (!pearl.isDead && this.mc.theWorld.loadedEntityList.contains(pearl)) continue;
            this.activeBoxes.add(new BoxLocation(owner.posX, owner.posY, owner.posZ, System.currentTimeMillis()));
            iterator.remove();
        }
        this.activeBoxes.removeIf(box -> System.currentTimeMillis() - box.spawnTime > 3000L);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.equals("You will be warped back in 3 seconds!")) {
            thrown = true;
            cooldown = 3.0f;
            tickCounter = 0;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!GamemodeUtil.skywarsGame) {
            return;
        }
        if (!cfg.v.timeWarpDisplayForOthers) {
            return;
        }
        for (BoxLocation box : this.activeBoxes) {
            Color color = new Color(cfg.v.timeWarpDisplay_red, cfg.v.timeWarpDisplay_green, cfg.v.timeWarpDisplay_blue, 150);
            double width = 0.6;
            double height = 1.8;
            double halfWidth = width / 2.0;
            AxisAlignedBB bb = new AxisAlignedBB(box.x - halfWidth, box.y, box.z - halfWidth, box.x + halfWidth, box.y + height, box.z + halfWidth);
            Render.draw3DBlockBox(bb, true, color, false, color, 0.0, 0.0, 0.0);
        }
    }

    private EntityPlayer findClosestPlayer(EntityEnderPearl pearl) {
        EntityPlayer closest = null;
        double bestDist = Double.MAX_VALUE;
        for (EntityPlayer p : this.mc.theWorld.playerEntities) {
            double dist = p.getDistanceSqToEntity((Entity)pearl);
            if (!(dist < bestDist)) continue;
            bestDist = dist;
            closest = p;
        }
        return closest;
    }

    private static class BoxLocation {
        public final double x;
        public final double y;
        public final double z;
        public final long spawnTime;

        public BoxLocation(double x, double y, double z, long spawnTime) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.spawnTime = spawnTime;
        }
    }
}
