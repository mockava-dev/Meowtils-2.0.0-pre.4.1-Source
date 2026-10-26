package wtf.tatp.meowtils.modules.render;

import java.awt.Color;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.NumberValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.mixins.PlayerControllerMPAccessor;
import wtf.tatp.meowtils.util.ColorUtil;

public class BreakProgress
extends Module {
    private NumberValue scale;
    private BooleanValue dynamicColor;
    private ArrayValue mode;
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private float progress;
    private BlockPos block;
    private String progressStr;

    public BreakProgress() {
        super("BreakProgress", "breakProgressKey", "breakProgress", Module.Category.Render);
        this.tooltip("Displays a countdown until block is broken.");
        this.mode = new ArrayValue("Mode", Arrays.asList("Percentage", "Time"), "breakProgressMode");
        this.addArray(this.mode);
        this.color = new ColorComponent("breakProgress_red", "breakProgress_green", "breakProgress_blue");
        this.rgb = new ColorValue("Text color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "breakProgressScale", Float.TYPE);
        this.addValue(this.scale);
        this.dynamicColor = new BooleanValue("Dynamic color", "breakProgressDynamicColor");
        this.addBoolean(this.dynamicColor);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (this.mc.objectMouseOver == null || this.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            this.resetVariables();
            return;
        }
        this.progress = ((PlayerControllerMPAccessor)this.mc.playerController).getCurBlockDamageMP();
        if (this.progress == 0.0f) {
            this.resetVariables();
            return;
        }
        this.block = this.mc.objectMouseOver.getBlockPos();
        this.updateProgressString();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (this.progress == 0.0f || this.block == null) {
            return;
        }
        double x = (double)this.block.getX() + 0.5 - this.mc.getRenderManager().viewerPosX;
        double y = (double)this.block.getY() + 0.5 - this.mc.getRenderManager().viewerPosY;
        double z = (double)this.block.getZ() + 0.5 - this.mc.getRenderManager().viewerPosZ;
        int r = cfg.v.breakProgress_red;
        int g = cfg.v.breakProgress_green;
        int b = cfg.v.breakProgress_blue;
        int color = !cfg.v.breakProgressDynamicColor ? new Color(r, g, b).getRGB() : (this.progress > 0.9f ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.DARK_RED) : (this.progress > 0.8f ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.RED) : (this.progress > 0.7f ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.GOLD) : (this.progress > 0.6f ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.YELLOW) : new Color(r, g, b).getRGB()))));
        float scale = cfg.v.breakProgressScale;
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)x, (double)y, (double)z);
        GlStateManager.rotate((float)(-this.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)(this.mc.gameSettings.thirdPersonView == 2 ? -1 : 1) * this.mc.getRenderManager().playerViewX), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)-0.02f, (float)-0.02f, (float)-0.02f);
        GlStateManager.depthMask((boolean)false);
        GlStateManager.disableDepth();
        GL11.glEnable((int)3042);
        if (!cfg.v.smoothFont) {
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)scale, (float)scale, (float)scale);
            this.mc.fontRendererObj.drawStringWithShadow(this.progressStr, (float)(-this.mc.fontRendererObj.getStringWidth(this.progressStr)) / 2.0f / scale, -3.0f / scale, color);
            GlStateManager.popMatrix();
        } else {
            Meowtils.fontRenderer.drawScaledStringWithShadow(this.progressStr, -Meowtils.fontRenderer.getStringWidth(this.progressStr, 10.0f) / 2.0f, -3.0f, color, scale * 10.0f);
        }
        GL11.glDisable((int)3042);
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.popMatrix();
    }

    private void updateProgressString() {
        float breakSpeed;
        IBlockState state;
        Block blockObj;
        float blockHardness;
        if (cfg.v.breakProgressMode.equals("Percentage")) {
            this.progressStr = (int)(this.progress * 100.0f) + "%";
        } else if (cfg.v.breakProgressMode.equals("Time") && (blockHardness = (blockObj = (state = this.mc.theWorld.getBlockState(this.block)).getBlock()).getBlockHardness((World)this.mc.theWorld, this.block)) > 0.0f && (breakSpeed = blockObj.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, (World)this.mc.theWorld, this.block)) > 0.0f) {
            int ticks = (int)Math.ceil((1.0f - this.progress) / breakSpeed);
            double timeLeft = (double)ticks / 20.0;
            this.progressStr = String.format("%.1f", timeLeft).replace('.', ',') + "s";
        }
    }

    private void resetVariables() {
        this.progress = 0.0f;
        this.block = null;
        this.progressStr = "";
    }
}
