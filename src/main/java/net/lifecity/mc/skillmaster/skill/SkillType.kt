package net.lifecity.mc.skillmaster.skill

import org.bukkit.Material

/**
 * スキルのおおまかな種類の列挙
 */
enum class SkillType(val jp : String, val material : Material) {
    ATTACK("攻撃", Material.STONE_SWORD), DEFENSE("防御", Material.SHIELD), MOVE("移動", Material.CHAINMAIL_BOOTS);
}