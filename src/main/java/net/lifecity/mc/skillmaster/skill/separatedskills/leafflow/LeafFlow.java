package net.lifecity.mc.skillmaster.skill.separatedskills.leafflow;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * 前方に突進しながら敵を攻撃するスキル
 */
import java.util.Arrays;

public class LeafFlow extends SeparatedSkill {

    private final double movePower;
    private final double yPower;
    private final double attackRadius;
    private final double damage;
    private final double attackImpact;
    private final double yImpact;

    /**
     * リーフフローのコンストラクタ
     * @param weapon 使用する武器
     * @param num スキルの番号
     * @param point 消費ポイント
     * @param activationTime 発動時間
     * @param interval インターバル
     * @param user 使用するユーザー
     * @param movePower 突進の強さ
     * @param yPower 突進のY軸への強さ
     * @param attackRadius 攻撃範囲
     * @param damage ダメージ
     * @param attackImpact ノックバック
     * @param yImpact Y軸へのノックバック
     */
    protected LeafFlow(
            Weapon weapon,
            int num,
            int point,
            int activationTime,
            int interval,
            SkillUser user,
            double movePower,
            double yPower,
            double attackRadius,
            double damage,
            double attackImpact,
            double yImpact
    ) {
        super(
                "リーフフロー",
                weapon,
                SkillType.ATTACK,
                Arrays.asList(
                        "前に進みながら斬撃します。",
                        "1回目の入力で前に移動します。",
                        "2回目の入力で攻撃します。"
                ),
                num,
                point,
                activationTime,
                interval,
                user
        );
        this.movePower = movePower;//1
        this.yPower = yPower;//0.15
        this.attackRadius = attackRadius;//1.8
        this.damage = damage;//3
        this.attackImpact = attackImpact;//1
        this.yImpact = yImpact;//0.15
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(movePower)
                .setY(yPower);

        user.getPlayer().setVelocity(vector);

        // [LE]葉の流れ
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (!activated)
                    cancel();
                if (count >= 10)
                    cancel();
                if (user.getPlayer().getVelocity().length() <= 0.3)
                    cancel();

                particle(Particle.FALLING_DUST, user.getPlayer().getEyeLocation(), Material.SPRUCE_LEAVES.createBlockData());

                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1);
    }

    @Override
    public void additionalInput() {

        // 一番近いEntityを攻撃
        boolean b = user.attackNearest(
                attackRadius,
                damage,
                user.getPlayer().getVelocity().normalize().multiply(attackImpact).setY(yImpact),
                Sound.ENTITY_PLAYER_ATTACK_SWEEP
        );

        // LE
        if (b) {
            particle(Particle.EXPLOSION_LARGE, user.getNearEntities(attackRadius).get(0).getLocation().add(0, 2, 0));
            for (int i = 0; i < 6; i++) {
                particle(Particle.FLAME, user.getPlayer().getLocation());
            }
        }

        // 終了
        deactivate();
    }
}
