package net.lifecity.mc.skillmaster.game.games

import net.lifecity.mc.skillmaster.game.*
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.ChatColor

class Duel(stage: GameStage, userA: SkillUser, userB: SkillUser) : Game {

    private val teamA: GameTeam = GameTeam.Solo("Alpha", ChatColor.RED, userA)
    private val teamB: GameTeam = GameTeam.Solo("Beta", ChatColor.BLUE, userB)

    override val countDownTime: Int = 5
    override val gameTime: Int = 180
    override var state: GameState = GameState.WAITING_FOR_STARTING
    override val teams: Array<GameTeam> = arrayOf(teamA, teamB)
    override lateinit var winners: GameTeam
    override lateinit var losers: GameTeam
    override var stage: GameStage = stage

    private var suddenDeath = false
    private val field = stage.getField(GameType.DUEL)

    override val gameManager = GameManager(this)

    override fun inStartGameTimer() {}

    override fun inGameTimer() {}

    override fun afterGameTimer() {
        // サドンデスに変更
        suddenDeath = true

        // サドンデス告知
        gameManager.sendMessageAll("${ChatColor.GRAY}サドンデスに突入...")
    }

    override fun sendResult() {
        val detail: String = if (suddenDeath)  "${ChatColor.RED}一撃決め" else "${ChatColor.BLUE}ノックダウン"
        if (winners === teamA) {
            teamA.sendTitle(GameResult.WIN.en, detail)
            teamB.sendTitle(GameResult.LOSE.en, detail)
        } else if (winners === teamB) {
            teamA.sendTitle(GameResult.LOSE.en, detail)
            teamB.sendTitle(GameResult.WIN.en, detail)
        }
    }


    override fun teleportAll() {
        teleportTeam(teamA)
        teleportTeam(teamB)
    }

    override fun teleportTeam(team: GameTeam) {
        if (team === teamA)
            team.teleportAll(field.tpLocations[0])
        else if (team === teamB)
            team.teleportAll(field.tpLocations[1])
    }

    override fun onUserAttack(attacker: SkillUser) {
        // 引数がこのゲームのプレイヤーじゃなかったらreturn
        if (!gameManager.joined(attacker)) return

        // サドンデスなら終了
        if (suddenDeath) {
            // 勝利チームを取得し、終了
            if (teamA.belongs(attacker)) {
                winners = teamA
                gameManager.stop()
            } else if (teamB.belongs(attacker)) {
                winners = teamB
                gameManager.stop()
            }
        }
    }

    override fun onUserDead(dead: SkillUser) {
        // 勝利チームを取得し、終了
        if (teamA.belongs(dead)) {
            winners = teamB
            gameManager.stop()
        } else if (teamB.belongs(dead)) {
            winners = teamA
            gameManager.stop()
        }
    }
}