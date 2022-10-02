package net.lifecity.mc.skillmaster.game

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitRunnable

/**
 * ひとつのゲームを管理します
 * フィールドとチームは継承先で実装してください
 */
abstract class Game protected constructor(
    protected val gameType: GameType, //ゲームのタイプ
    protected val fieldType: FieldType, //フィールドのタイプ
    protected val gameTime: Int, //ゲームの時間(秒)
    protected val countDownTime: Int //ゲーム開始前のカウントダウンの時間(秒)
) {
    protected var countDownTimer = CountDownTimer()
    protected var gameTimer = GameTimer()
    protected var elapsedTime = 0 //経過時間
    protected var state = GameState.WAITING_FOR_STARTING //ゲームの状態

    /**
     * このゲームのすべてチームの配列を取得します
     * @return 全てのチーム
     */
    abstract val teams: Array<GameTeam>


    init {
        SkillMaster.instance.gameList.list.add(this)
    }

    /**
     * ゲームをスタートします
     */
    fun start() {
        // テレポート
        teleportAll()

        // ユーザーモード変更
        changeModeAll(UserMode.BATTLE)

        // カウントダウン
        state = GameState.COUNT_DOWN
        countDownTimer.runTaskTimer(SkillMaster.instance, 0, 20)

        // タイマースタート
        gameTimer.runTaskTimer(SkillMaster.instance, countDownTime * 20L, 20)
    }

    /**
     * ゲームを終了します
     */
    fun stop(winners: GameTeam) {
        // タイマーの停止
        if (state === GameState.COUNT_DOWN) countDownTimer.cancel()
        if (state === GameState.IN_GAMING) gameTimer.cancel()

        // ゲーム状態移行
        state = GameState.WAITING_FOR_FINISH

        // 勝利チーム以外のゲームモードをスペクテイターにする
        setGameModeElseTeam(winners, GameMode.SPECTATOR)

        // 勝敗表示
        sendResult()

        // ロビーへ接続
        var count = 0
        val remainTime = 6
        SkillMaster.instance.runTaskTimer(20) {
            if (count >= remainTime-1) {
                // todo ロビーへ接続
                sendMessageAll("ロビーへ接続できた気持ちになってください")
                setGameModeElseTeam(winners, GameMode.SURVIVAL)
                cancel()
            }

            // 残り時間表示
            sendMessageAll("テレポートまで→${remainTime - count}..")
            count++
        }

        // ゲームリストからこのゲームを削除
        SkillMaster.instance.gameList.list.remove(this)
    }

    /**
     * ゲームタイマーが終了したときの処理
     */
    abstract fun afterGameTimer()

    /**
     * 勝敗の結果を表示します
     */
    abstract fun sendResult()

    /**
     * ユーザーがこのゲームに参加しているかを返します
     * @param user ユーザー
     * @return 参加しているときtrue
     */
    fun joined(user: SkillUser): Boolean {
        for (team in teams) {
            if (team.belongs(user)) return true
        }
        return false
    }

    /**
     * このゲームに指定チームが存在するか返します
     * @param team 指定チーム
     * @return 存在したらtrue
     */
    abstract fun hasTeam(team: GameTeam): Boolean


    /**
     * 対象以外のチームのゲームモードを変更します
     * @param elseTeam 対象チーム
     */
    fun setGameModeElseTeam(elseTeam: GameTeam, mode: GameMode) {
        for (team in teams) {
            if (team !== elseTeam) team.setGameMode(mode)
        }
    }

    /**
     * ゲーム内すべてのプレイヤーのユーザーモードを変更します
     * @param mode このモードに変更します
     */
    fun changeModeAll(mode: UserMode) {
        for (team in teams) {
            team.changeMode(mode)
        }
    }

    /**
     * ゲーム内のすべてのプレイヤーにメッセージを送信します
     */
    fun sendMessageAll(msg: String) {
        for (team in teams) {
            team.sendMessage(msg)
        }
    }

    /**
     * 指定チームにメッセージを送信します
     * @param team 指定チーム
     * @param msg メッセージ
     */
    fun sendMessageTeam(team: GameTeam, msg: String) {
        if (hasTeam(team)) team.sendMessage(msg)
    }

    /**
     * ゲーム内のすべてのプレイヤーにタイトルを送信します
     */
    fun sendTitleAll(title: String, sub: String) {
        for (team in teams) {
            team.sendTitle(title, sub)
        }
    }

    /**
     * 指定チームにタイトルを送信します
     * @param team 指定チーム
     */
    fun sendTitleTeam(team: GameTeam, title: String, sub: String) {
        if (hasTeam(team)) team.sendTitle(title, sub)
    }

    fun sendActionbarAll(msg: String) {
        for (team in teams) {
            team.sendActionbar(msg)
        }
    }

    fun sendActionbarTeam(team: GameTeam, msg: String) {
        if (hasTeam(team)) team.sendActionbar(msg)
    }

    fun playSoundAll(sound: Sound) {
        for (team in teams) {
            team.playSound(sound)
        }
    }

    fun playSoundTeam(team: GameTeam, sound: Sound) {
        if (hasTeam(team)) team.playSound(sound)
    }

    /**
     * ゲーム内すべてのプレイヤーを初期地点にテレポートします
     */
    abstract fun teleportAll()

    /**
     * チームごとにプレイヤーを初期地点にテレポートします
     */
    abstract fun teleportTeam(team: GameTeam)


    inner class CountDownTimer : BukkitRunnable() {
        private var count = 0
        override fun run() {
            // ゲームの状態がカウントダウン中でなかったらタスクキャンセル
            if (state !== GameState.COUNT_DOWN) cancel()

            // カウント確認
            if (count >= countDownTime - 1) {
                state = GameState.IN_GAMING
                cancel()
            }

            // タイトル表示
            val title = "${ChatColor.GREEN}${countDownTime - count}.."
            sendTitleAll(title, "")
            playSoundAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
            count++
        }
    }

    inner class GameTimer : BukkitRunnable() {
        override fun run() {
            // 戦闘開始
            if (elapsedTime == 0) {
                // タイトル
                sendTitleAll("${ChatColor.YELLOW}Start!!", "")
                // 爆発音
                playSoundAll(Sound.ENTITY_GENERIC_EXPLODE)
            }

            // ゲームの状態がゲーム中でなかったらタスクキャンセル
            if (state !== GameState.IN_GAMING) cancel()

            // 終了処理
            if (elapsedTime >= gameTime) {
                afterGameTimer()
                cancel()
            }
            sendActionbarAll("count: $elapsedTime")

            // todo ボスバー編集
            elapsedTime++
        }
    }
}