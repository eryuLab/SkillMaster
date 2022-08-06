package net.lifecity.mc.skillmaster.skill.skills.straightsword;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class SSVectorAttack extends Skill {

    public SSVectorAttack(SkillUser user) {
        super("ベクトルアタック", Weapon.STRAIGHT_SWORD, 0, 40, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(1.2);

        user.getPlayer().setVelocity(vector);

        double damage = user.getPlayer().getVelocity().length();
        damage *= 2;

        user.attackNearest(
                1.8,
                damage,
                user.getPlayer().getVelocity().multiply(0.25).setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_CRIT
        );

        user.sendMessage("damage: " + damage);
    }
}
