package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class MoveFast extends Skill {


    public MoveFast(SkillUser user) {
        super("高速移動", Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.LONG_SWORD, Weapon.RAPIER), 0, 25, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection();

        user.getPlayer().setVelocity(vector);
    }
}
