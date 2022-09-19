package net.lifecity.mc.skillmaster.game;

import lombok.Getter;
import org.bukkit.Location;

public class TwoPointField extends GameField {

    @Getter
    protected final Location spawnPointA;
    @Getter
    protected final Location spawnPointB;

    public TwoPointField(String name, GameType type, Location spawnPointA, Location spawnPointB) {
        super(name, type);
        this.spawnPointA = spawnPointA;
        this.spawnPointB = spawnPointB;
    }
}
