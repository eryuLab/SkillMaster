package net.lifecity.mc.skillmaster.game.stage

import net.lifecity.mc.skillmaster.game.Game

/**
 * ゲームに使う場所を管理します
 * スポーンやギミックに使うLocationは継承先のフィールドとして実装してください
 */
class GameStage(val name: String) {
    val fieldMap = mutableMapOf<FieldType, GameField>()

    var nowGame: Game? = null // 稼働中のゲーム

    /**
     * フィールドを追加します
     * @param field フィールド
     */
    fun addField(field: GameField) {
        fieldMap[field.type] = field
    }

    /**
     * タイプを指定し、フィールドを取得します
     * @param type 指定タイプ
     * @return フィールド
     */
    fun getField(type: FieldType) = fieldMap[type]


    /**
     * 引数のゲームタイプとして使えるかを判定します
     * @param type ゲームタイプ
     * @return 使えるとtrue
     */
    fun usableAs(type: FieldType): Boolean {
        val field = fieldMap[type]
        return field != null
    }

    /**
     * 現在このフィールドが使用されているか判定します
     * @return 使われていたらtrue
     */
    fun inUsing() = nowGame != null

}