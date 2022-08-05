package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.weapon.Weapon;

public class SkillSet {

    private final SkillUser user;

    @Getter
    private final Weapon weapon;

    @Getter
    private Skill[] rightSkillSet;

    @Getter
    private Skill[] swapSkillSet;

    @Getter
    private Skill[] dropSkillSet;

    public SkillSet(SkillUser user, Weapon weapon) {
        this.user = user;
        this.weapon = weapon;
        this.rightSkillSet = new Skill[] {
                null,
                null,
                null
        };
        this.swapSkillSet = new Skill[] {
                null,
                null,
                null
        };
        this.dropSkillSet = new Skill[] {
                null,
                null,
                null
        };
    }
}
