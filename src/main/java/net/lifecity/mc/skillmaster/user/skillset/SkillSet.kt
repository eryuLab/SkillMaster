package net.lifecity.mc.skillmaster.user.skillset

import lombok.Getter
import net.lifecity.mc.skillmaster.skill.Skill

/**
 * スキルキーが
 */
class SkillSet(val button: SkillButton, zero: Skill, one: Skill, two: Skill) :
    ArrayList<SkillKey>() {
    init {
        add(SkillKey(button, 0, zero))
        add(SkillKey(button, 1, one))
        add(SkillKey(button, 2, two))
    }
}