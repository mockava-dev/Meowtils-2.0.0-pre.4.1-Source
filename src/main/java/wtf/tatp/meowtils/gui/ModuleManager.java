package wtf.tatp.meowtils.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import wtf.tatp.meowtils.gui.Module;
import wtf.tatp.meowtils.modules.advanced.AntiBot;
import wtf.tatp.meowtils.modules.advanced.DelayRemover;
import wtf.tatp.meowtils.modules.advanced.InventoryFill;
import wtf.tatp.meowtils.modules.advanced.InventoryMove;
import wtf.tatp.meowtils.modules.advanced.ViewPackets;
import wtf.tatp.meowtils.modules.antisnipe.AntiCheat;
import wtf.tatp.meowtils.modules.antisnipe.AutoReport;
import wtf.tatp.meowtils.modules.antisnipe.AutoSafelist;
import wtf.tatp.meowtils.modules.antisnipe.PartyWarning;
import wtf.tatp.meowtils.modules.antisnipe.SniperWarning;
import wtf.tatp.meowtils.modules.bedwars.ArmorAlerts;
import wtf.tatp.meowtils.modules.bedwars.AutoChest;
import wtf.tatp.meowtils.modules.bedwars.AutoClaim;
import wtf.tatp.meowtils.modules.bedwars.BedTracker;
import wtf.tatp.meowtils.modules.bedwars.BedwarsCounter;
import wtf.tatp.meowtils.modules.bedwars.ConsumeAlerts;
import wtf.tatp.meowtils.modules.bedwars.ItemAlerts;
import wtf.tatp.meowtils.modules.bedwars.ResourceTracker;
import wtf.tatp.meowtils.modules.bedwars.TrapNotifier;
import wtf.tatp.meowtils.modules.bedwars.UpgradeAlerts;
import wtf.tatp.meowtils.modules.bedwars.UpgradesHUD;
import wtf.tatp.meowtils.modules.hypixel.AutoChannel;
import wtf.tatp.meowtils.modules.hypixel.AutoGG;
import wtf.tatp.meowtils.modules.hypixel.AutoTip;
import wtf.tatp.meowtils.modules.hypixel.AutoWho;
import wtf.tatp.meowtils.modules.hypixel.ChatCleaner;
import wtf.tatp.meowtils.modules.hypixel.Denicker;
import wtf.tatp.meowtils.modules.hypixel.Notify;
import wtf.tatp.meowtils.modules.hypixel.Stats;
import wtf.tatp.meowtils.modules.meowtils.GUI;
import wtf.tatp.meowtils.modules.meowtils.Notifications;
import wtf.tatp.meowtils.modules.meowtils.Settings;
import wtf.tatp.meowtils.modules.meowtils.Teams;
import wtf.tatp.meowtils.modules.render.AntiInvis;
import wtf.tatp.meowtils.modules.render.BlockCount;
import wtf.tatp.meowtils.modules.render.BreakProgress;
import wtf.tatp.meowtils.modules.render.Cape;
import wtf.tatp.meowtils.modules.render.ChestESP;
import wtf.tatp.meowtils.modules.render.ConsumeTimer;
import wtf.tatp.meowtils.modules.render.FakeAutoBlock;
import wtf.tatp.meowtils.modules.render.HealthInfo;
import wtf.tatp.meowtils.modules.render.InstantHurt;
import wtf.tatp.meowtils.modules.render.NoTitles;
import wtf.tatp.meowtils.modules.render.PotionHUD;
import wtf.tatp.meowtils.modules.render.ShinyPots;
import wtf.tatp.meowtils.modules.render.ViewClip;
import wtf.tatp.meowtils.modules.skywars.CooldownHUD;
import wtf.tatp.meowtils.modules.skywars.EquipAlerts;
import wtf.tatp.meowtils.modules.skywars.ItemHighlight;
import wtf.tatp.meowtils.modules.skywars.MiningAlerts;
import wtf.tatp.meowtils.modules.skywars.NoArmorDye;
import wtf.tatp.meowtils.modules.skywars.SkywarsAlerts;
import wtf.tatp.meowtils.modules.skywars.SkywarsCounter;
import wtf.tatp.meowtils.modules.skywars.StrengthESP;
import wtf.tatp.meowtils.modules.skywars.TimeWarpDisplay;
import wtf.tatp.meowtils.modules.utility.ActionSounds;
import wtf.tatp.meowtils.modules.utility.AntiObfuscate;
import wtf.tatp.meowtils.modules.utility.AutoStairs;
import wtf.tatp.meowtils.modules.utility.AutoSwap;
import wtf.tatp.meowtils.modules.utility.AutoText;
import wtf.tatp.meowtils.modules.utility.HotbarLock;
import wtf.tatp.meowtils.modules.utility.LatencyAlerts;
import wtf.tatp.meowtils.modules.utility.NullMove;
import wtf.tatp.meowtils.modules.utility.Sprint;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<Module>();

    private static void register(Module ... mods) {
        Collections.addAll(modules, mods);
    }

    public static List<Module> getModules() {
        return modules;
    }

    static {
        ModuleManager.register(new GUI(), new AntiBot(), new AntiCheat(), new AntiObfuscate(), new ArmorAlerts(), new AutoChannel(), new AutoGG(), new AutoReport(), new AutoSafelist(), new AutoTip(), new AutoWho(), new BreakProgress(), new Cape(), new ChatCleaner(), new ConsumeTimer(), new Denicker(), new FakeAutoBlock(), new HealthInfo(), new InventoryMove(), new ItemAlerts(), new ItemHighlight(), new LatencyAlerts(), new Notifications(), new NoTitles(), new NullMove(), new ConsumeAlerts(), new ResourceTracker(), new ShinyPots(), new SniperWarning(), new Sprint(), new Stats(), new StrengthESP(), new AutoClaim(), new UpgradeAlerts(), new UpgradesHUD(), new ViewClip(), new InventoryFill(), new ViewPackets(), new SkywarsAlerts(), new MiningAlerts(), new CooldownHUD(), new AutoText(), new AutoChest(), new NoArmorDye(), new EquipAlerts(), new ChestESP(), new Settings(), new PotionHUD(), new HotbarLock(), new TimeWarpDisplay(), new AutoSwap(), new BlockCount(), new BedTracker(), new InstantHurt(), new DelayRemover(), new AutoStairs(), new AntiInvis(), new TrapNotifier(), new ActionSounds(), new Teams(), new BedwarsCounter(), new Notify(), new SkywarsCounter(), new PartyWarning());
        modules.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
    }
}
