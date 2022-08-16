package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * 複数の入力ができる複合スキルのスーパークラス
 */
public abstract class SeparatedSkill extends Skill {

    protected final int activationTime;

    @Getter
    protected boolean activated = false;

    protected SeparatedSkill(String name, Weapon weapon, SkillType type, List<String> lore, int num, int point, int activationTime, int interval, SkillUser user) {
        super(name, weapon, type, lore, num, point, interval, user);
        this.activationTime = activationTime;
    }

    /**
     * スキルを発動します
     */
    public void activate() {
        // ログ
        user.sendActionBar(ChatColor.DARK_AQUA + "複合スキル『" + name + "』発動");

        // 発動中にする
        activated = true;

        // 終了処理
        new ActivationTimer();
    }

    /**
     * 発動している期間を計るクラス
     */
    private class ActivationTimer extends BukkitRunnable {

        private int count = 0;

        public ActivationTimer() {
            runTaskTimer(SkillMaster.instance, 0, 1);
        }

        @Override
        public void run() {
            if (!activated) //発動中か確認
                cancel();

            if (count >= activationTime) { //カウント確認
                deactivate();
                cancel();
            }

            count++;
        }
    }

    /**
     * 追加入力の処理
     */
    public abstract void additionalInput();

    /**
     * スキルを終了します
     */
    public void deactivate() {
        if (!activated) //発動していなかったら戻る
            return;

        activated = false; //非発動化する

        // ログ
        user.sendActionBar(ChatColor.RED + "複合スキル『" + name + "』終了");

        //インターバル処理
        super.deactivate();
    }
}
