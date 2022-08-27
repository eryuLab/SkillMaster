package net.lifecity.mc.skillmaster.skill.skills.highjump;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

/**
 * 上に高く飛び上がるスキル
 */
public class SSHighJump extends HighJump {

    public SSHighJump(SkillUser user, int num) {
        super(
                Weapon.STRAIGHT_SWORD,
                num,
                user,
                0.65,
                1
        );
    }
}
