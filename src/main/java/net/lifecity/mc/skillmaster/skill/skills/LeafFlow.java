package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.skill.ActionableSkill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class LeafFlow extends ActionableSkill {

    public LeafFlow() {
        super("リーフフロー", 0, 10);
    }

    @Override
    public void activate(SkillUser user) {
        normalAttack = false;

        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(1)
                .setY(0.15);

        user.getPlayer().setVelocity(vector);
    }

    @Override
    public void action(SkillUser user) {
        // 再度攻撃不可(通常攻撃に変換)
        normalAttack = true;

        // 一番近いEntityを取得
        Entity entity = user.getNearestEntity(1.8);

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
