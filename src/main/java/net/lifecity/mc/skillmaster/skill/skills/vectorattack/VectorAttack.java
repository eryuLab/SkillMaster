package net.lifecity.mc.skillmaster.skill.skills.vectorattack;

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
import java.util.List;

public class VectorAttack extends Skill {

    private final double movePower;
    private final double damage;
    private final double attackRadius;
    private final double impact;
    private final double yImpact;
    private final double maxDamageColor;

    protected VectorAttack(
            List<Weapon> weaponList,
            int point,
            int interval,
            SkillUser user,
            double movePower,
            double damage,
            double attackRadius,
            double impact,
            double yImpact,
            double maxDamageColor
    ) {
        super(
                "ベクトルアタック",
                weaponList,
                SkillType.ATTACK,
                Arrays.asList("ユーザーが持つベクトルを力に変換して攻撃します。"),
                point,
                interval,
                user
        );
        this.movePower = movePower;
        this.damage = damage;
        this.attackRadius = attackRadius;
        this.impact = impact;
        this.yImpact = yImpact;
        this.maxDamageColor = maxDamageColor;
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(movePower);

        user.getPlayer().setVelocity(user.getPlayer().getVelocity().add(vector));

        double damage = user.getPlayer().getVelocity().length();
        damage *= this.damage;

        boolean b = user.attackNearest(
                attackRadius,
                damage,
                user.getPlayer().getVelocity().multiply(impact).setY(yImpact),
                Sound.ENTITY_PLAYER_ATTACK_CRIT
        );

        if (b)
            user.sendMessage("damage: " + damage);

        // 軌道
        double finalDamage = damage;
        BlockData data;
        if (finalDamage > maxDamageColor)
            data = Material.RED_CONCRETE_POWDER.createBlockData();
        else if (finalDamage > maxDamageColor - 1)
            data = Material.PURPLE_CONCRETE_POWDER.createBlockData();
        else if (finalDamage > maxDamageColor - 2)
            data = Material.MAGENTA_CONCRETE_POWDER.createBlockData();
        else if (finalDamage > maxDamageColor - 3)
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
