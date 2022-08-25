package net.lifecity.mc.skillmaster.skill.skills.highjump;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * 上に高く飛び上がるスキル
 */
public class SSHighJump extends Skill {

    public SSHighJump(SkillUser user) {
        super("大ジャンプ", Weapon.STRAIGHT_SWORD, SkillType.MOVE, Arrays.asList("上に飛び上がります。"), 4, 0, 30, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection();

        // x方向を制限
        vector.setX(vector.getX() * 0.65);
        // z方向を制限
        vector.setZ(vector.getZ() * 0.65);
        // y方向を拡大
        vector.setY(1);

        user.getPlayer().setVelocity(vector);

        // パーティクル

        // 煙
        for (int i = 0; i < 20; i++) {
            // 座標を生成
            double max = 0.5;
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

            Location loc = user.getPlayer().getLocation().add(x, y, z);

            particle(Particle.CAMPFIRE_COSY_SMOKE, loc);
        }

        // 軌道
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (count >= 20)
                    cancel();
                if (user.getPlayer().getVelocity().length() < 0.3)
                    cancel();
                particle(Particle.LAVA, user.getPlayer().getLocation());
                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1);
    }
}
