package net.lifecity.mc.skillmaster.game.stage

import net.lifecity.mc.skillmaster.game.Game
import org.bukkit.Location

data class GameField(
    val tpLocations: List<Location>,
    val otherLocations: List<Location>
)