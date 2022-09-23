package net.lifecity.mc.skillmaster.user.skillset

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import net.lifecity.mc.skillmaster.skill.Skill

/**
 * 入力に使うスキルキーのクラス
 */
class SkillKey(val button : SkillButton, val num : Int = 0, var skill : Skill?)