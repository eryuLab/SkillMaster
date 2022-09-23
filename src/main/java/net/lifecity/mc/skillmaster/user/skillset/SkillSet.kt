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

    public SkillSet(SkillButton button, Skill zero, Skill one, Skill two) {
        this.button = button;
        add(new SkillKey(button, 0, zero));
        add(new SkillKey(button, 1, one));
        add(new SkillKey(button, 2, two));
    }
}
