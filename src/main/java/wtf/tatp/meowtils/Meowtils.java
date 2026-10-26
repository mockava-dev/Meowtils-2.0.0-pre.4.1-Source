package wtf.tatp.meowtils;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import wtf.tatp.meowtils.MeowtilsAlert;
import wtf.tatp.meowtils.MeowtilsUpdater;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.font.FontManager;
import wtf.tatp.meowtils.font.FontRenderer;
import wtf.tatp.meowtils.gui.ClickGUI;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.ModuleManager;
import wtf.tatp.meowtils.handlers.BlacklistHandler;
import wtf.tatp.meowtils.handlers.KeybindHandler;
import wtf.tatp.meowtils.handlers.MapClearer;
import wtf.tatp.meowtils.handlers.PartyHandler;
import wtf.tatp.meowtils.handlers.PlayerRemovedHandler;
import wtf.tatp.meowtils.handlers.QueueHandler;
import wtf.tatp.meowtils.util.ColorUtil;
import wtf.tatp.meowtils.util.DeathTracker;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.NotifyUtil;
import wtf.tatp.meowtils.util.Prefix;
import wtf.tatp.meowtils.util.RegisterCommand;
import wtf.tatp.meowtils.util.TeamUtil;
import wtf.tatp.meowtils.util.Wrapper;
import wtf.tatp.meowtils.util.anticheat.AntiCheatData;

@Mod(modid="meowtils", name="Meowtils", version="2.0.0-pre.4.1")
public class Meowtils {
    public static final String MODID = "meowtils";
    public static final String MODNAME = "Meowtils";
    public static final String VERSION = "2.0.0-pre.4.1";
    public static final Logger LOGGER = LogManager.getLogger((String)"Meowtils");
    public static FontRenderer fontRenderer;
    public static final File MEOWTILS_DIR;
    public static final File CUSTOM_CAPE_DIR;
    public static final File ITEM_DIR;
    public static final File ITEMBLACKLIST;
    public static final File ITEMSAFELIST;
    public static final File AUTO_UPDATE_DIR;
    private static ClickGUI clickGUI;
    public static Minecraft mc;
    public static Meowtils INSTANCE;
    public static final String onMessage;
    public static final String offMessage;

    public static boolean nullCheck() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc != null && mc.theWorld != null && mc.thePlayer != null && mc.getNetHandler() != null && mc.gameSettings != null;
    }

    public static String addMessage(String message) {
        if (!Meowtils.nullCheck()) {
            return message;
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.RESET + message));
        return message;
    }

    public static String debugMessage(String message) {
        if (!cfg.v.debugMessages) {
            return null;
        }
        if (!Meowtils.nullCheck()) {
            return message;
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.RESET + message));
        return message;
    }

    public static String addCleanMessage(String message) {
        if (!Meowtils.nullCheck()) {
            return message;
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
        return message;
    }

    public static String sendMessage(String message) {
        if (!Meowtils.nullCheck()) {
            return message;
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + ColorUtil.unformattedText(Prefix.getPrefix()) + "\u00bb " + message);
        return message;
    }

    public static String sendCleanMessage(String message) {
        if (!Meowtils.nullCheck()) {
            return message;
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
        return message;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        cfg.initFile(new File(event.getModConfigurationDirectory(), "meowtils.json"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        cfg.load();
        Font font = FontManager.loadTTF("/assets/meowtils/fonts/font.ttf", 32.0f);
        fontRenderer = new FontRenderer(font);
        RegisterCommand.registerCommands();
        KeybindHandler.init();
        MinecraftForge.EVENT_BUS.register((Object)this);
        MinecraftForge.EVENT_BUS.register((Object)new GamemodeUtil());
        MinecraftForge.EVENT_BUS.register((Object)new KeybindHandler());
        MinecraftForge.EVENT_BUS.register((Object)new QueueHandler());
        MinecraftForge.EVENT_BUS.register((Object)new BlacklistHandler());
        MinecraftForge.EVENT_BUS.register((Object)new PlayerRemovedHandler());
        MinecraftForge.EVENT_BUS.register((Object)new AntiCheatData());
        MinecraftForge.EVENT_BUS.register((Object)new DeathTracker());
        MinecraftForge.EVENT_BUS.register((Object)new MapClearer());
        MinecraftForge.EVENT_BUS.register((Object)new MeowtilsUpdater());
        MinecraftForge.EVENT_BUS.register((Object)new TeamUtil());
        MinecraftForge.EVENT_BUS.register((Object)new PartyHandler());
        MinecraftForge.EVENT_BUS.register((Object)new MeowtilsAlert());
        MinecraftForge.EVENT_BUS.register((Object)new NotifyUtil());
        clickGUI = new ClickGUI();
        if (!MEOWTILS_DIR.exists()) {
            MEOWTILS_DIR.mkdirs();
        }
        if (!AUTO_UPDATE_DIR.exists()) {
            AUTO_UPDATE_DIR.mkdirs();
        }
        if (!CUSTOM_CAPE_DIR.exists()) {
            CUSTOM_CAPE_DIR.mkdirs();
        }
        if (!ITEM_DIR.exists()) {
            ITEM_DIR.mkdirs();
        }
        if (!ITEMBLACKLIST.exists()) {
            ITEMBLACKLIST.getParentFile().mkdirs();
            try {
                ITEMBLACKLIST.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!ITEMSAFELIST.exists()) {
            ITEMSAFELIST.getParentFile().mkdirs();
            try {
                ITEMSAFELIST.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        mc = Minecraft.getMinecraft();
    }

    public static ClickGUI getClickGUI() {
        return clickGUI;
    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (Wrapper.getPlayer() != null) {
            if (!Keyboard.getEventKeyState()) {
                return;
            }
            int key = Keyboard.getEventKey();
            if (key == 0 || key < 0 || key > 256) {
                return;
            }
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getKey() != key) continue;
                mod.setState(!mod.getState());
            }
        }
    }

    public static Meowtils getInstance() {
        return INSTANCE;
    }

    static {
        MEOWTILS_DIR = new File(Minecraft.getMinecraft().mcDataDir, MODID);
        CUSTOM_CAPE_DIR = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/custom_cape");
        ITEM_DIR = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/items");
        ITEMBLACKLIST = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/items/itemblacklist.json");
        ITEMSAFELIST = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/items/itemsafelist.json");
        AUTO_UPDATE_DIR = new File(Minecraft.getMinecraft().mcDataDir, "meowtils/auto_update");
        onMessage = EnumChatFormatting.WHITE + ":" + EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + " ON";
        offMessage = EnumChatFormatting.WHITE + ":" + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + " OFF";
        INSTANCE = new Meowtils();
    }
}
