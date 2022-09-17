package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.user.SkillUser;

import java.util.ArrayList;

public class DuelList extends ArrayList<Duel> {

    /**
     * ユーザーが参加しているゲームを取得します
     * @param user このユーザーを使います
     * @return 参加していたらtrue
     */
    public Duel getFromUser(SkillUser user) {
        for (Duel duel : this) {
            if (duel.joined(user))
                return duel;
        }
        return null;
    }

    /**
     * Fieldからゲームを取得します
     * @param field このFieldを使います
     * @return Fieldを使っているゲーム
     */
    public Duel getFromField(DuelField field) {
        for (Duel duel : this) {
            if (duel.getField() == field)
                return duel;
        }
        return null;
    }

    // ユーザーがゲームに参加しているか

    /**
     * ユーザーがなんらかのゲームに参加しているかを返します
     * @param user このユーザーを使います
     * @return 参加していたらtrueを返します
     */
    public boolean inGamingUser(SkillUser user) {
        Duel duel = getFromUser(user);
        return duel != null;
    }

    /**
     * フィールドがゲームに使われているかを返します
     * @param field このフィールドを使います
     * @return 使われていたらtrueを返します
     */
    public boolean inGamingField(DuelField field) {
        Duel duel = getFromField(field);
        return duel != null;
    }
}
