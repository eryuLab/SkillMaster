package net.lifecity.mc.skillmaster.game.oneonone;

import net.lifecity.mc.skillmaster.game.Game;
import net.lifecity.mc.skillmaster.game.GameResult;
import net.lifecity.mc.skillmaster.game.GameTeam;
import net.lifecity.mc.skillmaster.game.GameType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.ChatColor;

public class OneOnOne extends Game {

    private final OneOnOneField field;

    private final GameTeam teamA;
    private final GameTeam teamB;

    protected OneOnOne(OneOnOneField field, SkillUser userA, SkillUser userB) {
        super(GameType.ONE_ON_ONE, 240, 6);
        this.field = field;
        this.teamA = new GameTeam("Alpha", ChatColor.RED, new SkillUser[]{userA});
        this.teamB = new GameTeam("Beta", ChatColor.BLUE, new SkillUser[]{userB});
    }

    @Override
    public void sendResult() {
        // 結果集計
        String sub = "A:33 --- B:4";

        GameTeam winner = teamA;
        GameTeam loser = teamB;

        // タイトル送信
        winner.sendTitle(GameResult.WIN.getEn(), sub);
        loser.sendTitle(GameResult.LOSE.getEn(), sub);
    }

    @Override
    public boolean hasTeam(GameTeam team) {
        return team == teamA || team == teamB;
    }

    @Override
    public GameTeam[] getTeams() {
        return new GameTeam[]{teamA, teamB};
    }

    @Override
    public void teleportAll() {
        teleportTeam(teamA);
        teleportTeam(teamB);
    }

    @Override
    public void teleportTeam(GameTeam team) {
        if (team == teamA)
            team.teleportAll(field.spawnPointA);
        else if (team == teamB)
            team.teleportAll(field.spawnPointB);
    }
}
