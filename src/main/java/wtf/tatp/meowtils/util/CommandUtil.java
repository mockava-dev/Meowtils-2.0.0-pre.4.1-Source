package wtf.tatp.meowtils.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandUtil {
    public static List<String> tabCompletePlayerNames(ICommandSender sender, String[] args) {
        ArrayList<String> names = new ArrayList<String>();
        for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            names.add(info.getGameProfile().getName());
        }
        return CommandBase.getListOfStringsMatchingLastWord((String[])args, names);
    }
}
