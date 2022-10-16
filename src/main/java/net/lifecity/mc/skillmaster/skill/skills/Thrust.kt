package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import com.github.syari.spigot.api.sound.playSoundLegacy
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Thrust(user: SkillUser?) :
    Skill(
        "スラスト",
        listOf(Weapon.STRAIGHT_SWORD),
        SkillType.ATTACK,
        listOf("剣を手と垂直に構えて突き上げる"),
        80,
        user = user
    ) {

    override fun activate() {
        if (user == null) return

        super.activate()

        // 使用者を少し進ませる
        val direction = user.player.eyeLocation.direction
        user.player.velocity = direction

        // ターゲットを取得
        val target = getTargetEntity(user.player, user.player.getNearbyEntities(4.5, 4.5, 4.5))

        // 攻撃
        target?.let {
            val livingEntity = target as? LivingEntity ?: return
            user.attack(
                livingEntity,
                3.8,
                direction.multiply(1.7),
                false
            )

            // SE再生
            user.player.playSound(Sound.ENTITY_EVOKER_CAST_SPELL, pitch = 2f)
            user.player.playSound(Sound.ENTITY_EVOKER_FANGS_ATTACK, pitch = 2f)

            // エフェクト
            // 前方への突き
            // 衝撃波
            var count = 0
            var loc = user.player.eyeLocation
            val vector = loc.direction.multiply(0.19)
            SkillMaster.INSTANCE.runTaskTimer(1) {
                if (count > 5)
                    this.cancel()

                // 突きの軌道
                for (i in 1..4) {
                    loc.spawnParticle(Particle.FALLING_DUST, data = Material.BLACK_WOOL.createBlockData(), count = 3)
                    loc.spawnParticle(Particle.FALLING_DUST, data = Material.RED_CONCRETE_POWDER.createBlockData(), count = 3)
                    loc.spawnParticle(Particle.FALLING_DUST, data = Material.NETHERRACK.createBlockData())
                    loc.spawnParticle(Particle.ELECTRIC_SPARK, count = 6)
                    loc.add(vector)
                }

                count++
            }
        }
    }

    /**
     * @param entity: 始点となるエンティティ
     * @param entities: 探索対象のEntityのIterableリスト
     * @return: 探索によって得られたEntity
     */
    private fun <T : Entity> getTargetEntity(entity: Entity, entities: Iterable<T>): T? {
        val threshold = 1.0
        var target: T? = null

        for (other in entities) { //iterableリストの中を走査
            //自分のベクトルと、対象のベクトルの差を計算 => 相手が向いている方向と自分が向いている方向が逆ならば最大, 同じならば最小
            val vec = other.location.toVector().subtract(entity.location.toVector())

            // 自分のベクトルと、vecの外積の長さが1未満である => 自分のベクトルとvecがなす平行四辺形の面積が1未満、
            // つまりここでもどの程度同じ方向を向いているか判定している。
            // かつvecと自分のベクトルの内積が0以上 => 自分のベクトルがvecの向いている方向と同じ方向
            // であるならば
            if (entity.location.direction.normalize().crossProduct(vec).lengthSquared() < threshold
                && vec.normalize().dot(entity.location.direction.normalize()) >= 0
            ) {
                if (target == null || target.location.distanceSquared(entity.location) > other.location.distanceSquared(
                        entity.location
                    )
                ) {
                    target = other
                }
            }
        }
        return target
    }

    /**
     * 円のパーティクルを表示する
     */
    private fun drawCircle(
        origin: Location,
        particle: Particle,
        data: Any? = null,
        speed: Double = 0.0,
        radius: Double,
        points: Int,
        rotX: Double,
        rotY: Double,
        rotZ: Double
    ) {
        for (i in 0 until points) {
            val t = i * 2 * Math.PI / points
            val point = Vector(radius * cos(t), 0.0, radius * sin(t))
            rotX(point, rotX)
            rotY(point, rotY)
            rotZ(point, rotZ)
            origin.add(point)
            // spawn something at origin
            origin.spawnParticle(particle, speed = speed, count = 10, data = data)
            origin.subtract(point)
        }
    }

    /**
     * 与えたVectorをX軸回りでtだけ回転させる
     *
     * @param point: 回転させたいVector
     * @param t:     角度
     */
    private fun rotX(point: Vector, t: Double) {
        val y = point.y
        point.y = y * cos(t) - point.z * sin(t)
        point.z = y * sin(t) + point.z * cos(t)
    }

    /**
     * 与えたVectorをY軸回りでtだけ回転させる
     *
     * @param point: 回転させたいVector
     * @param t:     角度
     */
    private fun rotY(point: Vector, t: Double) {
        val z = point.z
        point.z = z * cos(t) - point.x * sin(t)
        point.x = z * sin(t) + point.x * cos(t)
    }

    /**
     * 与えたVectorをZ軸回りでtだけ回転させる
     *
     * @param point: 回転させたいVector
     * @param t:     角度
     */
    private fun rotZ(point: Vector, t: Double) {
        val x = point.x
        point.x = x * cos(t) - point.y * sin(t)
        point.y = x * sin(t) + point.y * cos(t)
    }
}