package net.lifecity.mc.skillmaster.game;

import lombok.Getter;
import lombok.Setter;

/**
 * ゲームに使う場所を管理します
 * スポーンやギミックに使うLocationは継承先のフィールドとして実装してください
 */
public abstract class GameField {

    protected final String name;
    protected final GameType type;

    @Getter
    @Setter
    protected Game nowGame = null;// 稼働中のゲーム

    protected GameField(String name, GameType type) {
        this.name = name;
        this.type = type;
    }
}
