package net.lifecity.mc.skillmaster.skill.separatedskills.wall;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

public class SSWall extends Wall{
    public SSWall(SkillUser user, int num) {
        super(Weapon.STRAIGHT_SWORD, num, 0, 10, 20, user);
    }
}
