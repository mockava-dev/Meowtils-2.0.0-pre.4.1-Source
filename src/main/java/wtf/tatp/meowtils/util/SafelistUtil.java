package wtf.tatp.meowtils.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class SafelistUtil {
    private static final File FILE = new File(new File(Minecraft.getMinecraft().mcDataDir, "meowtils"), "meowtilssafelist.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Set<String>>(){}.getType();
    private static Set<String> safelist = new HashSet<String>();

    public static void addToSafelist(String uuidOrName) {
        safelist.add(uuidOrName);
        SafelistUtil.saveSafelist();
    }

    public static boolean isSafelisted(String uuidOrName) {
        return safelist.contains(uuidOrName);
    }

    public static Set<String> getSafelist() {
        return safelist;
    }

    private static void loadSafelist() {
        if (!FILE.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(FILE);){
            safelist = (Set)GSON.fromJson((Reader)reader, TYPE);
            if (safelist == null) {
                safelist = new HashSet<String>();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveSafelist() {
        try (FileWriter writer = new FileWriter(FILE);){
            GSON.toJson(safelist, (Appendable)writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSL() {
        SafelistUtil.saveSafelist();
    }

    public static void removeFromSafelist(String uuidOrName) {
        safelist.remove(uuidOrName);
        SafelistUtil.saveSafelist();
    }

    static {
        SafelistUtil.loadSafelist();
    }
}
