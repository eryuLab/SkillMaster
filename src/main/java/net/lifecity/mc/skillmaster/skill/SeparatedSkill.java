package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class SeparatedSkill extends Skill {

    protected final int activationTime;

    @Getter
    protected boolean activating = false;

    protected SeparatedSkill(String name, Weapon weapon, int point, int activationTime, int interval, SkillUser user) {
        super(name, weapon, point, interval, user);
        this.activationTime = activationTime;
    }

    /**
     * スキルを発動します
     */
    public void activate() {
        // ログ
        user.sendActionBar(ChatColor.DARK_AQUA + "複合スキル『" + name + "』発動");

        // 発動中にする
        activating = true;

        // 終了処理
        new ActivationTimer();
    }

    private class ActivationTimer extends BukkitRunnable {

        private int count = 0;

        public ActivationTimer() {
            runTaskTimer(SkillMaster.instance, 0, 1);
        }

        @Override
        public void run() {
            if (!activating) //発動中か確認
                cancel();

            if (count >= activationTime) { //カウント確認
                deactivate();
                cancel();
            }

            count++;
        }
    }

    /**
     * 呼び出し前に必ずisInActivating()がtrueになっていることを確認してください
     */
    public abstract void additionalInput();

    /**
     * スキルを終了します
     */
    public void deactivate() {
        if (!activating) //発動していなかったら戻る
            return;

        activating = false; //非発動化する

        // ログ
        user.sendActionBar(ChatColor.RED + "スキル『" + name + "』終了");

        //インターバル処理
        super.deactivate();
    }
}
