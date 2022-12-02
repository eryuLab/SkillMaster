package net.lifecity.mc.skillmaster.game.games

import net.lifecity.mc.skillmaster.game.*
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Husk

class Training(stage: GameStage, user: SkillUser) : Game {

    private val onlyTeam: GameTeam = GameTeam.Solo("OnlyTeam", ChatColor.GREEN, user)
    override val countDownTime: Int = 3
    override val gameTime: Int = 60
    override var state: GameState = GameState.WAITING_FOR_STARTING
    override val teams: Array<GameTeam> = arrayOf(onlyTeam)
    override lateinit var winners: GameTeam
    override lateinit var losers: GameTeam
    override var stage: GameStage = stage

    private val field = stage.getField(GameType.TRAINING)
    private var husk: Husk? = null
    override val gameManager = GameManager(this)


    override fun inStartGameTimer() {
        husk = field.tpLocations[0].world.spawnEntity(field.tpLocations[0], EntityType.HUSK) as Husk
        husk?.let {
            it.maxHealth = 40.0
            it.health = 40.0
            it.setAI(false)
            it.setGravity(false)
            it.customName = "Target"
        }
    }

    override fun inGameTimer() {
        husk?.let {
            if (it.isDead) {
                winners = onlyTeam
                gameManager.stop()
            }
        }
    }

    override fun afterGameTimer() {
        winners = onlyTeam
        gameManager.stop()

        husk?.let {
            it.damage(it.health)
        }
    }

    override fun sendResult() {
        onlyTeam.sendTitle(GameResult.WIN.en, "終了します...")
    }

    override fun teleportAll() {
        teleportTeam(onlyTeam)
    }

    override fun teleportTeam(team: GameTeam) {
        if (team === onlyTeam)
            team.teleportAll(field.tpLocations[0])
    }

    override fun onUserLogout(leaver: SkillUser) {
        winners = onlyTeam
        gameManager.stop()
    }

    override fun onUserAttack(attacker: SkillUser) {
    }

    override fun onUserDead(dead: SkillUser) {
    }
}