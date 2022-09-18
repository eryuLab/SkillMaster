package net.lifecity.mc.skillmaster.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameState {
    WAITING_FOR_STARTING("待機中"),
    IN_GAMING("ゲーム中"),
    WAITING_FOR_FINISH("終了処理中");

    @Getter
    private String jp;
}
