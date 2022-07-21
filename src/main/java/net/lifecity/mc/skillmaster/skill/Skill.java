package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.user.SkillUser;

public abstract class Skill {

    @Getter
    private final String name;

    @Getter
    private final int point;

    protected Skill(String name, int point) {
        this.name = name;
        this.point = point;
    }

    public abstract void activate(SkillUser user);
}
