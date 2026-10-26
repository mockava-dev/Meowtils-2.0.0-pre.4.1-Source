package wtf.tatp.meowtils.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wtf.tatp.meowtils.Meowtils;

public class GamemodeUtil {
    Minecraft mc = Minecraft.getMinecraft();
    public static boolean skywarsGame = false;
    public static boolean skywarsMiniGame = false;
    public static boolean bedwarsGame = false;
    public static boolean replay = false;
    public static boolean bedwarsLobby = false;
    public static boolean skywarsLobby = false;
    public static boolean duelsLobby = false;
    public static boolean limbo = false;
    public static boolean hypixel = false;
    public static boolean megaWalls = false;
    public static boolean bedwars2 = false;
    public static boolean bedwars3 = false;
    public static boolean bedwars4 = false;
    public static String scoreboardTitle = "";
    public static List<String> scoreboardLines = new ArrayList<String>();
    private int tickCounter = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        boolean shouldUpdate;
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (!Meowtils.nullCheck()) {
            return;
        }
        boolean onJoin = this.mc.thePlayer.ticksExisted <= 100;
        boolean bl = shouldUpdate = onJoin || this.tickCounter++ % 20 == 0;
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            skywarsGame = false;
            skywarsMiniGame = false;
            bedwarsGame = false;
            replay = false;
            bedwarsLobby = false;
            skywarsLobby = false;
            duelsLobby = false;
            limbo = false;
            hypixel = false;
            megaWalls = false;
            bedwars2 = false;
            bedwars3 = false;
            bedwars4 = false;
            scoreboardTitle = "";
            scoreboardLines.clear();
            return;
        }
        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
        if (sidebar == null) {
            return;
        }
        scoreboardTitle = sidebar.getDisplayName().replaceAll("\u00a7.", "").toLowerCase();
        scoreboardLines = this.getSidebarLines(scoreboard, sidebar);
        skywarsGame = scoreboardTitle.contains("skywars") && scoreboardLines.stream().anyMatch(line -> line.contains("mode: normal") || line.contains("mode: insane"));
        skywarsMiniGame = scoreboardTitle.contains("skywars") && scoreboardLines.stream().anyMatch(line -> line.contains("mode: mini"));
        replay = scoreboardTitle.contains("replay") || scoreboardTitle.contains("atlas");
        bedwarsLobby = scoreboardTitle.contains("bed wars") && scoreboardLines.stream().anyMatch(line -> line.contains("level:"));
        skywarsLobby = scoreboardTitle.contains("skywars") && scoreboardLines.stream().anyMatch(line -> line.contains("your level:"));
        duelsLobby = scoreboardTitle.contains("duels") && scoreboardLines.stream().anyMatch(line -> line.contains("tokens:"));
        bedwarsGame = scoreboardTitle.contains("bed wars");
        hypixel = scoreboardLines.stream().anyMatch(line -> line.contains("hypixel"));
        megaWalls = scoreboardTitle.contains("mega walls");
        bedwars2 = scoreboardLines.stream().anyMatch(line -> line.contains("mode: doubles"));
        bedwars3 = scoreboardLines.stream().anyMatch(line -> line.contains("mode: 3v3v3v3"));
        bedwars4 = scoreboardLines.stream().anyMatch(line -> line.contains("mode: 4v4v4v4"));
    }

    private List<String> getSidebarLines(Scoreboard scoreboard, ScoreObjective objective) {
        ArrayList<Score> scores = new ArrayList<Score>(scoreboard.getScores());
        scores.sort(Comparator.comparingInt(Score::getScorePoints));
        ArrayList<String> lines = new ArrayList<String>();
        for (Score score : scores) {
            if (score.getObjective() != objective) continue;
            String name = score.getPlayerName();
            ScorePlayerTeam team = scoreboard.getPlayersTeam(name);
            String line = ScorePlayerTeam.formatPlayerName((Team)team, (String)name);
            line = line.replaceAll("\u00a7.", "").toLowerCase();
            lines.add(line);
        }
        return lines;
    }
}
