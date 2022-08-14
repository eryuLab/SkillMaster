package net.lifecity.mc.skillmaster.skill;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class DefenseSkill extends SeparatedSkill {

    public DefenseSkill(String name, Weapon weapon, List<String> lore, int num, int point, int activationTime, int interval, SkillUser user) {
        super(name, weapon, SkillType.DEFENSE, lore, num, point, activationTime, interval, user);
    }

    @Override
    public void additionalInput() {}

    public abstract void defense(double damage, Vector vector);
}
