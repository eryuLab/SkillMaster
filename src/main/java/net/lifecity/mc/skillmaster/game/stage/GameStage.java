package net.lifecity.mc.skillmaster.game.stage;

import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.game.Game;
import net.lifecity.mc.skillmaster.game.stage.field.TwoPoint;

import java.util.*;

/**
 * ゲームに使う場所を管理します
 * スポーンやギミックに使うLocationは継承先のフィールドとして実装してください
 */
public class GameStage {

    @Getter
    private final String name;
    @Getter
    private final Map<FieldType, GameField> fieldMap = new HashMap<>();

    @Getter
    @Setter
    private Game nowGame = null;// 稼働中のゲーム

    public GameStage(String name) {
        this.name = name;
    }

    /**
     * フィールドを追加します
     * @param field フィールド
     */
    public void addField(GameField field) {
        fieldMap.put(field.type, field);
    }

    /**
     * タイプを指定し、フィールドを取得します
     * @param type 指定タイプ
     * @return フィールド
     */
    public GameField getField(FieldType type) {
        return fieldMap.get(type);
    }

    /**
     * 引数のゲームタイプとして使えるかを判定します
     * @param type ゲームタイプ
     * @return 使えるとtrue
     */
    public boolean usableAs(FieldType type) {
        GameField field = fieldMap.get(type);
        return field != null;
    }

    /**
     * 現在このフィールドが使用されているか判定します
     * @return 使われていたらtrue
     */
    public boolean inUsing() {
        return nowGame != null;
    }
}
