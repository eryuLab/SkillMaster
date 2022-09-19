package net.lifecity.mc.skillmaster.game;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ゲームに使う場所を管理します
 * スポーンやギミックに使うLocationは継承先のフィールドとして実装してください
 */
public abstract class GameField {

    @Getter
    protected final String name;
    protected final Set<GameType> typeList = new HashSet<>();

    @Getter
    @Setter
    protected Game nowGame = null;// 稼働中のゲーム

    protected GameField(String name, GameType... typeArray) {
        this.name = name;
        this.typeList.addAll(List.of(typeArray));
    }

    /**
     * 引数のゲームタイプとして使えるかを判定します
     * @param type ゲームタイプ
     * @return 使えるとtrue
     */
    public boolean usableAs(GameType type) {
        return typeList.contains(type);
    }

    /**
     * 現在このフィールドが使用されているか判定します
     * @return 使われていたらtrue
     */
    public boolean isUsed() {
        return nowGame != null;
    }
}
