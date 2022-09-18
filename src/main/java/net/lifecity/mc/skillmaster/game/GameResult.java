package net.lifecity.mc.skillmaster.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameResult {
    WIN("勝利"),
    LOSE("敗北"),
    DRAW("引き分け");

    @Getter
    private String jp;
}
