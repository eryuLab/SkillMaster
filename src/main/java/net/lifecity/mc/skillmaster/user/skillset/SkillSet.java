package net.lifecity.mc.skillmaster.user.skillset;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * スキルキーが
 */
public class SkillSet extends ArrayList<SkillKey> {

    @Getter
    private final SkillButton button;

    public SkillSet(SkillButton button, Skill... skills) {
        this.button = button;
        for (int i = 0; i < skills.length; i++) {
            add(new SkillKey(button, i, skills[i]));
        }
    }
}
