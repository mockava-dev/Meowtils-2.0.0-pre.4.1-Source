package wtf.tatp.meowtils.modules.render;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.ColorComponent;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.ArrayValue;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.gui.values.BrightnessValue;
import wtf.tatp.meowtils.gui.values.ColorValue;
import wtf.tatp.meowtils.gui.values.SaturationValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.Render;

public class ChestESP
extends Module {
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorValue rgbOpened;
    private ColorComponent color;
    private ColorComponent openedColor;
    private ArrayValue renderMode;
    private ArrayValue mode;
    private BooleanValue skywarsOnly;
    private final List<BlockPos> highlightedChestPositions = new CopyOnWriteArrayList<BlockPos>();

    public ChestESP() {
        super("ChestESP", "chestESPKey", "chestESP", Module.Category.Render);
        this.tooltip("Highlights chests");
        this.color = new ColorComponent("chestESP_red", "chestESP_green", "chestESP_blue");
        this.openedColor = new ColorComponent("chestESPOpened_red", "chestESPOpened_green", "chestESPOpened_blue");
        this.rgb = new ColorValue("Chest color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.rgbOpened = new ColorValue("Opened color", this.openedColor);
        this.addColor(this.rgbOpened);
        this.mode = new ArrayValue("Mode", Arrays.asList("Opened", "Normal", "Both"), "chestESPMode");
        this.addArray(this.mode);
        this.renderMode = new ArrayValue("Render", Arrays.asList("Full", "Outline"), "chestESPRenderMode");
        this.addArray(this.renderMode);
        this.skywarsOnly = new BooleanValue("Skywars only", "chestESPSkywarsOnly");
        this.addBoolean(this.skywarsOnly);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.entityPlayer != this.mc.thePlayer) {
            return;
        }
        BlockPos pos = event.pos;
        TileEntity tile = this.mc.theWorld.getTileEntity(pos);
        if (tile instanceof TileEntityChest && !this.highlightedChestPositions.contains(pos)) {
            this.highlightedChestPositions.add(pos);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.highlightedChestPositions.clear();
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (cfg.v.chestESPSkywarsOnly && !GamemodeUtil.skywarsGame && !GamemodeUtil.skywarsMiniGame) {
            return;
        }
        boolean full = cfg.v.chestESPRenderMode.equals("Full");
        boolean outline = cfg.v.chestESPRenderMode.equals("Outline");
        int r = cfg.v.chestESP_red;
        int g = cfg.v.chestESP_green;
        int b = cfg.v.chestESP_blue;
        String mode = cfg.v.chestESPMode;
        for (TileEntity tile : this.mc.theWorld.loadedTileEntityList) {
            Color outlineColor;
            Color fillColor;
            AxisAlignedBB box;
            if (!(tile instanceof TileEntityChest)) continue;
            BlockPos pos = tile.getPos();
            if (mode.equals("Opened") && !this.highlightedChestPositions.contains(pos) || (box = this.mc.theWorld.getBlockState(pos).getBlock().getSelectedBoundingBox((World)this.mc.theWorld, pos)) == null) continue;
            box = Render.getAdjustedBox(box, this.mc.theWorld.getBlockState(pos).getBlock());
            boolean opened = this.highlightedChestPositions.contains(pos);
            if (mode.equals("Opened")) {
                fillColor = new Color(r, g, b, 120);
                outlineColor = new Color(r, g, b, 255);
            } else if (mode.equals("Normal")) {
                fillColor = new Color(r, g, b, 120);
                outlineColor = new Color(r, g, b, 255);
            } else {
                if (!mode.equals("Both")) continue;
                if (opened) {
                    fillColor = new Color(cfg.v.chestESPOpened_red, cfg.v.chestESPOpened_green, cfg.v.chestESPOpened_blue, 120);
                    outlineColor = new Color(cfg.v.chestESPOpened_red, cfg.v.chestESPOpened_green, cfg.v.chestESPOpened_blue, 255);
                } else {
                    fillColor = new Color(r, g, b, 120);
                    outlineColor = new Color(r, g, b, 255);
                }
            }
            Render.draw3DBlockBox(box, full, fillColor, outline, outlineColor, 0.06, 0.06, 0.06);
        }
    }
}
