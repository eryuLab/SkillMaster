package net.lifecity.mc.skillmaster.skill.skills

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.entity.LivingEntity

class Kazagiri(user: SkillUser): Skill(
        "風斬り",
        arrayListOf(Weapon.STRAIGHT_SWORD),
        SkillType.ATTACK,
        arrayListOf("前方を切り上げ"),
        60,
        user
) {
    var target: LivingEntity? = null

    override fun canActivate(): Boolean {
        val search = TargetSearch()
        // 攻撃対象取得
        target = search.getTargetLivingEntity(user.player, 3.0)

        // 攻撃対象がいないとき発動不可
        if (target == null)
            return false

        // 攻撃対象が真上、真下にいる場合発動不可
        // 攻撃対象との位置関係を取得
        var theta = search.getLivingEntityPositionRelation(user.player, target!!).first
        theta *= 180 / Math.PI
        // 攻撃対象が真上にいるとき発動不可
        if (theta <= 15)
            return false
        // 攻撃対象が真下にいるとき発動不可
        if (theta >= 165)
            return false

        // 発動可
        return true
    }

    override fun onActivate() {
        // 攻撃処理
        // SEとLE
    }
}