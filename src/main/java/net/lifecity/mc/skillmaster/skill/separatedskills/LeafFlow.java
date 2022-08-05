package net.lifecity.mc.skillmaster.skill.separatedskills;

import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class LeafFlow extends SeparatedSkill {

    public LeafFlow(SkillUser user) {
        super("リーフフロー", Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER), 0, 8, 15, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(1)
                .setY(0.15);

        user.getPlayer().setVelocity(vector);
    }

    @Override
    public void additionalInput() {

        // 一番近いEntityを攻撃
        user.attackNearest(
                1.8,
                3,
                user.getPlayer().getVelocity().multiply(0.3).normalize().setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_SWEEP
                );

        // 終了
        deactivate();
    }
}