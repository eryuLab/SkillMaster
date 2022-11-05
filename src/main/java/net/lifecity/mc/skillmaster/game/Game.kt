package net.lifecity.mc.skillmaster.game

import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.user.SkillUser

/**
 * ひとつのゲームのひな型です(抽象的)
 */
interface Game {
    val gameType: GameType
    val countDownTime: Int
    val gameTime: Int
    var state: GameState
    val teams: Array<GameTeam>
    val winners: GameTeam
    val losers: GameTeam
    val gameStage: GameStage

    /**
     * ゲームタイマーが開始したときの処理
     */
    fun inStartGameTimer()

    /**
     * ゲームタイマーの中で１秒ごとに実行されるタスク
     */
    fun inGameTimer()

    /**
     * ゲームタイマーが終了したときの処理
     */
    fun afterGameTimer()

    /**
     * 勝敗の結果を表示します
     */
    fun sendResult()

    /**
     * ゲーム内すべてのプレイヤーを初期地点にテレポートします
     */
    fun teleportAll()

    /**
     * チームごとにプレイヤーを初期地点にテレポートします
     */
    fun teleportTeam(team: GameTeam)

    fun onUserAttack(attacker: SkillUser)

    fun onUserDead(dead: SkillUser)


}