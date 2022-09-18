package net.lifecity.mc.skillmaster.game.oneonone;

import net.lifecity.mc.skillmaster.game.Game;
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
    public void sendTitleAll() {

    }

    @Override
    public void sendTitleTeam(GameTeam team) {

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

    @Override
    public void teleportOne(SkillUser target) {
        if (teamA.belongs(target))
            target.getPlayer().teleport(field.spawnPointA);
        else if (teamB.belongs(target))
            target.getPlayer().teleport(field.spawnPointB);
    }
}
