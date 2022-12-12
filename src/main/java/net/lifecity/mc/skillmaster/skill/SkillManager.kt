package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.item.displayName
import net.lifecity.mc.skillmaster.SkillConvertException
import net.lifecity.mc.skillmaster.skill.compositeskills.*
import net.lifecity.mc.skillmaster.skill.skills.*
import net.lifecity.mc.skillmaster.skill.testskills.JuRenGeki
import net.lifecity.mc.skillmaster.skill.testskills.LightEffect
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.inventory.ItemStack

class SkillManager(val user: SkillUser) {
    // スキルの登録
    val skillList = listOf(
        // new type
        RazorStub(user),
        Thrust(user),
        KaraNoKamae(user),
        SlapStep(user),
        Kick(user),
        Okigiri(user),
        IkkikaseiNoKamae(user),
        Kazagiri(user),
        // old type
        LeafFlow(user),
        JumpAttackSlash(user),
        Wall(user),
        MoveFast(user),
        VectorAttack(user),
        HighJump(user),
        NormalDefense(user),
        // test
        JuRenGeki(user),
        LightEffect(user),
    )

    init {
        // スキルの登録
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

        for (skill in skillList as List<Skill>) {
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
    fun fromClass(skillClass: Class<Skill>): Skill {
        for (skill in skillList) {
            if (skillClass.isInstance(skill))
                return skill
        }
        throw SkillConvertException("$skillClass could not be converted to a Skill")
    }

    /**
     * クラス名からスキルインスタンスを取得します
     * @param name クラス名
     * @return スキルインスタンス
     */
    fun fromClassName(name: String): Skill {
        for (skill in skillList) {
            if (skill::class.java.simpleName == name)
                return skill
        }
        throw SkillConvertException("$name could not be converted to a Skill")
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    fun fromItemStack(itemStack: ItemStack): Skill {
        if (!itemStack.hasItemMeta())
            throw SkillConvertException("${itemStack.displayName} could not be converted to a Skill")

        if (!itemStack.itemMeta.hasCustomModelData())
            throw SkillConvertException("${itemStack.displayName} could not be converted to a Skill")


        for (skill in skillList as List<Skill>) {
            if (skill.match(itemStack))
                return skill
        }

        throw SkillConvertException("${itemStack.displayName} could not be converted to a Skill")
    }

    /**
     * ItemStackからSkillに変換できるか否かを返します
     * @param itemStack 特定の対象となるItemStack
     * @return 変換できるか否か
     */
    fun canConvertItemStack(itemStack: ItemStack) : Boolean {
        if (!itemStack.hasItemMeta())
            return false

        if (!itemStack.itemMeta.hasCustomModelData())
            return false

        for (skill in skillList) {
            if (skill.match(itemStack))
                return true
        }

        return false
    }
}