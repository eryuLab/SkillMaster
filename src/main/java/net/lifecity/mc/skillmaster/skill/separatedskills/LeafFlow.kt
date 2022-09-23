package net.lifecity.mc.skillmaster.skill.separatedskills;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * 前方に突進しながら敵を攻撃するスキル
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LeafFlow extends SeparatedSkill {

    public LeafFlow(SkillUser user) {
        super(
                "リーフフロー",
                Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER),
                SkillType.ATTACK,
                Arrays.asList(
                        "前に進みながら斬撃します。",
                        "1回目の入力で前に移動します。",
                        "2回目の入力で攻撃します。"
                ),
                0,
                8,
                10,//100
                user
        );
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(1.3)
                .setY(0.15);

        user.getPlayer().setVelocity(vector);

        // ランダムロケーションを生成
        List<Location> leaves = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            leaves.add(randomLocation(0.25));

        List<Location> wind = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            wind.add(randomLocation(0.5));

        // エフェクト葉の流れ
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

                for (Location location : leaves) {
                    Location particleLocation = user.getPlayer().getLocation();
                    particleLocation.add(0, 1, 0);
                    particleLocation.add(location);

                    particle(Particle.FALLING_DUST, particleLocation, Material.SPRUCE_LEAVES.createBlockData());
                }

                for (Location location : wind) {
                    Location particleLocation = user.getPlayer().getLocation();
                    particleLocation.add(0, 1, 0);
                    particleLocation.add(location);

                    particle(Particle.ELECTRIC_SPARK, particleLocation);
                }

                if (count % 2 == 0)
                    user.playSound(Sound.BLOCK_WET_GRASS_BREAK);

                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1);
    }

    public Location randomLocation(double max) {
        Random random = new Random();

        double x = random.nextDouble() * max;
        if (random.nextBoolean())
            x *= -1;
        double y = random.nextDouble() * max;
        if (random.nextBoolean())
            y *= -1;
        double z = random.nextDouble() * max;
        if (random.nextBoolean())
            z *= -1;

        return new Location(user.getPlayer().getWorld(), x, y, z);
    }

    @Override
    public void additionalInput() {

        // 一番近いEntityを攻撃
        boolean b = user.attackNearest(
                1.8,
                3,
                user.getPlayer().getVelocity().normalize().multiply(1).setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_SWEEP
        );

        // エフェクト
        if (b) {
            for (int i = 0; i < 3; i++) {
                particle(Particle.SWEEP_ATTACK, user.getNearEntities(1.8).get(0).getLocation().add(0, 2, 0));
            }
            for (int i = 0; i < 6; i++) {
                particle(Particle.FLAME, user.getPlayer().getLocation().add(randomLocation(0.3)));
            }
        }

        // 終了
        deactivate();
    }
}
