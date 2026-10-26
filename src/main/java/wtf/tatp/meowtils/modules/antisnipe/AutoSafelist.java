package wtf.tatp.meowtils.modules.antisnipe;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.MojangNameToUUID;
import wtf.tatp.meowtils.util.NameUtil;
import wtf.tatp.meowtils.util.SafelistUtil;
import wtf.tatp.meowtils.util.TeamUtil;

public class AutoSafelist
extends Module {
    private BooleanValue autoSafelistMessage;

    public AutoSafelist() {
        super("AutoSafelist", "autoSafelistKey", "autoSafelist", Module.Category.Antisnipe);
        this.tooltip("Automatically safelists players that take a final death.");
        this.autoSafelistMessage = new BooleanValue("Safelist feedback", "autoSafelistMessage");
        this.addBoolean(this.autoSafelistMessage);
    }

    @SubscribeEvent
    public void onChatMessageReceived(ClientChatReceivedEvent event) {
        Matcher matcher;
        if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
            return;
        }
        if (event == null || event.message == null || event.message.getUnformattedText() == null) {
            return;
        }
        String message = event.message.getUnformattedText();
        if (message.contains("FINAL KILL!") && (matcher = Pattern.compile("^([A-Za-z0-9_]+)(?=[\\s'])").matcher(message)).find()) {
            String playerName = matcher.group(1);
            if (TeamUtil.ignoreSelf(playerName)) {
                return;
            }
            if (GamemodeUtil.replay) {
                return;
            }
            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                if (!info.getGameProfile().getName().equalsIgnoreCase(playerName)) continue;
                String uuid = info.getGameProfile().getId().toString();
                if (!SafelistUtil.isSafelisted(uuid)) {
                    SafelistUtil.addToSafelist(uuid);
                    if (cfg.v.autoSafelistMessage) {
                        Meowtils.addMessage(EnumChatFormatting.GREEN + "Auto-safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GREEN + ".");
                    }
                }
                return;
            }
            new Thread(() -> {
                try {
                    MojangNameToUUID lookup = new MojangNameToUUID(playerName);
                    String uuid = lookup.getUUID();
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        if (uuid != null) {
                            if (!SafelistUtil.isSafelisted(uuid)) {
                                SafelistUtil.addToSafelist(uuid);
                                if (cfg.v.autoSafelistMessage) {
                                    Meowtils.addMessage(EnumChatFormatting.GREEN + "Auto-safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GREEN + ".");
                                }
                            }
                        } else if (!SafelistUtil.isSafelisted(playerName)) {
                            SafelistUtil.addToSafelist(playerName);
                            if (cfg.v.autoSafelistMessage) {
                                Meowtils.addMessage(EnumChatFormatting.GREEN + "Auto-safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GREEN + ".");
                            }
                        }
                    });
                }
                catch (IOException e) {
                    Minecraft.getMinecraft().addScheduledTask(() -> Meowtils.addMessage("AutoSafelist: " + EnumChatFormatting.RED + "Error retrieving UUID for " + NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.RED + "."));
                }
            }).start();
        }
    }
}
