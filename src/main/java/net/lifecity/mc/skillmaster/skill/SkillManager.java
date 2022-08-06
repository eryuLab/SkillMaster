package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.separatedskills.straightsword.SSJumpAttack;
import net.lifecity.mc.skillmaster.skill.separatedskills.straightsword.SSLeafFlow;
import net.lifecity.mc.skillmaster.skill.skills.straightsword.SSMoveFast;
import net.lifecity.mc.skillmaster.skill.skills.straightsword.SSVectorAttack;
import net.lifecity.mc.skillmaster.user.SkillUser;

import java.util.HashSet;
import java.util.Set;

public class SkillManager {

    @Getter
    private final Set<Skill> allSkill = new HashSet<>();

    @Getter
    private final Set<Skill> straightSword = new HashSet<>();

    public SkillManager(SkillUser user) {
        straightSword.add(new SSMoveFast(user));
        straightSword.add(new SSVectorAttack(user));
        straightSword.add(new SSLeafFlow(user));
        straightSword.add(new SSJumpAttack(user));

        allSkill.addAll(straightSword);
    }
}
