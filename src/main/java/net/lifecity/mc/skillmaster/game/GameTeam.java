package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class GameTeam {

    private final String name;
    private final ChatColor color;
    private final SkillUser[] userArray;

    public GameTeam(String name, ChatColor color, SkillUser[] userArray) {
        this.name = name;
        this.color = color;
        this.userArray = userArray;
    }

    /**
     * このチーム全員にタイトルを送信します
     * @param msg メインタイトル
     * @param sub サブタイトル
     */
    public void sendTitle(String msg, String sub) {
        for (SkillUser user : userArray) {
            user.sendTitle(msg, sub);
        }
    }

    /**
     * チーム内すべてのプレイヤーを指定地点へテレポートします
     * @param location 指定地点
     */
    public void teleportAll(Location location) {
        for (SkillUser user : userArray) {
            user.getPlayer().teleport(location);
        }
    }

    /**
     * このチームに指定ユーザーが所属しているか判定します
     * @param target 指定ユーザー
     * @return 所属していたらtrue
     */
    public boolean belongs(SkillUser target) {
        for (SkillUser user : userArray) {
            if (user == target)
                return true;
        }
        return false;
    }
}
