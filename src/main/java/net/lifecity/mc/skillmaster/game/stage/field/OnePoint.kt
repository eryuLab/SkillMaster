package net.lifecity.mc.skillmaster.game.stage.field

import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.game.stage.GameField
import net.lifecity.mc.skillmaster.game.stage.GameStage
import org.bukkit.Location

class OnePoint(
    stage: GameStage,
    val point: Location
) : GameField(stage, FieldType.ONE_POINT) {
}