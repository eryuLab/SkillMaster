package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class VectorAttack extends Skill {

    public VectorAttack(SkillUser user) {
        super(
                "ベクトルアタック",
                Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.GREAT_SWORD, Weapon.LONG_SWORD, Weapon.MACE),
                SkillType.ATTACK,
                Arrays.asList("ユーザーが持つベクトルを力に変換して攻撃します。"),
                0,
                40,
                user
        );
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(1.2);

        user.getPlayer().setVelocity(user.getPlayer().getVelocity().add(vector));

        double damage = user.getPlayer().getVelocity().length();
        damage *= 2;

        boolean b = user.attackNearest(
                1.8,
                damage,
                user.getPlayer().getVelocity().multiply(0.25).setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_CRIT
        );

        if (b)
            user.sendMessage("damage: " + damage);

        // 軌道
        double finalDamage = damage;
        BlockData data;
        if (finalDamage > 5)
            data = Material.RED_CONCRETE_POWDER.createBlockData();
        else if (finalDamage > 4)
            data = Material.PURPLE_CONCRETE_POWDER.createBlockData();
        else if (finalDamage > 3)
            data = Material.MAGENTA_CONCRETE_POWDER.createBlockData();
        else if (finalDamage > 2)
            data = Material.PINK_CONCRETE_POWDER.createBlockData();
        else
            data = Material.WHITE_CONCRETE_POWDER.createBlockData();

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (count >= 8)
                    cancel();
                if (user.getPlayer().getVelocity().length() < 0.47)
                    cancel();
                particle(Particle.FALLING_DUST, user.getPlayer().getLocation(), data);
                if (finalDamage > 5)
                    particle(Particle.LAVA, user.getPlayer().getLocation());
                else if (finalDamage > 4 && count % 2 == 1)
                    particle(Particle.LAVA, user.getPlayer().getLocation());
                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1);
    }
}
