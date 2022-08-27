package net.lifecity.mc.skillmaster.skill.defenseskills.normaldefense;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

/**
 * 0.5秒間だけ敵からの攻撃を完全に防御するスキル
 */
public class SSNormalDefense extends NormalDefense {

    public SSNormalDefense(SkillUser user, int num) {
        super(Weapon.STRAIGHT_SWORD, num, 0, 10, 35, user);
    }
}
