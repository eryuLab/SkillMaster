package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.AttackSkill
import net.lifecity.mc.skillmaster.skill.ISkill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class VectorAttack(
    override val name: String = "ベクトルアタック",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD, Weapon.GREAT_SWORD, Weapon.LONG_SWORD, Weapon.MACE),
    override val type: SkillType = SkillType.ATTACK,
    override val lore: List<String> = listOf("ユーザーが持つベクトルを力に変換して攻撃します。"),
    override var isActivated: Boolean = false,
    override val interval: Int = 100,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : AttackSkill, ISkill {
    override fun canActivate(): Boolean = true

    override fun onActivate() {
        val vector = user.player.eyeLocation.direction.multiply(1.2)
        user.player.velocity = user.player.velocity.add(vector)

        var damage = user.player.velocity.length()
        damage *= 2.0

        val target = getTargetEntity(user.player, user.player.getNearbyEntities(1.8, 1.8, 1.8)) ?: return

        if (target !is LivingEntity) return

        attackAddVector(user, target, damage, user.player.velocity.multiply(0.25).setY(0.15))
        user.player.sendMessage("damage: $damage")

        // 軌道
        val data: BlockData =
            if (damage > 5) Material.RED_CONCRETE_POWDER.createBlockData()
            else if (damage > 4) Material.PURPLE_CONCRETE_POWDER.createBlockData()
            else if (damage > 3) Material.MAGENTA_CONCRETE_POWDER.createBlockData()
            else if (damage > 2) Material.PINK_CONCRETE_POWDER.createBlockData()
            else Material.WHITE_CONCRETE_POWDER.createBlockData()

        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (count >= 8) cancel()
            if (user.player.velocity.length() < 0.47) cancel()

            val loc = user.player.location
            loc.spawnParticle(Particle.FALLING_DUST, data = data)

            if (damage > 5) loc.spawnParticle(Particle.LAVA)
            else if (damage > 4 && count % 2 == 1) loc.spawnParticle(Particle.LAVA)
            count++
        }
    }

    override fun onDeactivate() {}

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