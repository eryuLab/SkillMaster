package net.lifecity.mc.skillmaster.game.stage

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.game.GameType
import org.bukkit.Location

/**
 * ゲームに使う場所を管理します
 * スポーンやギミックに使うLocationは継承先のフィールドとして実装してください
 */
class GameStage(val name: String, val highestHeight: Int, var nowGame: Game?) {
    val fields = mutableMapOf<GameType, GameField>()

    /**
     * フィールドを追加します
     */
    fun addField(gameType: GameType, tpLocations: List<Location>, otherLocations: List<Location>) {
        val field = GameField(tpLocations, otherLocations)
        fields[gameType] = field
    }

    fun getField(gameType: GameType) = fields[gameType] ?: throw Exception()

    /**
     * 現在このフィールドが使用されているか判定します
     * @return 使われていたらtrue
     */
    fun inUsing() = nowGame != null
}