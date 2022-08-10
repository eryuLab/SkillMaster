package net.lifecity.mc.skillmaster.skill.separatedskills.straightsword;

import net.lifecity.mc.skillmaster.skill.DefenseSkill;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

public class SSDefense extends DefenseSkill {

    public SSDefense(SkillUser user) {
        super("通常防御", Weapon.STRAIGHT_SWORD, 0, 10, 35, user);
    }

    @Override
    public void defense(double damage, Vector vector) {
        user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
}
