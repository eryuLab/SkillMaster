package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.item.displayName
import net.lifecity.mc.skillmaster.SkillConvertException
import net.lifecity.mc.skillmaster.skill.compositeskills.*
import net.lifecity.mc.skillmaster.skill.skills.*
import net.lifecity.mc.skillmaster.skill.testskills.JuRenGeki
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.inventory.ItemStack

object SkillList {
    lateinit var skillList : List<ISkill>

    fun register(user: SkillUser) {
        val skillList = listOf(
            // new type
            RazorStub(user= user),
            Thrust(user= user),
            KaraNoKamae(user= user),
            SlapStep(user= user),
            Kick(user= user),
            Okigiri(user= user),
            IkkikaseiNoKamae(user= user),
            Kazagiri(user= user),
            // old type
            LeafFlow(user= user),
            JumpAttackSlash(user= user),
            Wall(user= user),
            MoveFast(user= user),
            VectorAttack(user= user),
            HighJump(user= user),
            NormalDefense(user= user),
            // test
            JuRenGeki(user= user),
        )
        this.skillList = skillList
    }

    /**
     * 武器からスキルリストを取得します
     * @param weapon この武器のスキルリストを取得します
     * @return 武器のスキルリスト
     */
    fun fromWeapon(weapon: Weapon): List<ISkill> {
        val list = mutableListOf<ISkill>()

        for (skill in skillList) {
            val skillManager = SkillManager(skill)
            if (skillManager.canUse(weapon))
                list.add(skill)
        }
        return list
    }

    /**
     * クラスからスキルインスタンスを取得します
     * @param skillClass スキルクラス
     * @return スキルインスタンス
     */
    fun fromClass(skillClass: Class<ISkill>): ISkill {
        for (skill in skillList) {
            if (skillClass.isInstance(skill))
                return skill
        }
        throw SkillConvertException("$skillClass could not be converted to a Skill")
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    fun fromItemStack(itemStack: ItemStack): ISkill {
        if (!itemStack.hasItemMeta())
            throw SkillConvertException("${itemStack.displayName} could not be converted to a Skill")

        if (!itemStack.itemMeta.hasCustomModelData())
            throw SkillConvertException("${itemStack.displayName} could not be converted to a Skill")


        for (skill in skillList) {
            val skillManager = SkillManager(skill)
            if (skillManager.match(itemStack))
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
            val skillManager = SkillManager(skill)
            if (skillManager.match(itemStack))
                return true
        }

        return false
    }

}