package wtf.tatp.meowtils.util;

public class StripFormattingCodes {
    public static String stripFormattingCodes(String input) {
        return input.replaceAll("\u00a7[0-9a-fk-or]", "");
    }
}
