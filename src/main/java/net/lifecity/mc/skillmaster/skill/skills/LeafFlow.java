package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class LeafFlow extends Skill {

    public LeafFlow(SkillUser user){
        super("リーフフロー", 0, 8, 0, user);
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
    public void leftClick() {

        // 一番近いEntityを攻撃
        user.attackNearest(
                1.8,
                3,
                user.getPlayer().getVelocity().normalize().setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_SWEEP
                );

        // 終了
        deactivate();
    }
}
