package net.lifecity.mc.skillmaster.game.games

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.game.GameResult
import net.lifecity.mc.skillmaster.game.GameTeam
import net.lifecity.mc.skillmaster.game.GameType
import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.game.stage.field.OnePoint
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.ChatColor

class Training(stage: GameStage, user: SkillUser) :
    Game(stage, GameType.TRAINING, FieldType.TWO_POINT, 60, 3) {

    private val field: OnePoint = stage.getField(FieldType.ONE_POINT) as OnePoint
    private val onlyTeam: GameTeam

    init {
        onlyTeam = GameTeam("OnlyTeam", ChatColor.GREEN, arrayOf(user))
    }

    override val teams: Array<GameTeam>
        get() = arrayOf(onlyTeam)

    override fun afterGameTimer() {}

    override fun sendResult() {
        onlyTeam.sendTitle(GameResult.WIN.en, "終了します...")
    }

    override fun hasTeam(team: GameTeam) = team === onlyTeam

    override fun teleportAll() {
        teleportTeam(onlyTeam)
    }

    override fun teleportTeam(team: GameTeam) {
        if (team === onlyTeam)
            team.teleportAll(field.point)
    }
}