package net.lifecity.mc.skillmaster.skill.separatedskills.straightsword;

import net.lifecity.mc.skillmaster.skill.DefenseSkill;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class SSDefense extends DefenseSkill {

    public SSDefense(SkillUser user) {
        super("通常防御", Weapon.STRAIGHT_SWORD, Arrays.asList("基本的な防御姿勢になります。", "発動してから一瞬だけすべての攻撃を防ぐことができます。"), 0, 0, 10, 35, user);
    }

    @Override
    public void defense(double damage, Vector vector) {
        user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
}
