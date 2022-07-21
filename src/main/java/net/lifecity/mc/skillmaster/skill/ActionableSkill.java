package net.lifecity.mc.skillmaster.skill;

import net.lifecity.mc.skillmaster.user.SkillUser;

public abstract class ActionableSkill extends Skill {

    protected ActionableSkill(String name, int point) {
        super(name, point);
    }

    public abstract void action(SkillUser user);
}
