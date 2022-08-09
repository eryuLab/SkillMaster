package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Skill {

    @Getter
    protected final String name;

    protected final Weapon weapon;

    @Getter
    protected final SkillType type;

    protected final int point;

    protected final int interval;

    protected final SkillUser user;

    @Getter
    protected boolean inInterval = false;

    protected Skill(String name, Weapon weapon, SkillType type, int point, int interval, SkillUser user) {
        this.name = name;
        this.weapon = weapon;
        this.type = type;
        this.point = point;
        this.interval = interval;
        this.user = user;
    }

    /**
     * スキルを発動します
     */
    public void activate() {
        // ログ
        user.sendActionBar(ChatColor.DARK_AQUA + "スキル『" + name + "』発動");

        deactivate();
    }

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    public void deactivate() {
        if (inInterval)
            return;

        // インターバル中にする
        inInterval = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                inInterval = false;
            }
        }.runTaskLater(SkillMaster.instance, interval);
    }

    /**
     * 引数の武器が使用可能かを返します
     * @param weapon この武器が使えるか確かめます
     * @return 武器が使えるかどうか
     */
    public boolean usable(Weapon weapon) {
        return this.weapon == weapon;
    }
}
