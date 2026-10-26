package wtf.tatp.meowtils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class RequeueCommand
extends CommandBase {
    public String getCommandName() {
        return "rq";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (!cfg.v.lastPlayCommand.isEmpty()) {
            Meowtils.sendCleanMessage("/play " + cfg.v.lastPlayCommand);
        } else {
            Meowtils.addMessage(EnumChatFormatting.RED + "No previously saved /play command found.");
        }
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
