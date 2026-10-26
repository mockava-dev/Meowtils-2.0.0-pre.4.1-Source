package wtf.tatp.meowtils.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangNameToUUID {
    private final String name;
    private String uuid;

    public MojangNameToUUID(String name) throws IOException {
        this.name = name;
        this.lookupUUID();
    }

    private void lookupUUID() throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + this.name);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setReadTimeout(5000);
        connection.connect();
        if (connection.getResponseCode() == 200) {
            JsonObject response = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            String rawId = response.get("id").getAsString();
            this.uuid = rawId.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        } else {
            this.uuid = null;
        }
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
}
