package wtf.tatp.meowtils.commands.meowtils;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import wtf.tatp.meowtils.Meowtils;

public class MeowtilsCommand
extends CommandBase {
    private static final int PAGES = 4;

    public String getCommandName() {
        return "meow";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/meow [page]";
    }

    public List<String> getCommandAliases() {
        return Arrays.asList("meowtils", "mtils");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException numberFormatException) {
                
            }
        }
        if (page < 1) {
            page = 1;
        }
        if (page > 4) {
            page = 4;
        }
        ChatComponentText header = new ChatComponentText("");
        if (page > 1) {
            ChatComponentText left = new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "                             \u00ab");
            left.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/meow " + (page - 1)));
            header.appendSibling((IChatComponent)left);
        } else {
            header.appendText(EnumChatFormatting.DARK_PURPLE + "                             \u00ab");
        }
        ChatComponentText label = new ChatComponentText(EnumChatFormatting.DARK_PURPLE + " Meowtils " + page + " ");
        header.appendSibling((IChatComponent)label);
        if (page < 4) {
            ChatComponentText right = new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "\u00bb");
            right.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/meow " + (page + 1)));
            header.appendSibling((IChatComponent)right);
        } else {
            header.appendText(EnumChatFormatting.DARK_PURPLE + "\u00bb");
        }
        mc.thePlayer.addChatComponentMessage((IChatComponent)header);
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "--------------------------------------------------");
        this.sendPageContent(page);
        Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "--------------------------------------------------");
    }

    private void sendPageContent(int page) {
        String separator = " \u00bb ";
        switch (page) {
            case 1: {
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meow <page>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Meowtils help");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/playcommands" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show all short play commands");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/shortcuts" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show all other shortcut commands");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/blacklist | /bl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Add to blacklist");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/unblacklist | /ubl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Remove from blacklist");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/safelist | /sl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Add to safelist");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/unsafelist | /usl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Remove from safelist");
                break;
            }
            case 2: {
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/namemc | /nmc <ign>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Link to NameMC");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/plancke | /pla <ign>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Link to Plancke");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/rq" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Requeues last played Hypixel game");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/capefolder" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Opens cape folder");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowcolor" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show all formatting codes");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/fakemsg <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Display a fake message in chat");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/s <ign>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show stats for player");
                break;
            }
            case 3: {
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/theme" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Change custom prefix color");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/anticheat" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Change anticheat alert color");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowapi <key>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set API key");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autogg1 <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set 1st AutoGG message");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autogg2 <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set 2nd AutoGG message");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autogl <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set AutoGL message");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autotext | /at <1-10> <msg|clear>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set AutoText message");
                break;
            }
            case 4: {
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowdebug" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Toggle Meowtils debug mode");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowanticheatinfo" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Extra clientside AntiCheat info");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/playerinfo <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Shows info about player");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/send <msg|command>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Send message directly to server");
                Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/resetgui" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Resets GUI positions");
            }
        }
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
