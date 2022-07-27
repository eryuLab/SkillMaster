package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Skill {

    @Getter
    protected final String name;

    protected final int point;

    protected final int activationTime;

    protected final int interval;

    protected final SkillUser user;

    @Getter
    protected boolean activating = false;

    @Getter
    protected boolean inInterval = false;

    protected Skill(String name, int point, int activationTime, int interval, SkillUser user) {
        this.name = name;
        this.point = point;
        this.activationTime = activationTime;
        this.interval = interval;
        this.user = user;
    }

    /**
     * スキルを発動します
     */
    public void activate() {

        // ログ
        user.sendActionBar(ChatColor.DARK_AQUA + "スキル『" + name + "』発動");

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
     * 呼び出し前に必ずinActivating()がtrueになっていることを確認してください
     */
    public abstract void leftClick();

    /**
     * スキルを終了します
     */
    protected void deactivate() {
        if (!activating) //発動していなかったら戻る
            return;

        activating = false; //非発動化する

        // ログ
        user.sendActionBar(ChatColor.RED + "スキル『" + name + "』終了");

        //インターバル処理
        inInterval = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                inInterval = false;
                user.sendMessage("インターバル解除");
            }
        }.runTaskLater(SkillMaster.instance, interval);
    }

}
