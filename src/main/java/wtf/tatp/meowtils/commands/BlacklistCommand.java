package wtf.tatp.meowtils.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

public class BlacklistCommand
extends CommandBase {
    public String getCommandName() {
        return "blacklist";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("bl");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /blacklist <player> <reason>");
            return;
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /blacklist info <player>");
                return;
            }
            String playername = args[1];
            for (NetworkPlayerInfo netInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playername)) continue;
                String uuid = netInfo.getGameProfile().getId().toString();
                if (!BlacklistUtil.isBlacklistedByUUID(uuid)) break;
                String entry = BlacklistUtil.getEntry(uuid);
                this.sendBlacklistInfo(sender, playername, entry);
                return;
            }
            new Thread(() -> {
                try {
                    MojangNameToUUID lookup = new MojangNameToUUID(playername);
                    String uuid = lookup.getUUID();
                    if (uuid != null && BlacklistUtil.isBlacklistedByUUID(uuid)) {
                        String entry = BlacklistUtil.getEntry(uuid);
                        Minecraft.getMinecraft().addScheduledTask(() -> this.sendBlacklistInfo(sender, playername, entry));
                    } else if (BlacklistUtil.isBlacklisted(playername)) {
                        String entry = BlacklistUtil.getEntry(playername);
                        Minecraft.getMinecraft().addScheduledTask(() -> this.sendBlacklistInfo(sender, playername, entry));
                    } else {
                        Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is not in the blacklist."));
                    }
                }
                catch (IOException e) {
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RED + "Error retrieving UUID"));
                }
            }).start();
            return;
        }
        String playername = args[0];
        String reason = args.length > 1 ? this.reasonPreserve(Arrays.copyOfRange(args, 1, args.length)) : "cheating";
        for (NetworkPlayerInfo netInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playername)) continue;
            String uuid = netInfo.getGameProfile().getId().toString();
            if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
                Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is already in the blacklist!");
                return;
            }
            BlacklistUtil.addToBlacklist(uuid, reason);
            Meowtils.addMessage(EnumChatFormatting.GREEN + "Blacklisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " for: " + BlacklistUtil.getFormattedReason(reason));
            return;
        }
        new Thread(() -> {
            try {
                MojangNameToUUID lookup = new MojangNameToUUID(playername);
                String uuid = lookup.getUUID();
                if (uuid != null) {
                    if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
                        Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is already in the blacklist!"));
                    } else {
                        BlacklistUtil.addToBlacklist(uuid, reason);
                        Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.GREEN + "Blacklisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " for: " + BlacklistUtil.getFormattedReason(reason)));
                    }
                } else if (BlacklistUtil.isBlacklisted(playername)) {
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is already in the blacklist!"));
                } else {
                    BlacklistUtil.addToBlacklist(playername, reason);
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.GREEN + "Blacklisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " by name for: " + BlacklistUtil.getFormattedReason(reason)));
                }
            }
            catch (IOException e) {
                Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RED + "Error retrieving UUID"));
            }
        }).start();
    }

    private void sendBlacklistInfo(ICommandSender sender, String playername, String entry) {
        long timeMillis;
        if (entry == null) {
            Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is not in the blacklist.");
            return;
        }
        String[] split = entry.split(" ", 2);
        String timestamp = split[0];
        String reason = split.length > 1 ? split[1] : "No reason provided";
        try {
            timeMillis = Long.parseLong(timestamp);
        }
        catch (NumberFormatException e) {
            Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is blacklisted (couldn't find timestamp) for: " + BlacklistUtil.getFormattedReason(reason));
            return;
        }
        String formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeMillis));
        Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is blacklisted since " + EnumChatFormatting.GRAY + formattedTime + EnumChatFormatting.RED + " for: " + BlacklistUtil.getFormattedReason(reason));
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            ArrayList<String> names = new ArrayList<String>();
            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                names.add(info.getGameProfile().getName());
            }
            return BlacklistCommand.getListOfStringsMatchingLastWord((String[])args, names);
        }
        return null;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    private String reasonPreserve(String[] reasonParts) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < reasonParts.length; ++i) {
            String combined;
            String current = reasonParts[i].toLowerCase();
            if (i < reasonParts.length - 1 && (BlacklistUtil.NON_BLATANT.contains(combined = current + " " + reasonParts[i + 1].toLowerCase()) || BlacklistUtil.BLATANT.contains(combined))) {
                result.add(combined);
                ++i;
                continue;
            }
            result.add(current);
        }
        return String.join((CharSequence)" | ", result);
    }
}
