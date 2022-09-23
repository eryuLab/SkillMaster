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

/**
 * スキルを管理するクラス
 */
class SkillManager(user: SkillUser) {

    val skillList = listOf(
        // 複合スキル
        LeafFlow(user),
        JumpAttackSlash(user),
        Wall(user),
        // 単発スキル
        MoveFast(user),
        VectorAttack(user),
        HighJump(user),
        Kick(user),
        // 防御スキル
        NormalDefense(user)
    )

    init {
        for (i in skillList.indices) {
            skillList[i].id = i
        }
    }

    /**
     * 武器からスキルリストを取得します
     * @param weapon この武器のスキルリストを取得します
     * @return 武器のスキルリスト
     */
    fun fromWeapon(weapon: Weapon?): List<Skill> {
        val list: MutableList<Skill> = ArrayList()
        for (skill in skillList) {
            if (skill.weaponList.contains(weapon)) list.add(skill)
        }
        return list
    }

    /**
     * クラスからスキルインスタンスを取得します
     * @param skillClass スキルクラス
     * @return スキルインスタンス
     */
    fun fromClass(skillClass: Class<out Skill>): Skill? {
        for (skill in skillList) {
            if (skillClass.isInstance(skill)) return skill
        }
        return null
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    fun fromItemStack(itemStack: ItemStack): Skill? {
        if (!itemStack.hasItemMeta()) return null
        if (!itemStack.itemMeta.hasCustomModelData()) return null
        for (skill in skillList) {
            if (skill.`is`(itemStack)) return skill
        }
        return null
    }
}