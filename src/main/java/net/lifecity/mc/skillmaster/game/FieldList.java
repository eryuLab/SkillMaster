package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;

import java.util.ArrayList;

public class FieldList extends ArrayList<GameField> {

    /**
     * ユーザーが参加しているゲームのフィールドを取得します
     * @param user ユーザー
     * @return ユーザーが参加しているゲームのフィールド
     */
    public GameField getFromUser(SkillUser user) {
        Game game = SkillMaster.instance.getGameList().getFromUser(user);
        return getFromGame(game);
    }

    /**
     * 引数のゲームで使用しているフィールドを取得します
     * @param game ゲーム
     * @return 使用中のフィールド
     */
    public GameField getFromGame(Game game) {
        for (GameField field : this) {
            if (field.nowGame == game)
                return field;
        }
        return null;
    }
}
