package net.lifecity.mc.skillmaster.game.stage

import net.lifecity.mc.skillmaster.game.Game
import org.bukkit.Location

/**
 * 構造物の中で戦う上で、必要となるLocationを管理するクラス
 */
data class GameField(
    val tpLocations: List<Location>,
    val otherLocations: List<Location>
)