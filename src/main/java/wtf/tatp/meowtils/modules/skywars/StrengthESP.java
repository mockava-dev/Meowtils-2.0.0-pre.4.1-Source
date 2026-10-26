package wtf.tatp.meowtils.modules.skywars;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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

public class StrengthESP
extends Module {
    private static final Map<String, Long> recentKillers = new HashMap<String, Long>();
    private ColorValue rgb;
    private SaturationValue saturation;
    private BrightnessValue brightness;
    private ColorComponent color;
    private BooleanValue fillBox;
    private BooleanValue fade;
    private ArrayValue mode;

    public StrengthESP() {
        super("StrengthESP", "strengthESPKey", "strengthESP", Module.Category.Skywars);
        this.tooltip("Renders a box on players who have the Strength effect.");
        this.color = new ColorComponent("strengthESP_red", "strengthESP_green", "strengthESP_blue");
        this.rgb = new ColorValue("ESP color", this.color);
        this.addColor(this.rgb);
        this.saturation = new SaturationValue(this.color);
        this.addSaturation(this.saturation);
        this.brightness = new BrightnessValue(this.color);
        this.addBrightness(this.brightness);
        this.fillBox = new BooleanValue("Fill entire box", "strengthESPFillBox");
        this.addBoolean(this.fillBox);
        this.fade = new BooleanValue("Fade out", "strengthESPFade");
        this.addBoolean(this.fade);
        this.mode = new ArrayValue("Mode", Arrays.asList("3D", "2D"), "strengthESPMode");
        this.addArray(this.mode);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        Entity viewEntity = this.mc.getRenderViewEntity();
        if (!(viewEntity instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase camEntity = (EntityLivingBase)viewEntity;
        double camX = camEntity.lastTickPosX + (camEntity.posX - camEntity.lastTickPosX) * (double)event.partialTicks;
        double camY = camEntity.lastTickPosY + (camEntity.posY - camEntity.lastTickPosY) * (double)event.partialTicks;
        double camZ = camEntity.lastTickPosZ + (camEntity.posZ - camEntity.lastTickPosZ) * (double)event.partialTicks;
        long currentTime = System.currentTimeMillis();
        boolean use3D = cfg.v.strengthESPMode.equalsIgnoreCase("3D");
        boolean fillBox = cfg.v.strengthESPFillBox;
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            long elapsed;
            String name;
            Long killTime;
            if (player == this.mc.thePlayer || player.isInvisible() || (killTime = recentKillers.get(name = player.getName())) == null || (elapsed = currentTime - killTime) > 5000L) continue;
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.partialTicks - camX;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.partialTicks - camY;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.partialTicks - camZ;
            int alpha = 150;
            if (cfg.v.strengthESPFade) {
                float fadeProgress = (float)elapsed / 5000.0f;
                alpha = (int)(150.0f * (1.0f - fadeProgress));
            }
            Color color = new Color(cfg.v.strengthESP_red, cfg.v.strengthESP_green, cfg.v.strengthESP_blue, alpha);
            if (use3D) {
                Render.draw3DEntityBox((Entity)player, fillBox, color, true, color, 0.1, 0.1, 0.1);
                continue;
            }
            float width = 0.5f;
            float height = width * 2.0f;
            Render.draw2DEntityBox(x, y + (double)(player.height / 2.0f), z, width, height, color, fillBox);
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        Matcher matcher = Pattern.compile("(?:was killed by|was thrown into the void by|was thrown off a cliff by|was shot by|was crushed into moon dust by|was sent the wrong way by|was blasted to the moon by|was hit by an asteroid from|got rekt by|took the L to|got dabbed on by|got bamboozled by|was bit by|got WOOF'D by|was growled off an edge by|was thrown a frisbee by|was crusaded by the knight|was jousted by|was catapulted by|was shot to the knee by|was socked by|was KO'd by|took an uppercut from|was sent into a daze by|was mushed by|was peeled by|slipped on|got banana pistol'd by|was exterminated by|was scared off an edge by|was squashed by|was tranquilized by|was glazed in BBQ sauce by|slipped in BBQ sauce off the edge spilled by|was not spicy enough for|was thrown chili powder at by|was deleted by|was ALT\\+F4'd by|was crashed by|was rm -rf by|was turned into space dust by|was sent into orbit by|was retrograded by|be sent to Davy Jones' locker by|be cannonballed to death by|be voodooed by|be shot and killed by|was given the cold shoulder by|was out of the league of|heart was broken by|was struck with Cupid's arrow by|was filled full of lead by|met their end by|lost a drinking contest with|lost the draw to|was struck down by|was turned to dust by|was turned to ash by|was melted by|was put on the naughty list by|was pushed down the chimney by|was traded in for milk and cookies by|was turned to gingerbread by|was ripped to shreds by|was charged by|was ripped and thrown by|was pounced on by|was smothered in holiday cheer by|was banished into the ether by|was pushed by|was sniped by a missile of festivity by|was wrapped up by|was tied into a bow by|tripped over a present placed by|was glued up by|was whacked with a party balloon by|was popped into the void by|was launched like a firework by|was shot with a roman candle by|was painted pretty by|was flipped off the edge by|was deviled by|was made sunny side up by|was locked outside during a snow storm by|was shoved down an icy slope by|was made into a snowman by|was hit with a snowball from|was backstabbed by|was pushed into the abyss by|was thrown into a ravine by|was brutally shot by|was trampled by|was back kicked into the void by|was headbutted off a cliff by|was impaled from a distance by|was buzzed to death by|was bzzz'd off the edge by|was stung by|was startled from a distance by|was oinked by|slipped into void for|was distracted by a piglet from|got attacked by a carrot from|was chewed up by|was squeaked off the edge by|was distracted by a rat dragging pizza from|was squeaked from a distance by|of|died in close combat to|fought to the edge with|stumbled off a ledge with help by|fell to the great marksmanship of) (\\w+)(?!'s)", 2).matcher(message);
        if (matcher.find() && GamemodeUtil.skywarsGame) {
            String playerName = matcher.group(1);
            recentKillers.put(playerName, System.currentTimeMillis());
        }
    }

    public static void clear() {
        recentKillers.clear();
    }
}
