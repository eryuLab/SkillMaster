package net.lifecity.mc.skillmaster.skill.defenseskills.normaldefense;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

import java.util.Arrays;

/**
 * 0.5秒間だけ敵からの攻撃を完全に防御するスキル
 */
public class SSNormalDefense extends NormalDefense {

    public SSNormalDefense(SkillUser user) {
        super(Arrays.asList(Weapon.STRAIGHT_SWORD), 0, 10, 35, user);
    }
}
