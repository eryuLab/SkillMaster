package net.lifecity.mc.skillmaster.user.skillset

import org.bukkit.Material

enum class SkillButton(val jp: String, val material: Material) {
    RIGHT("右クリック", Material.YELLOW_DYE),
    SWAP("スワップ", Material.LIGHT_BLUE_DYE),
    DROP("ドロップ", Material.PINK_DYE)
}