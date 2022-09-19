package net.lifecity.mc.skillmaster.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameResult {
    WIN("勝利", "Win"),
    LOSE("敗北", "Lose"),
    DRAW("引き分け", "Draw");

    @Getter
    private String jp;
    @Getter
    private String en;
}
