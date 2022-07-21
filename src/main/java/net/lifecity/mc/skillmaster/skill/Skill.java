package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.user.SkillUser;

public abstract class Skill {

    @Getter
    private final String name;

    @Getter
    private final int point;

    @Getter
    private final int interval;

    protected Skill(String name, int point, int interval) {
        this.name = name;
        this.point = point;
        this.interval = interval;
    }

    public abstract void activate(SkillUser user);
}
