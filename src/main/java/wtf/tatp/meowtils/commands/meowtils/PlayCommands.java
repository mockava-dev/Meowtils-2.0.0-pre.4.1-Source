package wtf.tatp.meowtils.commands.meowtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;

public class PlayCommands
extends CommandBase {
    public String getCommandName() {
        return "playcommands";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String separator = " \u00bb ";
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/1s or /bw1" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_eight_one");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/2s or /bw2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_eight_two");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/3s or /bw3" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_four_three");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/4s or /bw4" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_four_four");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/4v4 or /bw4v4" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_four_two");
        Meowtils.addCleanMessage("");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sw" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play solo_normal");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sw2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play teams_normal");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /si" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play solo_insane");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sm" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play mini_normal");
        Meowtils.addCleanMessage("");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /mw" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play mw_standard");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /mwfaceoff" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play mw_face_off");
        Meowtils.addCleanMessage("");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /blitz" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play blitz_solo_normal");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /blitz2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play blitz_teams_normal");
        Meowtils.addCleanMessage("");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /tntrun" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play tnt_tntrun");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /tnttag" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play tnt_tntag");
        Meowtils.addCleanMessage("");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /classic" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_classic_duel");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /swduel" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_sw_duel");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /uhcduel" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_uhc_duel");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sumo" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_sumo_duel");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge1" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_duel");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_doubles");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge3" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_threes");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge4" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_four");
        Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /blitz" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_blitz_duel");
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
