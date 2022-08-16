package net.lifecity.mc.skillmaster.user.skillset;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.Skill;

public class SkillSet {

    @Getter
    private final SkillKey[] rightSet;

    @Getter
    private final SkillKey[] swapSet;

    @Getter
    private final SkillKey[] dropSet;

    public SkillSet(
            Skill rightOne, Skill rightTwo, Skill rightThree,
            Skill swapOne, Skill swapTwo, Skill swapThree,
            Skill dropOne, Skill dropTwo, Skill dropThree
    ) {
        this.rightSet = new SkillKey[] {
                new SkillKey(SkillButton.RIGHT, 0, rightOne),
                new SkillKey(SkillButton.RIGHT, 1, rightTwo),
                new SkillKey(SkillButton.RIGHT, 2, rightThree)
        };
        this.swapSet = new SkillKey[] {
                new SkillKey(SkillButton.SWAP, 0, swapOne),
                new SkillKey(SkillButton.SWAP, 1, swapTwo),
                new SkillKey(SkillButton.SWAP, 2, swapThree)
        };
        this.dropSet = new SkillKey[] {
                new SkillKey(SkillButton.DROP, 0, dropOne),
                new SkillKey(SkillButton.DROP, 1, dropTwo),
                new SkillKey(SkillButton.DROP, 2, dropThree)
        };
    }
}
