package net.lifecity.mc.skillmaster.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameType {
    FREE_FOR_ALL("フリーフォーオール"),
    DUEL("デュエル"),
    ONE_ON_ONE("一対一"),
    TEAM_DEATH_MATCH("チームデスマッチ");

    @Getter
    private String jp;
}
