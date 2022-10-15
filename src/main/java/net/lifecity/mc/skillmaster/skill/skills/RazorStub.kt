package net.lifecity.mc.skillmaster.skill.skills

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

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
        val target = getTargetEntity(player, player.getNearbyEntities(3.0,3.0,3.0))

        target?.let {
            val targetPlayer = target as? Player ?: return
            if (targetPlayer != player) {
                user.sendActionBar("${ChatColor.DARK_AQUA} スキル『$name』発動")
                player.sendMessage("レイザースタブ発動")
                val vector = Vector(0.0, 1.5, 0.0)
                targetPlayer.velocity = vector
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

        for (other in entities) {
            val vec = other.location.toVector().subtract(entity.location.toVector())
            if (entity.location.direction.normalize().crossProduct(vec).lengthSquared() < threshold
                && vec.normalize().dot(entity.location.direction.normalize()) >= 0) {

                if(target == null || target.location.distanceSquared(entity.location) > other.location.distanceSquared(entity.location)) {
                    target = other
                }
            }
        }
        return target
    }

}