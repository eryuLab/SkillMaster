package net.lifecity.mc.skillmaster.game;

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
     * ユーザーがなんらかのゲームに参加しているかを返します
     * @param user このユーザーを使います
     * @return 参加していたらtrueを返します
     */
    public boolean inGamingUser(SkillUser user) {
        Game game = getFromUser(user);
        return game != null;
    }
}
