package net.lifecity.mc.skillmaster.skill.separatedskills;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class JumpAttack extends SeparatedSkill {

    /**
     * step0: 飛び上がってから攻撃できるようになるまで
     * step1: 突っ込みの入力が終わるまで
     * step2: 攻撃の入力が終わるまで
     */
    private int step = 0;

    public JumpAttack(SkillUser user) {
        super("ジャンプアタック", Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.GREAT_SWORD, Weapon.MACE), 0, 28, 20, user);
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
    public void additionalInput() { //向いている方向(下)に突っ込み、攻撃する

        if (step == 1) { //突っ込みの入力

            Vector vector = user.getPlayer().getEyeLocation().getDirection()
                    .normalize()
                    .multiply(1.3)
                    .setY(-1);

            user.getPlayer().setVelocity(vector);

            step = 2; //段階を2に

        } else if (step == 2) {

            // 一番近いEntityを攻撃
            user.attackNearest(
                    2,
                    5,
                    user.getPlayer().getVelocity().setY(0.5),
                    Sound.ENTITY_PLAYER_ATTACK_CRIT
            );

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