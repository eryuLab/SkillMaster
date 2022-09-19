package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
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
     * このチーム全員のユーザーモードを変更します
     * @param mode このモードに変更します
     */
    public void changeMode(UserMode mode) {
        for (SkillUser user : userArray) {
            user.changeMode(mode);
        }
    }

    /**
     * このチーム全員にメッセージを送信します
     * @param msg メッセージ
     */
    public void sendMessage(String msg) {
        for (SkillUser user : userArray) {
            user.sendMessage(msg);
        }
    }

    /**
     * このチーム全員にタイトルを送信します
     * @param title メインタイトル
     * @param sub サブタイトル
     */
    public void sendTitle(String title, String sub) {
        for (SkillUser user : userArray) {
            user.sendTitle(title, sub);
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