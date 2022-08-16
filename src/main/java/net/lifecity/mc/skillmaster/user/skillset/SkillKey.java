package net.lifecity.mc.skillmaster.user.skillset;

import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.skillset.SkillButton;

/**
 * 入力に使うスキルキーのクラス
 */
public class SkillKey {

    @Getter
    private final SkillButton button;
    @Getter
    private final int num;

    @Getter
    @Setter
    private Skill skill;

    private SkillKey(SkillButton button, int num, Skill skill) {
        this.button = button;
        this.num = num;
        this.skill = skill;
    }
}
