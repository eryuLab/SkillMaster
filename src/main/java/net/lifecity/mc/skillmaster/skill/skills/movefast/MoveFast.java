package net.lifecity.mc.skillmaster.skill.skills.movefast;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class MoveFast extends Skill {

    private final double movePower;
    private final double yPower;

    protected MoveFast(
            List<Weapon> weaponList,
            int point,
            int interval,
            SkillUser user,
            double movePower,
            double yPower
    ) {
        super(
                "高速移動",
                weaponList,
                SkillType.MOVE,
                Arrays.asList("向いている方向に高速移動します。", "上方向には飛べません。"),
                point,
                interval,
                user
        );
        this.movePower = movePower;
        this.yPower = yPower;
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(movePower);

        // Yの値が+だったら
        if (vector.getY() > 0)
            vector.setY(yPower);

        user.getPlayer().setVelocity(vector);

        // 軌道
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (count >= 7)
                    cancel();
                if (user.getPlayer().getVelocity().length() < 0.3)
                    cancel();
                if (count % 2 == 1)
                    particle(Particle.FALLING_DUST, user.getPlayer().getLocation(), Material.OXIDIZED_COPPER.createBlockData());
                if (count %  2 == 0)
                    particle(Particle.FALLING_DUST, user.getPlayer().getLocation(), Material.IRON_BLOCK.createBlockData());
                if (count % 3 == 0)
                    particle(Particle.FALLING_DUST, user.getPlayer().getLocation(), Material.ICE.createBlockData());
                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1);
    }
}
