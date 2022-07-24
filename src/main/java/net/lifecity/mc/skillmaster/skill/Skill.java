package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Skill {

    @Getter
    protected final String name;

    @Getter
    protected final int point;

    @Getter
    protected final int interval;

    protected final SkillUser user;

    protected boolean activating = false;

    protected Skill(String name, int point, int interval, SkillUser user) {
        this.name = name;
        this.point = point;
        this.interval = interval;
        this.user = user;
    }

    /**
     * スキルを発動します
     */
    public void activate() {
        // インターバル中か確認
        if (activating)
            return;

        // インターバル開始
        activating = true;

        // 終了処理
        new BukkitRunnable() {
            @Override
            public void run() {
                deactivate();
            }
        }.runTaskLater(SkillMaster.instance, interval);
    }

    /**
     * スキルを終了します
     */
    public void deactivate() {
        if (!activating)
            return;

        activating = false;
    }

}
