package net.lifecity.mc.skillmaster.game.games

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.game.GameResult
import net.lifecity.mc.skillmaster.game.GameTeam
import net.lifecity.mc.skillmaster.game.GameType
import net.lifecity.mc.skillmaster.game.function.OnAttack
import net.lifecity.mc.skillmaster.game.function.OnUserDead
import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.game.stage.field.TwoPoint
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.ChatColor

class Duel(stage: GameStage, userA: SkillUser, userB: SkillUser) :
    Game(stage, GameType.DUEL, FieldType.TWO_POINT, 180, 5), OnAttack, OnUserDead {

    private val field: TwoPoint = stage.getField(FieldType.TWO_POINT) as TwoPoint
    private val teamA: GameTeam
    private val teamB: GameTeam

    private var suddenDeath = false
    private var winner: GameTeam? = null

    init {
        teamA = GameTeam("Alpha", ChatColor.RED, arrayOf(userA))
        teamB = GameTeam("Beta", ChatColor.BLUE, arrayOf(userB))
    }

    override val teams: Array<GameTeam>
        get() = arrayOf(teamA, teamB)

    override fun onAttack(attacker: SkillUser) {
        // 引数がこのゲームのプレイヤーじゃなかったらreturn
        if (!joined(attacker)) return

        // サドンデスなら終了
        if (suddenDeath) {
            // 勝利チームを取得し、終了
            if (teamA.belongs(attacker)) {
                winner = teamA
                stop(teamA)
            } else if (teamB.belongs(attacker)) {
                winner = teamB
                stop(teamB)
            }
        }
    }

    override fun onDie(dead: SkillUser) {
        // 勝利チームを取得し、終了
        if (teamA.belongs(dead)) {
            winner = teamB
            stop(teamB)
        } else if (teamB.belongs(dead)) {
            winner = teamA
            stop(teamA)
        }
    }

    override fun inStartGameTimer() {}

    override fun inGameTimer() {}

    override fun afterGameTimer() {
        // サドンデスに変更
        suddenDeath = true

        // サドンデス告知
        sendMessageAll("${ChatColor.GRAY}サドンデスに突入...")
    }

    override fun sendResult() {
        val detail: String = if (suddenDeath)  "${ChatColor.RED}一撃決め" else "${ChatColor.BLUE}ノックダウン"
        if (winner === teamA) {
            teamA.sendTitle(GameResult.WIN.en, detail)
            teamB.sendTitle(GameResult.LOSE.en, detail)
        } else if (winner === teamB) {
            teamA.sendTitle(GameResult.LOSE.en, detail)
            teamB.sendTitle(GameResult.WIN.en, detail)
        }
    }

    override fun hasTeam(team: GameTeam) = team === teamA || team === teamB


    override fun teleportAll() {
        teleportTeam(teamA)
        teleportTeam(teamB)
    }

    override fun teleportTeam(team: GameTeam) {
        if (team === teamA)
            team.teleportAll(field.pointA)
        else if (team === teamB)
            team.teleportAll(field.pointB)
    }
}