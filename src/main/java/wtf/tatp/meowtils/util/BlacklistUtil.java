package wtf.tatp.meowtils.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.util.SafelistUtil;

public class BlacklistUtil {
    private static final File FILE = new File(new File(Minecraft.getMinecraft().mcDataDir, "meowtils"), "meowtilsblacklist.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Map<String, String>>(){}.getType();
    private static Map<String, String> blacklist = new HashMap<String, String>();
    public static final String RED_ICON = EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + "\u26a0 " + EnumChatFormatting.RESET;
    public static final String GOLD_ICON = EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "\u26a0 " + EnumChatFormatting.RESET;
    public static final String GREEN_ICON = EnumChatFormatting.DARK_GREEN.toString() + EnumChatFormatting.BOLD + "\u26a0 " + EnumChatFormatting.RESET;
    public static final String LIGHT_PURPLE_ICON = EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD + "\u26a0 " + EnumChatFormatting.RESET;
    public static final String CHECK_ICON = EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "\u2713 " + EnumChatFormatting.RESET;
    public static final Set<String> NON_BLATANT = new HashSet<String>(Arrays.asList("cheating", "aimassist", "autoclicker", "reach", "velocity", "esp", "fakelag", "legit scaffold", "fastplace", "closet"));
    public static final Set<String> BLATANT = new HashSet<String>(Arrays.asList("antivoid", "bednuker", "fly", "keepsprint", "killaura", "nofall", "noslow", "scaffold", "bhop", "antifireball", "safewalk", "blink", "autoblock", "blatant", "strafe"));

    public static void addToBlacklist(String uuidOrName, String reason) {
        blacklist.put(uuidOrName, System.currentTimeMillis() + " " + reason);
        BlacklistUtil.saveBlacklist();
    }

    public static boolean isBlacklisted(String uuidOrName) {
        return blacklist.containsKey(uuidOrName);
    }

    public static Map<String, String> getBlacklist() {
        return blacklist;
    }

    private static void loadBlacklist() {
        if (!FILE.exists()) {
            return;
        }
        boolean modified = false;
        try (FileReader reader = new FileReader(FILE);){
            JsonElement jsonElement = new JsonParser().parse(reader);
            if (jsonElement.isJsonObject()) {
                blacklist = (Map)GSON.fromJson(jsonElement, TYPE);
            } else if (jsonElement.isJsonArray()) {
                blacklist.clear();
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    String line;
                    String[] parts;
                    if (!element.isJsonPrimitive() || (parts = (line = element.getAsString().trim()).split(" ", 3)).length < 3) continue;
                    String uuid = parts[0];
                    String timestamp = parts[1];
                    String reasonsRaw = parts[2];
                    String formattedReasons = String.join((CharSequence)" | ", reasonsRaw.split("\\s+")).replaceAll("\\[.*?\\]", "").trim();
                    blacklist.put(uuid, timestamp + " " + formattedReasons);
                    modified = true;
                }
                if (modified) {
                    BlacklistUtil.saveBlacklist();
                }
            } else {
                System.err.println("[Meowtils] Unknown JSON format.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveBlacklist() {
        try (FileWriter writer = new FileWriter(FILE);){
            GSON.toJson(blacklist, (Appendable)writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBlacklistedByUUID(String uuid) {
        return blacklist.containsKey(uuid);
    }

    public static String getEntry(String uuidOrName) {
        return blacklist.get(uuidOrName);
    }

    public static void saveBL() {
        BlacklistUtil.saveBlacklist();
    }

    public static void removeFromBlacklist(String key) {
        BlacklistUtil.getBlacklist().remove(key);
        BlacklistUtil.saveBlacklist();
    }

    public static String getFormattedIcon(String uuid, String name) {
        boolean isSafelisted;
        boolean isBlacklisted = BlacklistUtil.isBlacklistedByUUID(uuid) || BlacklistUtil.isBlacklisted(name);
        boolean bl = isSafelisted = SafelistUtil.isSafelisted(uuid) || SafelistUtil.isSafelisted(name);
        if (!isBlacklisted && !isSafelisted) {
            return null;
        }
        if (isBlacklisted && isSafelisted) {
            return LIGHT_PURPLE_ICON;
        }
        if (isSafelisted) {
            return CHECK_ICON;
        }
        String entry = BlacklistUtil.getEntry(uuid);
        if (entry == null) {
            entry = BlacklistUtil.getEntry(name);
        }
        if (entry == null) {
            return GREEN_ICON;
        }
        String[] parts = entry.split(" ", 2);
        String reasons = parts.length > 1 ? parts[1].toLowerCase() : "";
        for (String reason : reasons.split(" \\| ")) {
            if (!BLATANT.contains(reason.trim())) continue;
            return RED_ICON;
        }
        for (String reason : reasons.split(" \\| ")) {
            if (!NON_BLATANT.contains(reason.trim())) continue;
            return GOLD_ICON;
        }
        return GREEN_ICON;
    }

    public static String getFormattedReason(String reason) {
        StringBuilder formatted = new StringBuilder();
        String[] parts = reason.split(" \\| ");
        for (int i = 0; i < parts.length; ++i) {
            String part = parts[i].trim().toLowerCase();
            if (NON_BLATANT.contains(part)) {
                formatted.append(EnumChatFormatting.GOLD).append(part);
            } else if (BLATANT.contains(part)) {
                formatted.append(EnumChatFormatting.DARK_RED).append(part);
            } else {
                formatted.append(EnumChatFormatting.DARK_GREEN).append(part);
            }
            if (i >= parts.length - 1) continue;
            formatted.append(EnumChatFormatting.DARK_GRAY).append(" | ");
        }
        return formatted.toString();
    }

    static {
        BlacklistUtil.loadBlacklist();
    }
}
