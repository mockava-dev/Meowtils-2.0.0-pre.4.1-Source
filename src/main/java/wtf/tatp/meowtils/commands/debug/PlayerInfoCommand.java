package wtf.tatp.meowtils.commands.debug;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.util.CommandUtil;

public class PlayerInfoCommand
extends CommandBase {
    public String getCommandName() {
        return "playerinfo";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("info", "pi", "playeri", "pinfo");
    }

    private String state(boolean value) {
        return value ? EnumChatFormatting.GREEN + "true" : EnumChatFormatting.RED + "false";
    }

    
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String playerName;
        Minecraft mc = Minecraft.getMinecraft();
        if (args.length == 0) {
            if (mc.thePlayer == null) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Could not determine your username.");
                return;
            }
            playerName = mc.thePlayer.getName();
        } else {
            playerName = args[0];
        }
        EntityPlayer player = mc.theWorld.getPlayerEntityByName(playerName);
        if (player == null) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Player not found in world.");
            return;
        }
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(player.getUniqueID());
        if (info == null) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Could not retrieve tablist info for " + playerName);
            return;
        }
        Meowtils.addMessage(EnumChatFormatting.GOLD + "Player Info: " + player.getName());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Name: " + EnumChatFormatting.WHITE + player.getName());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "UUID: " + EnumChatFormatting.WHITE + player.getUniqueID());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Skin Texture: " + EnumChatFormatting.WHITE + info.getLocationSkin());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Cape Texture: " + EnumChatFormatting.WHITE + info.getLocationCape());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Position: " + EnumChatFormatting.WHITE + String.format("%.2f, %.2f, %.2f", player.posX, player.posY, player.posZ));
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Rotation: " + EnumChatFormatting.WHITE + String.format("%.2f, %.2f", Float.valueOf(player.rotationYaw), Float.valueOf(player.rotationPitch)));
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "On Ground: " + this.state(player.onGround));
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Sneaking: " + this.state(player.isSneaking()));
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Health: " + EnumChatFormatting.WHITE + player.getHealth());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Absorption: " + EnumChatFormatting.WHITE + player.getAbsorptionAmount());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Dead: " + this.state(player.isDead));
        ItemStack held = player.getHeldItem();
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Held Item: " + EnumChatFormatting.WHITE + (held != null ? held.getDisplayName() : "None"));
        if (held != null && held.hasTagCompound()) {
            Meowtils.addMessage(EnumChatFormatting.GRAY + "  NBT: " + held.getTagCompound().toString());
        }
        for (ItemStack armorPiece : player.inventory.armorInventory) {
            if (armorPiece == null) continue;
            Meowtils.addMessage(EnumChatFormatting.YELLOW + "Armor: " + EnumChatFormatting.WHITE + armorPiece.getDisplayName());
            if (!armorPiece.hasTagCompound()) continue;
            Meowtils.addMessage(EnumChatFormatting.GRAY + "  NBT: " + armorPiece.getTagCompound().toString());
        }
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Ping: " + EnumChatFormatting.WHITE + info.getResponseTime() + "ms");
        IChatComponent displayName = info.getDisplayName();
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Scoreboard Name: " + EnumChatFormatting.WHITE + (displayName != null ? displayName.getFormattedText() : player.getName()));
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Gamemode: " + EnumChatFormatting.WHITE + info.getGameType());
        Meowtils.addMessage(EnumChatFormatting.YELLOW + "Tablist Visible: " + this.state(info.getGameProfile().isComplete()));
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandUtil.tabCompletePlayerNames(sender, args);
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
