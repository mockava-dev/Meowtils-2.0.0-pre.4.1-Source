package wtf.tatp.meowtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import org.apache.commons.io.FileUtils;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.util.Prefix;

public class MeowtilsUpdater {
    private static final String API_LATEST = "https://api.github.com/repos/femboytatp/meowtils/releases/latest";
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean verified = false;

    public MeowtilsUpdater() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.verified || this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        Meowtils.addMessage(EnumChatFormatting.GRAY + "Verifying " + EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.BOLD + "Meowtils" + EnumChatFormatting.GRAY + " version...");
        this.verified = true;
        new Thread(this::checkForUpdate, "Meowtils-Updater").start();
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    private void checkForUpdate() {
        try {
            JsonObject release = this.fetchLatest();
            if (release == null) {
                return;
            }
            ComparableVersion current = new ComparableVersion("2.0.0-pre.4.1");
            String latestTag = release.get("tag_name").getAsString();
            ComparableVersion latest = new ComparableVersion(latestTag);
            if (current.compareTo(latest) >= 0) {
                Meowtils.addMessage(EnumChatFormatting.GREEN + "Already using latest " + EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.BOLD + "Meowtils" + EnumChatFormatting.GREEN + " version: " + EnumChatFormatting.GRAY + "2.0.0-pre.4.1");
                return;
            }
            JsonArray assets = release.getAsJsonArray("assets");
            if (assets.size() == 0) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Failed to verify version.");
                return;
            }
            String url = null;
            String name = null;
            for (JsonElement e : assets) {
                JsonObject asset = e.getAsJsonObject();
                if (!asset.get("name").getAsString().endsWith(".jar")) continue;
                url = asset.get("browser_download_url").getAsString();
                name = asset.get("name").getAsString();
                break;
            }
            if (url == null) {
                return;
            }
            Meowtils.addMessage(EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.BOLD + "Meowtils" + EnumChatFormatting.GREEN + " update " + EnumChatFormatting.GRAY + latestTag + EnumChatFormatting.GREEN + " is available!");
            if (cfg.v.autoUpdate) {
                Meowtils.addMessage(EnumChatFormatting.GREEN + "Downloading update...");
                this.download(url, name);
            } else {
                ChatComponentText msg = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "Click to download update!");
                msg.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText(EnumChatFormatting.YELLOW + url))));
                this.mc.thePlayer.addChatMessage((IChatComponent)msg);
            }
        }
        catch (Exception e) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Failed to verify update.");
            e.printStackTrace();
        }
    }

    private void download(String url, String name) {
        try {
            File cacheDirectory = new File(this.mc.mcDataDir, "meowtils/auto_update");
            if (!cacheDirectory.exists()) {
                cacheDirectory.mkdirs();
            }
            File target = new File(cacheDirectory, name);
            FileUtils.copyURLToFile((URL)new URL(url), (File)target);
            this.meowtilsAutoUpdater(target);
            Meowtils.addMessage(EnumChatFormatting.GREEN + "Done!" + EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC + " Update will be applied next launch.");
        }
        catch (Exception e) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Failed to download update.");
            e.printStackTrace();
        }
    }

    private void meowtilsAutoUpdater(File newJar) {
        File autoUpdateJar = new File(this.mc.mcDataDir, "meowtils/auto_update/MeowtilsAutoUpdate.jar");
        if (!autoUpdateJar.exists()) {
            try {
                FileUtils.copyURLToFile((URL)new URL("https://github.com/femboytatp/MeowtilsAutoUpdate/releases/latest/download/MeowtilsAutoUpdate.jar"), (File)autoUpdateJar);
            }
            catch (Exception exception) {
                
            }
        }
        File oldJar = Loader.instance().activeModContainer().getSource();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                new ProcessBuilder(javaBin, "-jar", autoUpdateJar.getAbsolutePath(), oldJar.getAbsolutePath(), newJar.getAbsolutePath()).start();
            }
            catch (Exception exception) {
                
            }
        }));
    }

    private JsonObject fetchLatest() throws Exception {
        HttpURLConnection conn = (HttpURLConnection)new URL(API_LATEST).openConnection();
        conn.setRequestProperty("User-Agent", "Meowtils-Updater");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);){
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            return jsonObject;
        }
    }
}
