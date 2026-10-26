package wtf.tatp.meowtils.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.MojangNameToUUID;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.SafelistUtil;

public class UnSafelistCommand
extends CommandBase {
    public String getCommandName() {
        return "unsafelist";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("usl", "slr", "safelistremove", "unsl");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /unsafelist <player>");
            return;
        }
        String playername = args[0];
        for (NetworkPlayerInfo netInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playername)) continue;
            String uuid = netInfo.getGameProfile().getId().toString();
            if (SafelistUtil.isSafelisted(uuid)) {
                SafelistUtil.removeFromSafelist(uuid);
                Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the safelist.");
            } else {
                Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is not in the safelist.");
            }
            return;
        }
        new Thread(() -> {
            try {
                MojangNameToUUID lookup = new MojangNameToUUID(playername);
                String uuid = lookup.getUUID();
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    if (uuid != null && SafelistUtil.isSafelisted(uuid)) {
                        SafelistUtil.removeFromSafelist(uuid);
                        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the safelist.");
                    } else if (SafelistUtil.isSafelisted(playername)) {
                        SafelistUtil.removeFromSafelist(playername);
                        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the safelist.");
                    } else {
                        Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is not in the safelist.");
                    }
                });
            }
            catch (IOException e) {
                Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RED + "Error retrieving UUID"));
            }
        }).start();
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            ArrayList<String> names = new ArrayList<String>();
            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                names.add(info.getGameProfile().getName());
            }
            return UnSafelistCommand.getListOfStringsMatchingLastWord((String[])args, names);
        }
        return null;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
