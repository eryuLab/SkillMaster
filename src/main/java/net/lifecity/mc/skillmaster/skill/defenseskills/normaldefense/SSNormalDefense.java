package net.lifecity.mc.skillmaster.skill.defenseskills.normaldefense;

import net.lifecity.mc.skillmaster.skill.DefenseSkill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * 0.5秒間だけ敵からの攻撃を完全に防御するスキル
 */
public class SSNormalDefense extends DefenseSkill {

    public SSNormalDefense(SkillUser user) {
        super("通常防御", Weapon.STRAIGHT_SWORD, Arrays.asList("基本的な防御姿勢になります。", "発動してから一瞬だけすべての攻撃を防ぐことができます。"), 0, 0, 10, 35, user);
    }

    @Override
    public void defense(double damage, Vector vector) {
        user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
}
