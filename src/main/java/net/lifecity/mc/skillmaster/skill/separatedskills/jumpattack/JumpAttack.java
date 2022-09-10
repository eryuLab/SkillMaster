package net.lifecity.mc.skillmaster.skill.separatedskills.jumpattack;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;

/**
 * 飛び上がり、地面に向かって突撃するスキル
 */
public abstract class JumpAttack extends SeparatedSkill {

    /**
     * step0: 飛び上がってから攻撃できるようになるまで
     * step1: 突っ込みの入力が終わるまで
     * step2: 攻撃の入力が終わるまで
     */
    private int step = 0;

    private final double jumpMove;
    private final double jumpPower;
    private final int delay;
    private final double fallMove;
    private final int fallPower;
    private final int attackRadius;
    private final int damage;
    private final double yImpact;

    /**
     * ジャンプアタックのコンストラクタ
     * @param weapon 使用する武器
     * @param point 消費ポイント
     * @param activationTime 発動時間
     * @param interval インターバル
     * @param user 使用するユーザー
     * @param jumpMove ジャンプ時の横移動の強さ
     * @param jumpPower ジャンプの強さ
     * @param delay 突撃可能までの時間
     * @param fallMove 落下時の横移動の強さ
     * @param fallPower 落下の強さ
     * @param attackRadius 攻撃範囲
     * @param damage ダメージ
     * @param yImpact Y軸へのノックバック
     */
    protected JumpAttack(
            Weapon weapon,
            int point,
            int activationTime,
            int interval,
            SkillUser user,
            double jumpMove,
            double jumpPower,
            int delay,
            double fallMove,
            int fallPower,
            int attackRadius,
            int damage,
            double yImpact
    ) {
        super(
                "ジャンプアタック",
                weapon, SkillType.ATTACK,
                Arrays.asList(
                        "上に飛び上がり、地面に突撃します。",
                        "1回目の入力で上に飛び上がります。",
                        "2回目の入力で素早く落下します。",
                        "3回目の入力で攻撃します。"
                ),
                point,
                activationTime,
                interval,
                user
        );
        this.jumpMove = jumpMove;
        this.jumpPower = jumpPower;
        this.delay = delay;
        this.fallMove = fallMove;
        this.fallPower = fallPower;
        this.attackRadius = attackRadius;
        this.damage = damage;
        this.yImpact = yImpact;
    }

    @Override
    public void activate() { //上方向に高く飛びあがる
        super.activate();

        // 上方向に高く飛びあがる
        Vector vector = user.getPlayer().getEyeLocation().getDirection()
                .normalize()
                .multiply(jumpMove)
                .setY(jumpPower);

        user.getPlayer().setVelocity(vector);

        // 煙
        for (int i = 0; i < 10; i++) {
            // 座標を生成
            double max = 0.5;
            Random random = new Random();

            double x = max * random.nextDouble();
            if (random.nextBoolean())
                x *= -1;

            double y = max * random.nextDouble();
            if (random.nextBoolean())
                y *= -1;

            double z = max * random.nextDouble();
            if (random.nextBoolean())
                z *= -1;

            Location loc = user.getPlayer().getLocation().add(x, y, z);

            particle(Particle.CAMPFIRE_COSY_SMOKE, loc);
        }

        // 指定時間経過後に攻撃入力可能になる
        new BukkitRunnable() {
            @Override
            public void run() {
                step = 1;

                user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            }
        }.runTaskLater(SkillMaster.instance, delay);
    }

    @Override
    public void additionalInput() { //向いている方向(下)に突っ込み、攻撃する

        if (step == 1) { //突っ込みの入力

            Vector vector = user.getPlayer().getEyeLocation().getDirection()
                    .normalize()
                    .multiply(fallMove)
                    .setY(fallPower);

            user.getPlayer().setVelocity(vector);

            step = 2; //段階を2に

            // LE
            particle(Particle.EXPLOSION_LARGE, user.getPlayer().getEyeLocation());

            // 軌道
            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    if (step == 0)
                        cancel();
                    if (count >= 10)
                        cancel();
                    if (user.getPlayer().getVelocity().length() < 0.3)
                        cancel();

                    particle(Particle.FALLING_DUST, user.getPlayer().getLocation().add(0, 1, 0), Material.ICE.createBlockData());

                    count++;
                }
            }.runTaskTimer(SkillMaster.instance, 0, 1);

        } else if (step == 2) {

            // 一番近いEntityを攻撃
            boolean b = user.attackNearest(
                    attackRadius,
                    damage,
                    user.getPlayer().getVelocity().setY(yImpact),
                    Sound.ENTITY_PLAYER_ATTACK_CRIT
            );

            if (b)
                particle(Particle.EXPLOSION_LARGE, user.getNearEntities(attackRadius).get(0).getLocation().add(0, 2, 0));

            deactivate(); //終了処理
        }

    }

    @Override
    public void deactivate() {
        if (!activated)
            return;

        super.deactivate();

        step = 0;
    }
}
