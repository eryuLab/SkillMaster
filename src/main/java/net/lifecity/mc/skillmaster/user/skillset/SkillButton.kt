package net.lifecity.mc.skillmaster.user.skillset

import org.bukkit.Material

enum class SkillButton(val systemName: String, val jp: String, val material: Material) {
    RIGHT("r", "右クリック", Material.YELLOW_DYE),
    SWAP("s", "スワップ", Material.LIGHT_BLUE_DYE),
    DROP("d", "ドロップ", Material.PINK_DYE)
}