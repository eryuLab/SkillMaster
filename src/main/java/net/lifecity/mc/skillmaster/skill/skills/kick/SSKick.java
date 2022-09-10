package net.lifecity.mc.skillmaster.skill.skills.kick;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

/**
 * 敵を蹴り飛ばすスキル
 */
public class SSKick extends Kick {

    public SSKick(SkillUser user) {
        super(
                Weapon.STRAIGHT_SWORD,
                0,
                5,
                user,
                1.7,
                1.8
        );
    }
}
