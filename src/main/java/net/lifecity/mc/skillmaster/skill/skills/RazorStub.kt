package net.lifecity.mc.skillmaster.skill.skills

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
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
        if(user == null) return

        val player = this.user.player
        val world = player.world

        val traceResult = world.rayTraceEntities(player.location, player.location.direction, 2.0)
        traceResult?.let {
            val target = it.hitEntity as? Player ?: return

            player.sendMessage("レイザースタブ発動")
            val vector = Vector(0.0, 2.5, 0.0)
            target.velocity = vector
        }
    }

}