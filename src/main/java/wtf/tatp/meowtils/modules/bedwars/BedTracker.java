package wtf.tatp.meowtils.modules.bedwars;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
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
import wtf.tatp.meowtils.modules.advanced.AntiBot;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.PlaySound;
import wtf.tatp.meowtils.util.TeamUtil;

public class BedTracker
extends Module {
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private NumberValue scale;
    private NumberValue cooldown;
    private NumberValue maxDistance;
    private BooleanValue sound;
    private BooleanValue hud;
    private static BlockPos bedPos = null;
    private static long bedScanTime = 0L;
    private static long startTime = 0L;
    private static boolean rangeAlert = false;
    private static final Map<UUID, Long> lastAlertTimes = new HashMap<UUID, Long>();

    public BedTracker() {
        super("BedTracker", "bedTrackerKey", "bedTracker", Module.Category.Bedwars);
        this.tooltip("Alerts when enemy players are near your bed and shows distance to your bed on screen.\nShows warning icon after distance if bed is out of render.");
        this.color = new ColorComponent("bedTracker_red", "bedTracker_green", "bedTracker_blue");
        this.rgb = new ColorValue("Text color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.scale = new NumberValue("Scale", 0.5, 1.5, 0.05, null, "bedTrackerScale", Float.TYPE);
        this.addValue(this.scale);
        this.cooldown = new NumberValue("Alert frequency", 5.0, 30.0, 1.0, "s", "bedTrackerAlertCooldown", Integer.TYPE);
        this.addValue(this.cooldown);
        this.maxDistance = new NumberValue("Max distance", 10.0, 100.0, 5.0, "m", "bedTrackerMaxDistance", Integer.TYPE);
        this.addValue(this.maxDistance);
        this.sound = new BooleanValue("Ping sound", "bedTrackerSound");
        this.addBoolean(this.sound);
        this.hud = new BooleanValue("Show HUD", "bedTrackerHUD");
        this.addBoolean(this.hud);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (!msg.contains(":")) {
            if (msg.contains("The game starts in 1 second!")) {
                bedPos = null;
                bedScanTime = System.currentTimeMillis() + 6000L;
                startTime = System.currentTimeMillis() + 7000L;
            } else if (msg.contains("You will respawn because you still have a bed!")) {
                bedPos = null;
                bedScanTime = System.currentTimeMillis() + 12000L;
                startTime = System.currentTimeMillis() + 13000L;
            }
            Meowtils.debugMessage(EnumChatFormatting.YELLOW + "[BedTracker]" + EnumChatFormatting.WHITE + "Locating bed..");
        }
        if (msg.startsWith("BED DESTRUCTION > Your Bed")) {
            bedPos = null;
            Meowtils.addMessage(EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + "\u26a0 Your bed was destroyed!");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (bedPos == null && bedScanTime > 0L && System.currentTimeMillis() > bedScanTime) {
            bedPos = this.findNearbyBed((World)this.mc.theWorld, this.mc.thePlayer.getPosition(), 25);
            bedScanTime = 0L;
            if (bedPos != null) {
                Meowtils.addMessage(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "\u2713 " + EnumChatFormatting.RESET + "Whitelisted your bed at" + EnumChatFormatting.GRAY + " (" + EnumChatFormatting.GREEN + bedPos.getX() + ", " + bedPos.getY() + ", " + bedPos.getZ() + EnumChatFormatting.GRAY + ")");
            } else {
                Meowtils.addMessage(EnumChatFormatting.RED + "\u26a0 Error locating your bed.");
            }
        }
        if (bedPos != null && this.isBedOutOfRange() && !rangeAlert) {
            Meowtils.addMessage(EnumChatFormatting.BOLD.toString() + EnumChatFormatting.LIGHT_PURPLE + "\u26a0 Your bed is out of range!");
            rangeAlert = true;
        } else if (!this.isBedOutOfRange() && bedPos != null) {
            rangeAlert = false;
        }
        if (bedPos != null && this.mc.theWorld != null && !TeamUtil.inSpectator()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime < 6000L) {
                return;
            }
            for (EntityPlayer player : this.mc.theWorld.playerEntities) {
                UUID uuid2;
                long lastAlert;
                int distanceToBed;
                if (player == this.mc.thePlayer || AntiBot.isBot(player) || TeamUtil.ignoreTeam(player.getName()) || player.capabilities.isFlying || player.ticksExisted < 100 || (distanceToBed = (int)player.getDistance((double)bedPos.getX(), (double)bedPos.getY(), (double)bedPos.getZ())) > cfg.v.bedTrackerMaxDistance || currentTime - (lastAlert = lastAlertTimes.getOrDefault(uuid2 = player.getUniqueID(), 0L).longValue()) < (long)cfg.v.bedTrackerAlertCooldown * 1000L) continue;
                String distanceColor = distanceToBed <= 5 ? EnumChatFormatting.DARK_RED.toString() : (distanceToBed <= 15 ? EnumChatFormatting.RED.toString() : (distanceToBed <= 30 ? EnumChatFormatting.GOLD.toString() : (distanceToBed <= 40 ? EnumChatFormatting.YELLOW.toString() : EnumChatFormatting.GREEN.toString())));
                Meowtils.addMessage(NameUtil.getTabDisplayName(player.getName()) + EnumChatFormatting.WHITE + " is " + distanceColor + distanceToBed + EnumChatFormatting.WHITE + " blocks from your bed!" + distanceColor + " \u26a0");
                if (cfg.v.notify && cfg.v.notifyBedTracker) {
                    NotifyUtil.notifyParty(player.getName() + " is " + distanceToBed + " blocks from your bed! \u26a0");
                }
                lastAlertTimes.put(uuid2, currentTime);
                if (!cfg.v.bedTrackerSound) continue;
                PlaySound.getInstance().playMeowSound();
            }
            lastAlertTimes.keySet().removeIf(uuid -> this.mc.theWorld.getPlayerEntityByUUID(uuid) == null);
        }
    }

    @Override
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        if (!cfg.v.bedTrackerHUD) {
            return;
        }
        float scale = cfg.v.bedTrackerScale;
        String cross = EnumChatFormatting.RED + "\u2717";
        String checkMark = EnumChatFormatting.GREEN + "\u2713";
        String separator = bedPos != null ? EnumChatFormatting.GRAY + " | " + EnumChatFormatting.RESET : "";
        int color = new Color(cfg.v.bedTracker_red, cfg.v.bedTracker_green, cfg.v.bedTracker_blue).getRGB();
        String bedState = bedPos == null ? cross : checkMark;
        String bed = "Bed: ";
        EnumChatFormatting distanceColor = this.getDistanceToBed() < 70 ? EnumChatFormatting.GREEN : (this.isBedOutOfRange() ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW);
        String distance = bedPos == null ? "" : "Distance: " + distanceColor + this.getDistanceToBed();
        String doesChunkExist = this.isBedOutOfRange() && bedPos != null ? EnumChatFormatting.RED + " \u26a0" : "";
        String text = bed + bedState + separator + distance + doesChunkExist;
        if (!cfg.v.smoothFont) {
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)scale, (float)scale, (float)scale);
            this.mc.fontRendererObj.drawStringWithShadow(text, (float)cfg.v.bedTracker_x / scale, (float)cfg.v.bedTracker_y / scale, color);
            GlStateManager.popMatrix();
        } else {
            Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.bedTracker_x, cfg.v.bedTracker_y, color, scale * 10.0f);
        }
    }

    private BlockPos findNearbyBed(World world, BlockPos center, int radius) {
        for (int x = center.getX() - radius; x <= center.getX() + radius; ++x) {
            for (int y = center.getY() - radius; y <= center.getY() + radius; ++y) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!(world.getBlockState(pos).getBlock() instanceof BlockBed)) continue;
                    return pos;
                }
            }
        }
        return null;
    }

    private int getDistanceToBed() {
        if (bedPos == null) {
            return 0;
        }
        Vec3 playerPos = this.mc.thePlayer.getPositionVector();
        return (int)Math.sqrt(playerPos.squareDistanceTo(new Vec3((double)bedPos.getX() + 0.5, (double)bedPos.getY(), (double)bedPos.getZ() + 0.5)));
    }

    private boolean isBedOutOfRange() {
        if (bedPos == null || this.mc.thePlayer == null || this.mc.theWorld == null) {
            return true;
        }
        int chunkX = bedPos.getX() >> 4;
        int chunkZ = bedPos.getZ() >> 4;
        boolean serverOutOfRange = !this.mc.theWorld.getChunkProvider().chunkExists(chunkX, chunkZ);
        int renderDistanceBlocks = this.mc.gameSettings.renderDistanceChunks * 16;
        double dx = this.mc.thePlayer.posX - (double)bedPos.getX();
        double dz = this.mc.thePlayer.posZ - (double)bedPos.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        boolean clientOutOfRange = horizontalDistance > (double)renderDistanceBlocks;
        return serverOutOfRange || clientOutOfRange;
    }

    public static void clear() {
        bedPos = null;
        bedScanTime = 0L;
        startTime = 0L;
        lastAlertTimes.clear();
    }
}
