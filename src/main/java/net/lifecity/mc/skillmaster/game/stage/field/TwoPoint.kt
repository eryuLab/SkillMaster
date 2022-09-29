package net.lifecity.mc.skillmaster.game.stage.field;

import lombok.Getter;
import net.lifecity.mc.skillmaster.game.stage.FieldType;
import net.lifecity.mc.skillmaster.game.stage.GameField;
import net.lifecity.mc.skillmaster.game.stage.GameStage;
import org.bukkit.Location;

public class TwoPoint extends GameField {

    @Getter
    private final Location pointA;
    @Getter
    private final Location pointB;

    public TwoPoint(GameStage field, Location pointA, Location pointB) {
        super(field, FieldType.TWO_POINT);
        this.pointA = pointA;
        this.pointB = pointB;
    }
}