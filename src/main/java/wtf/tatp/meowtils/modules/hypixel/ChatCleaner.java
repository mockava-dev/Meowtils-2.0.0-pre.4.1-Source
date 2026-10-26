package wtf.tatp.meowtils.modules.hypixel;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.Meowtils;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.gui.values.BooleanValue;
import wtf.tatp.meowtils.util.GamemodeUtil;

public class ChatCleaner
extends Module {
    private BooleanValue hideBedwarsExp;
    private BooleanValue hideSendTo;
    private BooleanValue hideWait;

    public ChatCleaner() {
        super("ChatCleaner", "chatCleanerKey", "chatCleaner", Module.Category.Hypixel);
        this.tooltip("Removes chat messages that aren't important on Hypixel.\nContains settings for hiding more optional messages");
        this.hideBedwarsExp = new BooleanValue("Hide \u00a77'Bed Wars XP'", "hideBedwarsExpMessage");
        this.addBoolean(this.hideBedwarsExp);
        this.hideSendTo = new BooleanValue("Hide \u00a77'Sending you to..'", "hideSendToMessage");
        this.addBoolean(this.hideSendTo);
        this.hideWait = new BooleanValue("Hide \u00a77'You must wait..'", "hideWaitMessage");
        this.addBoolean(this.hideWait);
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (!msg.contains(":") && !msg.contains("[Meow]") && !msg.contains("[M]") && (msg.startsWith("You tipped") || msg.contains("You are still radiating with Generosity!") || msg.contains("Your game was boosted by") || msg.equals("Coins just earned DOUBLED as a Guild Level Reward!") || msg.endsWith("'s Network Booster)") || msg.equals("You are AFK. Move around to return from AFK.") || msg.contains("joined the lobby!") || msg.equals("Tokens just earned DOUBLED as a Guild Level Reward!") || msg.contains("tokens! (") || msg.contains("coins! (") || msg.equals("If you get disconnected use /rejoin to join back in the game.") || msg.startsWith("You purchased") || msg.startsWith("Contents of") || msg.contains("Bed Wars Experience (Group Bonus)") || msg.contains("Bed Wars Experience (Position Bonus)") || msg.contains("Bed Wars Experience (Time Played)") || msg.startsWith("You deposited") || msg.equals("You can't deposit this item!") || msg.startsWith("You don't have enough") || msg.equals("YOU GOT LUCKY!") || msg.equals("You will receive DOUBLE EXP this game!") || msg.endsWith("Karma!") || msg.contains("You've already purchased this item") || msg.contains("You are already connected to this server") || msg.contains("The game starts in 10 seconds!") || msg.contains("The game starts in 20 seconds!") || msg.endsWith("Mystery Dust!") || msg.endsWith("Pet Consumables items!") || msg.equals("Please don't spam the command!") || msg.equals("You are sending commands too fast! Please slow down.") || msg.contains("Click here to watch the Replay!") || msg.contains("tokens! Picked up gold") || msg.contains("tokens! Survived 30 seconds") || msg.equals("You can't use gadgets right now!") || msg.endsWith("Soul") || msg.endsWith("Souls (Xezbeth Luck Perk)") || msg.equals("You're already in this channel!") || msg.startsWith("Teaming is not allowed") || msg.equals("We don't have enough players! Start cancelled.") || msg.endsWith("unclaimed achievement reward!") || msg.contains("Click here to view it!") || msg.contains("is available to join! CLICK HERE to join!") || msg.equals("[WATCHDOG ANNOUNCEMENT]") || msg.contains("Watchdog has banned") || msg.equals("Blacklisted modifications are a bannable offense!") || msg.contains("You've already started the") || msg.startsWith("You displayed your") || msg.equals("You can't break that block!") || msg.startsWith("Click to view the stats of your") || msg.equals("You cannot ride another player's mobs!") || msg.equals("You will be teleported to an arena to fight to the death.") || msg.equals("Right-click the bow to select a kit!") || msg.equals("If you find yourself to be going too fast, consider a slowness potion for more control.") || msg.equals("Later on in the game, some jumps may only be possible with the use of a speed potion.") || msg.equals("Try to save your double jumps for situations where you really need them.") || msg.endsWith("more players are required for the game to start.") || msg.equals("You died! You can now spectate the game!") || msg.equals("Right click with the compass in a lobby to select a game!") || msg.equals("Rate this map by clicking: [5] [4] [3] [2] [1]") || msg.equals("Prepare your defenses!") || msg.equals("Your spawn protection was removed!") || msg.equals("Click with any sword or bow to activate your skill!") || msg.equals("The next game will begin in 10s. Prepare yourselves!") || msg.equals("You've already claimed that reward!") || msg.equals("You are not allowed to use that command as a spectator!") || msg.equals("Resource Pack Declined! Make sure you have resource packs enabled in your multiplayer server settings!") || msg.equals("Click here or do /resource to open the resource pack book.") || msg.equals("Resource pack not working? Type /resource to fix it!") || msg.contains("Welcome to the Prototype Lobby") || msg.equals("All games in this lobby are currently in development.") || msg.startsWith("Click here to leave feedback!") || msg.startsWith("PIT! Latest update:") || msg.startsWith("STREAK! of") || msg.startsWith("GOLD PICKUP! from the ground") || msg.startsWith("DEATH! by") || msg.startsWith("BOUNTY! bump") || msg.startsWith("You died while carrying") && msg.contains("Comfy Pillows!") || msg.endsWith("unclaimed leveling rewards!") || msg.contains("Click here to view them!") || msg.endsWith("items of this type to deposit!") || msg.startsWith(">>> [MVP++]") && msg.contains("joined the lobby!") || msg.startsWith("Staff have banned an additional") && msg.endsWith("in the last 7 days.") || msg.endsWith("unclaimed achievement rewards!") || msg.equals("Cross-teaming is not allowed! Report cross-teamers using /report.") || msg.contains("Slumber Tickets! (") || msg.equals("You can only break blocks placed by a player!") || msg.equals("Woah there, slow down!") || msg.equals("Please wait...") || msg.equals("Opening settings...") || msg.equals("Ticket Machine now rolling...") || msg.contains("has found a ") || msg.equals("The game starts in 15 seconds!") || msg.startsWith("You are already in ") || msg.endsWith("(Lucky! Harvesting Season Perk)") || msg.endsWith("(w/ Avarice Perk)") || msg.equals("Teaming with the Murderer is not allowed!") || msg.startsWith("Survived ") || msg.equals("YOU GOT LUCKY! You will receive DOUBLE EXP this game!") || msg.contains("Bed Wars XP") && cfg.v.hideBedwarsExpMessage || msg.startsWith("MIDAS GIFT") || msg.contains("Gain XP and coins by \u00bb CLICKING HERE! \u00ab") || msg.contains("You cannot return items to another team's Shopkeeper!") || msg.contains("You cannot carry any more Comfy Pillows!") || msg.contains("[NPC]") && GamemodeUtil.bedwarsLobby || msg.contains("Sending you to") && cfg.v.hideSendToMessage || msg.contains("You must wait") && msg.contains("!") && cfg.v.hideWaitMessage || msg.contains("You are now carrying x1 Comfy Pillows, bring it back to your base!") || msg.startsWith("Deposited") && msg.contains("into Ender Chest!") || msg.equals("Command Failed: This command is on cooldown! Try again in about a second!"))) {
            event.setCanceled(true);
            if (cfg.v.debugMessages) {
                Meowtils.addMessage(EnumChatFormatting.YELLOW + "[ChatCleaner]: " + EnumChatFormatting.GRAY + "Cancelled message: " + EnumChatFormatting.WHITE + msg);
            }
        }
        if (cfg.v.failedQueueMessage && !msg.contains(":")) {
            if (msg.startsWith("Something went wrong trying to")) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Failed to queue: " + EnumChatFormatting.GRAY + "Hypixel Error");
            } else if (msg.equals("Please don't spam the command!") || msg.equals("Command Failed: This command is on cooldown! Try again in about a second!")) {
                Meowtils.addMessage(EnumChatFormatting.RED + "Failed to queue: " + EnumChatFormatting.GRAY + "Cooldown");
            }
        }
    }
}
