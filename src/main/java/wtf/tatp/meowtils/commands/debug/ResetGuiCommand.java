package wtf.tatp.meowtils.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;

public class ResetGuiCommand
extends CommandBase {
    public String getCommandName() {
        return "resetgui";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        cfg.v.meowtilsCategoryX = 5;
        cfg.v.meowtilsCategoryY = 5;
        cfg.v.hypixelCategoryX = 90;
        cfg.v.hypixelCategoryY = 5;
        cfg.v.skywarsCategoryX = 175;
        cfg.v.skywarsCategoryY = 5;
        cfg.v.bedwarsCategoryX = 260;
        cfg.v.bedwarsCategoryY = 5;
        cfg.v.renderCategoryX = 345;
        cfg.v.renderCategoryY = 5;
        cfg.v.antisnipeCategoryX = 430;
        cfg.v.antisnipeCategoryY = 5;
        cfg.v.utilityCategoryX = 515;
        cfg.v.utilityCategoryY = 5;
        cfg.v.advancedCategoryX = 515;
        cfg.v.advancedCategoryY = 160;
        cfg.v.meowtilsCategoryExpanded = false;
        cfg.v.hypixelCategoryExpanded = false;
        cfg.v.skywarsCategoryExpanded = false;
        cfg.v.bedwarsCategoryExpanded = false;
        cfg.v.renderCategoryExpanded = false;
        cfg.v.antisnipeCategoryExpanded = false;
        cfg.v.utilityCategoryExpanded = false;
        cfg.v.advancedCategoryExpanded = false;
        cfg.save();
        Meowtils.addMessage(EnumChatFormatting.GREEN + "Reset GUI positions." + EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + " Requires restart.");
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}
