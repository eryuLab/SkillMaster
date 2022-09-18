package net.lifecity.mc.skillmaster.game.oneonone;

import net.lifecity.mc.skillmaster.game.GameField;
import net.lifecity.mc.skillmaster.game.GameType;
import org.bukkit.Location;

public class OneOnOneField extends GameField {

    protected final Location spawnPointA;
    protected final Location spawnPointB;

    protected OneOnOneField(String name, Location spawnPointA, Location spawnPointB) {
        super(name, GameType.ONE_ON_ONE);
        this.spawnPointA = spawnPointA;
        this.spawnPointB = spawnPointB;
    }
}
