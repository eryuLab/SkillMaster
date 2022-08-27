package net.lifecity.mc.skillmaster.skill.skills.movefast;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MoveFast extends Skill {

    protected MoveFast() {

    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(1.55);

        // Yの値が+だったら
        if (vector.getY() > 0)
            vector.setY(0.15);

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
