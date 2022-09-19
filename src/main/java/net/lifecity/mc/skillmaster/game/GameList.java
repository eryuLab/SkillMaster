package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.game.prototype.Duel;
import net.lifecity.mc.skillmaster.game.prototype.DuelField;
import net.lifecity.mc.skillmaster.user.SkillUser;

import java.util.ArrayList;

public class GameList extends ArrayList<Game> {

    /**
     * ユーザーが参加しているゲームを取得します
     * @param user このユーザーを使います
     * @return 参加していたらtrue
     */
    public Game getFromUser(SkillUser user) {
        for (Game game : this) {
            if (game.joined(user))
                return game;
        }
        return null;
    }

    /**
     * Fieldからゲームを取得します
     * @param field このFieldを使います
     * @return Fieldを使っているゲーム
     */
    public Game getFromField(GameField field) {
        return field.nowGame;
    }

    // ユーザーがゲームに参加しているか

    /**
     * ユーザーがなんらかのゲームに参加しているかを返します
     * @param user このユーザーを使います
     * @return 参加していたらtrueを返します
     */
    public boolean inGamingUser(SkillUser user) {
        Game game = getFromUser(user);
        return game != null;
    }

    /**
     * フィールドがゲームに使われているかを返します
     * @param field このフィールドを使います
     * @return 使われていたらtrueを返します
     */
    public boolean inGamingField(GameField field) {
        Game game = getFromField(field);
        return game != null;
    }
}
