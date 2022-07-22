package net.lifecity.mc.skillmaster.skill;

import lombok.Setter;
import net.lifecity.mc.skillmaster.user.SkillUser;

public abstract class ActionableSkill extends Skill {

    @Setter
    protected boolean actionable = true;

    protected ActionableSkill(String name, int point, int interval) {
        super(name, point, interval);
    }

    public abstract void action(SkillUser user);
}
