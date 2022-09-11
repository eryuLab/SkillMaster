package net.lifecity.mc.skillmaster.skill.defenseskills;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.DefenseSkill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 敵からの攻撃を一定時間完全に防御するスキル
 */
public abstract class NormalDefense extends DefenseSkill {

    /**
     * 通常防御のコンストラクタ
     * @param user 使用するユーザー
     */
    public NormalDefense(SkillUser user) {
        super(
                "通常防御",
                Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.GREAT_SWORD, Weapon.LONG_SWORD),
                Arrays.asList("基本的な防御姿勢になります。", "発動してから一瞬だけすべての攻撃を防ぐことができます。"),
                0,
                10,
                35,
                user
        );
    }

    @Override
    public void defense(double damage, Vector vector) {
        user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

        // ガードエフェクト
        for (int i = 0; i < 2; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 4; j++) {
                        // 座標を生成
                        double max = 1.5;
                        Random random = new Random();

                        double x = max * random.nextDouble();
                        if (random.nextBoolean())
                            x *= -1;

                        double y = max * random.nextDouble();
                        if (random.nextBoolean())
                            y *= -1;

                        double z = max * random.nextDouble();
                        if (random.nextBoolean())
                            z *= -1;

                        Location loc = user.getPlayer().getEyeLocation().add(x, y, z);

                        particle(Particle.FALLING_DUST, loc, Material.ICE.createBlockData());

                        if (j % 2 == 0)
                            particle(Particle.ELECTRIC_SPARK, loc);
                        if (j % 3 == 0)
                            particle(Particle.LAVA, loc);
                    }
                }
            }.runTaskLater(SkillMaster.instance, i * 4);
        }
    }
}
