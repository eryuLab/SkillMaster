package net.lifecity.mc.skillmaster.skill.skills.kick;

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

public class SSKick extends Skill {

    public SSKick(SkillUser user) {
        super(
                "蹴り",
                Weapon.STRAIGHT_SWORD,
                SkillType.ATTACK,
                Arrays.asList("相手を蹴り飛ばして、相手と距離を作ります。", "一番近い敵を攻撃します。"),
                6,
                0,
                5,
                user
        );
    }

    @Override
    public void activate() {

        double radius = 1.7;

        boolean b = user.attackNearest(
                radius,
                1,
                user.getPlayer().getEyeLocation().getDirection().multiply(1.8),
                Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK
        );

        if (b) {
            Entity entity = user.getNearEntities(radius).get(0);
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
