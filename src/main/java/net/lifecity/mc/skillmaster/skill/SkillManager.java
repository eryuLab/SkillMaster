package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.skills.MoveFast;
import net.lifecity.mc.skillmaster.user.SkillUser;

import java.util.HashSet;
import java.util.Set;

public class SkillManager {

    @Getter
    private final Set<Skill> allSkill = new HashSet<>();

    @Getter
    private final Set<Skill> straightSword = new HashSet<>();

    public SkillManager(SkillUser user) {
        straightSword.add(new MoveFast(user));

        allSkill.addAll(straightSword);
    }
}
