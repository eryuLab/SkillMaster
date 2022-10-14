package net.lifecity.mc.skillmaster.skill

import net.lifecity.mc.skillmaster.skill.defenseskills.NormalDefense
import net.lifecity.mc.skillmaster.skill.separatedskills.JumpAttackSlash
import net.lifecity.mc.skillmaster.skill.separatedskills.LeafFlow
import net.lifecity.mc.skillmaster.skill.separatedskills.Wall
import net.lifecity.mc.skillmaster.skill.skills.HighJump
import net.lifecity.mc.skillmaster.skill.skills.Kick
import net.lifecity.mc.skillmaster.skill.skills.MoveFast
import net.lifecity.mc.skillmaster.skill.skills.VectorAttack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class SkillManager(val user: SkillUser?) {
    val skillList = mutableListOf<Skill>()

    init {
        // スキルの登録
        /*
        skillList.add(LeafFlow(user))
        skillList.add(JumpAttackSlash(user))
        skillList.add(Wall(user))
        skillList.add(MoveFast(user))
        skillList.add(VectorAttack(user))
        skillList.add(HighJump(user))
        skillList.add(Kick(user))
        skillList.add(NormalDefense(user))
         */

        for ((id, skill) in skillList.withIndex()) {
            skill.id = id
        }
    }

    /**
     * 武器からスキルリストを取得します
     * @param weapon この武器のスキルリストを取得します
     * @return 武器のスキルリスト
     */
    fun fromWeapon(weapon: Weapon): List<Skill> {
        val list = mutableListOf<Skill>()

        for (skill in skillList) {
            if (skill.usable(weapon))
                list.add(skill)
        }
        return list
    }

    /**
     * クラスからスキルインスタンスを取得します
     * @param skillClass スキルクラス
     * @return スキルインスタンス
     */
    fun fromClass(skillClass: Class<Skill>): Skill? {
        for (skill in skillList) {
            if (skillClass.isInstance(skill))
                return skill
        }
        return null
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    fun fromItemStack(itemStack: ItemStack): Skill? {
        if (!itemStack.hasItemMeta())
            return null
        if (!itemStack.itemMeta.hasCustomModelData())
            return null

        for (skill in skillList) {
            if (skill.match(itemStack))
                return skill
        }
        return null
    }
}