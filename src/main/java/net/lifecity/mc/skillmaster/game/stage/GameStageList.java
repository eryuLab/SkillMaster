package net.lifecity.mc.skillmaster.game.stage;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.game.Game;
import net.lifecity.mc.skillmaster.game.stage.GameStage;
import net.lifecity.mc.skillmaster.user.SkillUser;

import java.util.ArrayList;

public class GameStageList extends ArrayList<GameStage> {

    /**
     * ステージを追加
     * @param target
     */
    public void addStage(GameStage target) {
        // 名前がかぶっていないか確認
        for (GameStage stage : this) {
            if (stage.getName().equals(target.getName()))
                return;
        }
        add(target);
    }

    /**
     * 名前からステージを取得します
     * @param name 名前
     * @return ステージ
     */
    public GameStage getFromName(String name) {
        for (GameStage stage : this) {
            if (stage.getName().equals(name))
                return stage;
        }
        return null;
    }

    /**
     * ユーザーが参加しているゲームのフィールドを取得します
     * @param user ユーザー
     * @return ユーザーが参加しているゲームのフィールド
     */
    public GameStage getFromUser(SkillUser user) {
        Game game = SkillMaster.instance.getGameList().getFromUser(user);
        return getFromGame(game);
    }

    /**
     * 引数のゲームで使用しているフィールドを取得します
     * @param game ゲーム
     * @return 使用中のフィールド
     */
    public GameStage getFromGame(Game game) {
        for (GameStage field : this) {
            if (field.getNowGame() == game)
                return field;
        }
        return null;
    }
}
