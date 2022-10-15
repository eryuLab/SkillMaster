package net.lifecity.mc.skillmaster.skill.skills

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class RazorStub(user: SkillUser?) : Skill(
    "レイザースタブ",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    listOf("剣に闇をまとわせて斜め下から切り上げる"),
    0,
    user = user
) {
    override fun activate() {
        if (user == null) return

        val player = this.user.player
        val target = getTargetEntity(player, player.getNearbyEntities(3.0, 3.0, 3.0))

        target?.let {
//            val targetPlayer = target as? Player ?: return
            if (target != player) {
                user.sendActionBar("${ChatColor.DARK_AQUA} スキル『$name』発動")
                drawParticle(target)
                val vector = Vector(0.0, 1.0, 0.0)
                target.velocity = vector
                super.deactivate()
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

    private fun drawParticle(target: Entity) {
        if (user == null) return

        val loc =
            Location(target.world, target.location.x, target.location.y , target.location.z)

        drawCircle(loc, Particle.SPELL_WITCH, 1.0, 30, 0.0, 0.0, 0.0)

    }

    private fun drawCircle(
        origin: Location,
        particle: Particle,
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
            origin.world.spawnParticle(particle, origin, 10, null)
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