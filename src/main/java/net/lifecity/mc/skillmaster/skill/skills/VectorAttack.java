package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class VectorAttack extends Skill {

    public VectorAttack(SkillUser user) {
        super("ベクトルアタック", Weapon.STRAIGHT_SWORD, 0, 40, user);
    }

    @Override
    public void activate() {
        super.activate();

        double damage = user.getPlayer().getVelocity().length();
        damage *= 3;

        user.attackNearest(
                1.8,
                damage,
                user.getPlayer().getVelocity().multiply(0.25).setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_CRIT
        );

        user.sendMessage("damage: " + damage);
    }
}
