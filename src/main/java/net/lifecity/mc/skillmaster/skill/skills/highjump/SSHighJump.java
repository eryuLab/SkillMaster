package net.lifecity.mc.skillmaster.skill.skills.highjump;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

import java.util.Arrays;

/**
 * 上に高く飛び上がるスキル
 */
public class SSHighJump extends HighJump {

    public SSHighJump(SkillUser user) {
        super(
                Arrays.asList(Weapon.STRAIGHT_SWORD),
                0,
                30,
                user,
                0.65,
                1
        );
    }
}
