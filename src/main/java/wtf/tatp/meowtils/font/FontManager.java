package wtf.tatp.meowtils.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class FontManager {
    
    public static Font loadTTF(String path, float size) {
        try (InputStream is = FontManager.class.getResourceAsStream(path);){
            Font font2 = Font.createFont(0, is).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font2);
            Font font = font2;
            return font;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
