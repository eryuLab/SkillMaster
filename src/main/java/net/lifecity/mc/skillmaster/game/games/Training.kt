package net.lifecity.mc.skillmaster.game.games

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.game.GameResult
import net.lifecity.mc.skillmaster.game.GameTeam
import net.lifecity.mc.skillmaster.game.GameType
import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.game.stage.field.OnePoint
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Husk

class Training(stage: GameStage, user: SkillUser) :
    Game(stage, GameType.TRAINING, FieldType.TWO_POINT, 60, 3) {

    private val field: OnePoint = stage.getField(FieldType.ONE_POINT) as OnePoint
    private val onlyTeam: GameTeam
    private var husk: Husk? = null

    init {
        onlyTeam = GameTeam("OnlyTeam", ChatColor.GREEN, arrayOf(user))
    }

    override val teams: Array<GameTeam>
        get() = arrayOf(onlyTeam)

    override fun inStartGameTimer() {
        husk = field.point.world.spawnEntity(field.point, EntityType.HUSK) as Husk
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
                stop(onlyTeam)
            }
        }
    }

    override fun afterGameTimer() {
        husk?.let {
            it.damage(it.health)
        }
    }

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