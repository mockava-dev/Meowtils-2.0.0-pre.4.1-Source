package wtf.tatp.meowtils.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;

public class CapeFolderCommand
extends CommandBase {
    public String getCommandName() {
        return "capefolder";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        File capeDirectory = new File("meowtils/custom_cape");
        try {
            if (Desktop.isDesktopSupported()) {
                Meowtils.addMessage(EnumChatFormatting.GREEN + "Opening cape folder.");
                Desktop.getDesktop().open(capeDirectory);
            } else {
                Meowtils.addMessage(EnumChatFormatting.RED + "Failed to open cape folder on this system!");
                Meowtils.addMessage(EnumChatFormatting.GRAY + "Open it manually: " + EnumChatFormatting.YELLOW + "<this minecraft instance folder>/meowtils/custom_cape/");
            }
        }
        catch (IOException e) {
            Meowtils.addMessage(EnumChatFormatting.RED + "Failed to open cape folder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
