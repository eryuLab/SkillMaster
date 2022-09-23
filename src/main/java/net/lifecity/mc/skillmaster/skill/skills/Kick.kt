package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

/**
 * 敵を蹴り飛ばすスキル
 */
public class Kick extends Skill {

    public Kick(SkillUser user) {
        super(
                "蹴り",
                Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER, Weapon.MACE),
                SkillType.ATTACK,
                Arrays.asList("相手を蹴り飛ばして、相手と距離を作ります。", "一番近い敵を攻撃します。"),
                0,
                5,
                user
        );
    }

    @Override
    public void activate() {
        super.activate();

        boolean b = user.attackNearest(
                1.7,
                1,
                user.getPlayer().getEyeLocation().getDirection().multiply(1.8),
                Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK
        );

        if (b) {
            Entity entity = user.getNearEntities(1.7).get(0);
            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    if (count >= 12)
                        cancel();
                    if (entity.getVelocity().length() <= 0.4)
                        cancel();

                    particle(Particle.FALLING_DUST, entity.getLocation().add(0, 1, 0), Material.ICE.createBlockData());

                    count++;
                }
            }.runTaskTimer(SkillMaster.instance, 0, 1);
        }
    }
}
