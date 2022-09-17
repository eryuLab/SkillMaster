package net.lifecity.mc.skillmaster.game;

import org.bukkit.Location;

public class DuelField {

    private final String name;
    private final Location startLocA;
    private final Location startLocB;

    public DuelField(String name, Location startLocA, Location startLocB) {
        this.name = name;
        this.startLocA = startLocA;
        this.startLocB = startLocB;
    }
}
