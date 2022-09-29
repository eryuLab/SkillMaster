package net.lifecity.mc.skillmaster.game.stage.field

import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.game.stage.GameField
import net.lifecity.mc.skillmaster.game.stage.GameStage
import org.bukkit.Location

class TwoPoint(
    stage: GameStage?,
    val pointA: Location,
    val pointB: Location
) : GameField(stage, FieldType.TWO_POINT)