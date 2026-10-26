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

public class SafelistCommand
extends CommandBase {
    public String getCommandName() {
        return "safelist";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("sl");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /safelist <player>");
            return;
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /safelist info <player>");
                return;
            }
            String playername = args[1];
            for (NetworkPlayerInfo netInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playername)) continue;
                String uuid = netInfo.getGameProfile().getId().toString();
                if (!SafelistUtil.isSafelisted(uuid)) break;
                Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is safelisted.");
                return;
            }
            new Thread(() -> {
                try {
                    MojangNameToUUID lookup = new MojangNameToUUID(playername);
                    String uuid = lookup.getUUID();
                    boolean safelisted = uuid != null && SafelistUtil.isSafelisted(uuid) || SafelistUtil.isSafelisted(playername);
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        if (safelisted) {
                            Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is already in the safelist!");
                        } else {
                            Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is not in the safelist.");
                        }
                    });
                }
                catch (IOException e) {
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage(EnumChatFormatting.RED + "Error retrieving UUID"));
                }
            }).start();
            return;
        }
        String playername = args[0];
        for (NetworkPlayerInfo netInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (!netInfo.getGameProfile().getName().equalsIgnoreCase(playername)) continue;
            String uuid = netInfo.getGameProfile().getId().toString();
            if (SafelistUtil.isSafelisted(uuid)) {
                Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is already safelisted.");
                return;
            }
            SafelistUtil.addToSafelist(uuid);
            Meowtils.addMessage(EnumChatFormatting.GREEN + "Safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + ".");
            return;
        }
        new Thread(() -> {
            try {
                MojangNameToUUID lookup = new MojangNameToUUID(playername);
                String uuid = lookup.getUUID();
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    if (uuid != null) {
                        if (SafelistUtil.isSafelisted(uuid)) {
                            Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is already safelisted.");
                        } else {
                            SafelistUtil.addToSafelist(uuid);
                            Meowtils.addMessage(EnumChatFormatting.GREEN + "Safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + ".");
                        }
                    } else if (SafelistUtil.isSafelisted(playername)) {
                        Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is already safelisted.");
                    } else {
                        SafelistUtil.addToSafelist(playername);
                        Meowtils.addMessage(EnumChatFormatting.GREEN + "Safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " by name.");
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
            return SafelistCommand.getListOfStringsMatchingLastWord((String[])args, names);
        }
        return null;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
