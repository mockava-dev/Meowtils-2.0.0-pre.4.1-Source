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
import wtf.tatp.meowtils.util.BlacklistUtil;
import wtf.tatp.meowtils.util.MojangNameToUUID;
import wtf.tatp.meowtils.util.NameUtil;

public class UnBlacklistCommand
extends CommandBase {
    public String getCommandName() {
        return "unblacklist";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("ubl", "blr", "blacklistremove", "unbl");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /unblacklist <player>");
            return;
        }
        String playername = args[0];
        for (NetworkPlayerInfo netInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playername)) continue;
            String uuid = netInfo.getGameProfile().getId().toString();
            if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
                BlacklistUtil.removeFromBlacklist(uuid);
                Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the blacklist.");
            } else {
                Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is not in the blacklist.");
            }
            return;
        }
        new Thread(() -> {
            try {
                MojangNameToUUID lookup = new MojangNameToUUID(playername);
                String uuid = lookup.getUUID();
                if (uuid != null && BlacklistUtil.isBlacklistedByUUID(uuid)) {
                    BlacklistUtil.removeFromBlacklist(uuid);
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the blacklist."));
                } else if (BlacklistUtil.isBlacklisted(playername)) {
                    BlacklistUtil.removeFromBlacklist(playername);
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the blacklist."));
                } else {
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is not in the blacklist."));
                }
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
            return UnBlacklistCommand.getListOfStringsMatchingLastWord((String[])args, names);
        }
        return null;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
