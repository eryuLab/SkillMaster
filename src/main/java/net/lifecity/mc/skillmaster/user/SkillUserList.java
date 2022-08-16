package net.lifecity.mc.skillmaster.user;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * スキルユーザーを管理するためのリストクラス
 */
public class SkillUserList extends ArrayList<SkillUser> {

    /**
     * PlayerのUUIDが一致するSkillUserを取得します
     * @param player このプレイヤーのUUIDを使用します
     * @return UUIDが一致したSkillUserまたはnull
     */
    public SkillUser get(Player player) {
        for (SkillUser user : this) {
            if (user.match(player))
                return user;
        }
        return null;
    }

    /**
     * Playerが一致するSkillUserがこのリストに入っているか
     * @param player 対象となるPlayer
     * @return リストにPlayerが入っているか
     */
    public boolean contains(Player player) {
        return get(player) != null;
    }

    /**
     * プレイヤーをSkillUserとしてリストに追加します
     * @param player 追加するPlayer
     */
    public void add(Player player) {
        if (!contains(player)) {
            add(new SkillUser(player));
        }
    }

    /**
     * Playerが一致するSkillUserを削除します
     * @param player 対象となるPlayer
     */
    public void remove(Player player) {
        SkillUser user = get(player);
        if (user != null) remove(user);
    }
}
