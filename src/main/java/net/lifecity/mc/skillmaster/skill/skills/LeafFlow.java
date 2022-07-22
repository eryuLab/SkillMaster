package net.lifecity.mc.skillmaster.skill.skills;

import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.ActionableSkill;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LeafFlow extends ActionableSkill {

    @Getter
    @Setter
    private double radius = 1;

    public LeafFlow() {
        super("リーフフロー", 0, 40);
    }

    @Override
    public void activate(SkillUser user) {

        Location from = user.getPlayer().getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                Location to = user.getPlayer().getLocation();

                Vector vector = to.toVector().subtract(from.toVector());

                vector.setY(0).normalize().multiply(1.5).setY(0.2);

                if (Double.isNaN(vector.length())) {
                    user.sendMessage("NaN");
                    return;
                }

                user.getPlayer().setVelocity(vector);
            }
        }.runTaskLater(SkillMaster.instance, 1);
    }

    @Override
    public void action(SkillUser user) {
        // 再度攻撃不可
        if (!actionable) {
            user.attack();
            return;
        }
        actionable = false;

        // 一番近いEntityを取得
        Entity entity = user.getNearestEntity(radius);

        // 近くにEntityがいなければreturn
        if (entity == null)
            return;

        if (entity instanceof Damageable target) {
            // 標的にダメージを与える
            target.damage(3);

            // 標的をノックバックさせる
            target.setVelocity(user.getPlayer().getVelocity().normalize().setY(0.15));

            // SE再生
            user.playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP);
        }
    }
}
