package net.lifecity.mc.skillmaster.skill.separatedskills.jumpattack;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

import java.util.Arrays;

/**
 * 飛び上がり、地面に向かって突撃するスキル
 */
public class SSJumpAttack extends JumpAttack {

    public SSJumpAttack(SkillUser user) {
        super(
                Arrays.asList(Weapon.STRAIGHT_SWORD),
                0,
                28,
                20,
                user,
                0.8,
                1.1,
                7,
                1.3,
                -1,
                2,
                5,
                0.5
        );
    }
}
