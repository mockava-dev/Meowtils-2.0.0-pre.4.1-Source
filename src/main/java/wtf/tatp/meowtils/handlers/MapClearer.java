package wtf.tatp.meowtils.handlers;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wtf.tatp.meowtils.handlers.BlacklistHandler;
import wtf.tatp.meowtils.handlers.PlayerRemovedHandler;
import wtf.tatp.meowtils.modules.advanced.AntiBot;
import wtf.tatp.meowtils.modules.antisnipe.AntiCheat;
import wtf.tatp.meowtils.modules.antisnipe.SniperWarning;
import wtf.tatp.meowtils.modules.bedwars.ArmorAlerts;
import wtf.tatp.meowtils.modules.bedwars.BedTracker;
import wtf.tatp.meowtils.modules.bedwars.BedwarsCounter;
import wtf.tatp.meowtils.modules.bedwars.ConsumeAlerts;
import wtf.tatp.meowtils.modules.bedwars.ItemAlerts;
import wtf.tatp.meowtils.modules.bedwars.ResourceTracker;
import wtf.tatp.meowtils.modules.bedwars.UpgradeAlerts;
import wtf.tatp.meowtils.modules.bedwars.UpgradesHUD;
import wtf.tatp.meowtils.modules.hypixel.Denicker;
import wtf.tatp.meowtils.modules.skywars.CooldownHUD;
import wtf.tatp.meowtils.modules.skywars.EquipAlerts;
import wtf.tatp.meowtils.modules.skywars.SkywarsAlerts;
import wtf.tatp.meowtils.modules.skywars.SkywarsCounter;
import wtf.tatp.meowtils.modules.skywars.StrengthESP;
import wtf.tatp.meowtils.util.DeathTracker;
import wtf.tatp.meowtils.util.anticheat.checks.LegitScaffoldCheck;

public class MapClearer {
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote) {
            DeathTracker.clear();
            AntiBot.clear();
            AntiCheat.clear();
            ArmorAlerts.clear();
            ResourceTracker.clear();
            SniperWarning.clear();
            StrengthESP.clear();
            UpgradeAlerts.clear();
            ConsumeAlerts.clear();
            Denicker.clear();
            BlacklistHandler.clear();
            PlayerRemovedHandler.clear();
            LegitScaffoldCheck.clear();
            SkywarsAlerts.clear();
            CooldownHUD.clear();
            EquipAlerts.clear();
            ItemAlerts.clear();
            BedTracker.clear();
            UpgradesHUD.clear();
            BedwarsCounter.clear();
            SkywarsCounter.clear();
        }
    }
}
