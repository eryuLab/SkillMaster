package net.lifecity.mc.skillmaster.game.games;

import net.lifecity.mc.skillmaster.game.*;
import net.lifecity.mc.skillmaster.game.field.TwoPointField;
import net.lifecity.mc.skillmaster.game.function.OnAttack;
import net.lifecity.mc.skillmaster.game.function.OnDie;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.ChatColor;

public class Duel extends Game implements OnAttack, OnDie {

    private final TwoPointField field;

    private final GameTeam teamA;
    private final GameTeam teamB;

    private boolean suddenDeath = false;

    private GameTeam winner = null;

    protected Duel(TwoPointField field, SkillUser userA, SkillUser userB) {
        super(GameType.ONE_ON_ONE, 240, 6);
        this.field = field;
        this.teamA = new GameTeam("Alpha", ChatColor.RED, new SkillUser[]{userA});
        this.teamB = new GameTeam("Beta", ChatColor.BLUE, new SkillUser[]{userB});
    }

    @Override
    public void onAttack(SkillUser attacker) {
        // 引数がこのゲームのプレイヤーじゃなかったらreturn
        if (!joined(attacker))
            return;

        // サドンデスなら終了
        if (suddenDeath) {
            // 勝利チームを取得し、終了
            if (teamA.belongs(attacker)) {
                winner = teamA;
                stop(teamA);
            } else if (teamB.belongs(attacker)) {
                winner = teamB;
                stop(teamB);
            }
        }
    }

    @Override
    public void onDie(SkillUser dead) {
        // 勝利チームを取得し、終了
        if (teamA.belongs(dead)) {
            winner = teamA;
            stop(teamA);
        } else if (teamB.belongs(dead)) {
            winner = teamB;
            stop(teamB);
        }
    }

    @Override
    public void afterGameTimer() {
        suddenDeath = true;
    }

    @Override
    public void sendResult() {
        String detail;
        if (suddenDeath)
            detail = ChatColor.RED + "一撃決め";
        else
            detail = ChatColor.BLUE + "ノックダウン";
        if (winner == teamA) {
            teamA.sendTitle(GameResult.WIN.getEn(), detail);
            teamB.sendTitle(GameResult.LOSE.getEn(), detail);
        } else if (winner == teamB) {
            teamA.sendTitle(GameResult.LOSE.getEn(), detail);
            teamB.sendTitle(GameResult.WIN.getEn(), detail);
        }
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
            team.teleportAll(field.getPointA());
        else if (team == teamB)
            team.teleportAll(field.getPointB());
    }
}
