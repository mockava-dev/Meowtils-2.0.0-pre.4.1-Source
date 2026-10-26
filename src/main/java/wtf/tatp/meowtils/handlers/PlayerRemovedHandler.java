package wtf.tatp.meowtils.handlers;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class PlayerRemovedHandler {
    private static final int MAX_TICKS = 10;
    private static int ticksRemaining = 0;
    private static final Set<String> previousPlayerNames = new HashSet<String>();
    private static boolean registerOnTick = false;
    private static PlayerRemovedHandler instance;
    private int tickCounter = 0;

    public static void startTracking() {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (registerOnTick) {
            return;
        }
        ticksRemaining = 10;
        previousPlayerNames.clear();
        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
            previousPlayerNames.add(player.getName());
        }
        instance = new PlayerRemovedHandler();
        MinecraftForge.EVENT_BUS.register((Object)instance);
        registerOnTick = true;
    }

    public static void stopTracking() {
        if (!registerOnTick || instance == null) {
            return;
        }
        MinecraftForge.EVENT_BUS.unregister((Object)instance);
        instance = null;
        registerOnTick = false;
        previousPlayerNames.clear();
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.equals("A player has been removed from your game.") && cfg.v.playerRemovedMessage) {
            PlayerRemovedHandler.startTracking();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        ++this.tickCounter;
        if (this.tickCounter < 20) {
            return;
        }
        this.tickCounter = 0;
        --ticksRemaining;
        HashSet<String> currentPlayerNames = new HashSet<String>();
        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
            currentPlayerNames.add(player.getName());
        }
        for (String name : previousPlayerNames) {
            if (currentPlayerNames.contains(name)) continue;
            Meowtils.addMessage(EnumChatFormatting.RED + "Player removed: " + EnumChatFormatting.GRAY + name);
            PlayerRemovedHandler.stopTracking();
            return;
        }
        if (ticksRemaining <= 0) {
            PlayerRemovedHandler.stopTracking();
        }
    }

    public static void clear() {
        previousPlayerNames.clear();
    }
}
