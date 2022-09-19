package net.lifecity.mc.skillmaster.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public enum GameResult {
    WIN("勝利", "Win"),
    LOSE("敗北", "Lose"),
    DRAW("引き分け", "Draw");

    @Getter
    private String jp;
    @Getter
    private String en;

    public class Data {

        @Getter
        private final Set<GameTeam> winner = new HashSet<>();
        @Getter
        private final Set<GameTeam> loser = new HashSet<>();
        @Getter
        private final Set<GameTeam> drawer = new HashSet<>();
    }
}
