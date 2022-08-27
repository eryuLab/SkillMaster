package net.lifecity.mc.skillmaster.skill.separatedskills.leafflow;

import com.destroystokyo.paper.ParticleBuilder;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;

/**
 * 前方に突進しながら敵を攻撃するスキル
 */
public class SSLeafFlow extends SeparatedSkill {

    public SSLeafFlow(SkillUser user) {
        super("リーフフロー", Weapon.STRAIGHT_SWORD, SkillType.ATTACK, Arrays.asList("前に進みながら斬撃します。", "1回目の入力で前に移動します。", "2回目の入力で攻撃します。"), 0, 0, 8, 15, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(1)
                .setY(0.15);

        user.getPlayer().setVelocity(vector);

        // [LE]葉の流れ
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (!activated)
                    cancel();
                if (count >= 10)
                    cancel();
                if (user.getPlayer().getVelocity().length() <= 0.3)
                    cancel();

                particle(Particle.FALLING_DUST, user.getPlayer().getEyeLocation(), Material.SPRUCE_LEAVES.createBlockData());

                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1);
    }

    @Override
    public void additionalInput() {

        double radius = 1.8;

        // 一番近いEntityを攻撃
        boolean b = user.attackNearest(
                radius,
                3,
                user.getPlayer().getVelocity().multiply(0.3).normalize().setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_SWEEP
                );

        // LE
        if (b) {
            particle(Particle.EXPLOSION_LARGE, user.getNearEntities(radius).get(0).getLocation().add(0, 2, 0));
            for (int i = 0; i < 6; i++) {
                particle(Particle.FLAME, user.getPlayer().getLocation());
            }
        }

        // 終了
        deactivate();
    }
}
