package net.lifecity.mc.skillmaster.skill;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

public abstract class DefenseSkill extends SeparatedSkill {

    public DefenseSkill(String name, Weapon weapon, int point, int activationTime, int interval, SkillUser user) {
        super(name, weapon, SkillType.DEFENSE, point, activationTime, interval, user);
    }

    @Override
    public void additionalInput() {}

    public abstract void defense(double damage, Vector vector);
}
