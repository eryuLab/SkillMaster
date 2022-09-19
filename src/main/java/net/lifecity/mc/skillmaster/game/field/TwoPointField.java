package net.lifecity.mc.skillmaster.game.field;

import lombok.Getter;
import net.lifecity.mc.skillmaster.game.GameField;
import net.lifecity.mc.skillmaster.game.GameType;
import org.bukkit.Location;

public class TwoPointField extends GameField {

    @Getter
    protected final Location pointA;
    @Getter
    protected final Location pointB;

    public TwoPointField(String name, GameType type, Location pointA, Location pointB) {
        super(name, type);
        this.pointA = pointA;
        this.pointB = pointB;
    }
}
