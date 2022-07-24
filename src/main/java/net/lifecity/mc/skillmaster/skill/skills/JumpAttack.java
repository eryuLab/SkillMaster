package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.skill.ActionableSkill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class JumpAttack extends ActionableSkill {

    private int step = 0;

    public JumpAttack() {
        super("ジャンプアタック", 0, 40);
    }

    @Override
    public void activate(SkillUser user) { //上方向に高く飛びあがる
        normalAttack = false;

        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(0.7)
                .setY(0.8);

        user.getPlayer().setVelocity(vector);
    }

    @Override
    public void action(SkillUser user) { //向いている方向(下)に突っ込み、攻撃する

        if (step == 0) { //突っ込みの入力
            Vector vector = user.getPlayer().getEyeLocation().getDirection()
                    .normalize()
                    .multiply(0.4)
                    .setY(-0.8);

            user.getPlayer().setVelocity(vector);

            step = 1;

        } else if (step == 1) { //攻撃の入力
            step = 0;
            normalAttack = true;

            // 一番近いEntityを取得
            Entity entity = user.getNearestEntity(2);

            // 近くにEntityがいなければreturn
            if (entity == null)
                return;

            if (entity instanceof Damageable target) {
                // 標的にダメージを与える
                target.damage(5);

                // 標的をノックバックさせる
                target.setVelocity(user.getPlayer().getVelocity().setY(0.45));

                // SE再生
                user.playSound(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK);
            }
        }
    }
}
