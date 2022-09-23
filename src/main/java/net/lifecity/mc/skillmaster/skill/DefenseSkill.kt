package net.lifecity.mc.skillmaster.skill

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.util.Vector

/**
 * 防御スキルのスーパークラス
 */
abstract class DefenseSkill(
    name: String,
    weaponList: List<Weapon>,
    lore: List<String>,
    point: Int,
    activationTime: Int,
    interval: Int,
    user: SkillUser
) : SeparatedSkill(
    name, weaponList, SkillType.DEFENSE, lore, point, activationTime, interval, user
) {
    override fun additionalInput() {}

    /**
     * 防御するときに呼び出されます
     * @param damage 攻撃力
     * @param vector ノックバックの強さ
     */
    abstract fun defense(damage: Double, vector: Vector)
}