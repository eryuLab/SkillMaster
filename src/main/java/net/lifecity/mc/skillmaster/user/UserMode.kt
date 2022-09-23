package net.lifecity.mc.skillmaster.user

import lombok.Getter

/**
 * SkillUserのモードの列挙
 */
enum class UserMode(val jp: String) {
    BATTLE("バトル"), TRAINING("トレーニング"), UNARMED("武装解除")
}