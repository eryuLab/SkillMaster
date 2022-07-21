package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.event.block.Action;

public abstract class Skill {

    @Getter
    private final String name;

    @Getter
    private final int point;

    public Skill(String name, int point) {
        this.name = name;
        this.point = point;
    }

    public abstract void activate(SkillUser user);

    public abstract void action(SkillUser user);
}
