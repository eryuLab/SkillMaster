package net.lifecity.mc.skillmaster.skill.skills;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class JumpAttack extends Skill {

    /**
     * step0: 飛び上がってから攻撃できるようになるまで
     * step1: 突っ込みの入力が終わるまで
     * step2: 攻撃の入力が終わるまで
     */
    private int step = 0;

    public JumpAttack(SkillUser user) {
        super("ジャンプアタック", 0, 28, 20, user);
    }

    @Override
    public void activate() { //上方向に高く飛びあがる
        super.activate();

        // 上方向に高く飛びあがる
        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(0.8)
                .setY(1.1);

        user.getPlayer().setVelocity(vector);

        // 指定時間経過後に攻撃入力可能になる
        new BukkitRunnable() {
            @Override
            public void run() {
                step = 1;

                user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            }
        }.runTaskLater(SkillMaster.instance, 7);
    }

    @Override
    public void leftClick() { //向いている方向(下)に突っ込み、攻撃する

        if (step == 1) { //突っ込みの入力

            Vector vector = user.getPlayer().getEyeLocation().getDirection()
                    .normalize()
                    .multiply(1.3)
                    .setY(-1);

            user.getPlayer().setVelocity(vector);

            step = 2; //段階を2に

        } else if (step == 2) {

            // 一番近いEntityを取得
            Entity entity = user.getNearestEntity(2);

            // 近くにEntityがいなければreturn
            if (entity != null) {
                if (entity instanceof Damageable target) {
                    // 標的にダメージを与える
                    target.damage(5);

                    // 標的をノックバックさせる
                    target.setVelocity(user.getPlayer().getVelocity().setY(0.45));

                    // SE再生
                    user.playSound(Sound.ENTITY_PLAYER_ATTACK_CRIT);
                }
            }

            deactivate(); //終了処理
        }

    }

    @Override
    public void deactivate() {
        if (!activating)
            return;

        super.deactivate();

        step = 0;
    }
}