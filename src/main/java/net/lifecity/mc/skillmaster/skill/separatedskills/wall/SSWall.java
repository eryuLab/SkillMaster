package net.lifecity.mc.skillmaster.skill.separatedskills.wall;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

import java.util.Arrays;

public class SSWall extends Wall{
    public SSWall(SkillUser user) {
        super(Arrays.asList(Weapon.STRAIGHT_SWORD), 0, 10, 20, user);
    }
}
