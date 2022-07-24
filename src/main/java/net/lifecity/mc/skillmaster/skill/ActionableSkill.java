package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.user.SkillUser;

public abstract class ActionableSkill extends Skill {

    @Getter
    @Setter
    protected boolean normalAttack = true;

    protected ActionableSkill(String name, int point, int interval) {
        super(name, point, interval);
    }

    public abstract void action(SkillUser user);
}
