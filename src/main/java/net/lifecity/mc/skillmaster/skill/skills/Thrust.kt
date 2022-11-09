package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class Thrust(user: SkillUser) : Skill(
    "スラスト",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    listOf("剣を手と垂直に構えて突き上げる"),
    80,
    user
), Attack {

    override fun onActivate() {
        // 使用者を少し進ませる
        val direction = user.player.eyeLocation.direction
        user.player.velocity = direction

        // ターゲットを取得
        val target = getTargetEntity(user.player, user.player.getNearbyEntities(4.5, 4.5, 4.5))

        // 攻撃
        target?.let {
            val livingEntity = target as? LivingEntity ?: return
            attackChangeVector(user, livingEntity, 3.8, direction)

            // SE再生
            user.player.playSound(Sound.ENTITY_EVOKER_CAST_SPELL, pitch = 2f)
            user.player.playSound(Sound.ENTITY_EVOKER_FANGS_ATTACK, pitch = 2f)

            // エフェクト
            // 前方への突き
            // 衝撃波
            var count = 0
            val loc = target.location
            loc.add(0.0, 1.0, 0.0)
            val vector = user.player.eyeLocation.direction.multiply(0.19)
            SkillMaster.INSTANCE.runTaskTimer(1) {
                if (count > 5)
                    this.cancel()

                // 突きの軌道
                repeat (4) {
                    loc.spawnParticle(Particle.FALLING_DUST, data = Material.WHITE_WOOL.createBlockData(), count = 4)
                    loc.spawnParticle(Particle.FALLING_DUST, data = Material.DIAMOND_ORE.createBlockData())
                    loc.spawnParticle(Particle.FALLING_DUST, data = Material.WARPED_PLANKS.createBlockData())
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
}