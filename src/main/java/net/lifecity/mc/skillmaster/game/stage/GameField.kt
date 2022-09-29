package net.lifecity.mc.skillmaster.game.stage;

import lombok.Getter;

public class GameField {

    @Getter
    protected final GameStage stage;
    @Getter
    protected final FieldType type;

    protected GameField(GameStage stage, FieldType type) {
        this.stage = stage;
        this.type = type;
    }
}
