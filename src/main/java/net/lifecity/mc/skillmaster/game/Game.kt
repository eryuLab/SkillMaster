package net.lifecity.mc.skillmaster.game

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.StageNotFoundException
import net.lifecity.mc.skillmaster.game.stage.FieldType
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

/**
 * ひとつのゲームを管理します
 * フィールドとチームは継承先で実装してください
 */
abstract class Game protected constructor(
    protected val stage: GameStage, //ゲームに使うステージ
    protected val gameType: GameType, //ゲームのタイプ
    protected val fieldType: FieldType, //フィールドのタイプ
    protected val gameTime: Int, //ゲームの時間(秒)
    protected val countDownTime: Int //ゲーム開始前のカウントダウンの時間(秒)
) {
    private val HEIGHT_LIMIT = 30
    protected val countDownTimer = CountDownTimer()
    protected var elapsedTime = 0 //経過時間
    protected val gameTimer = GameTimer()
    protected var state = GameState.WAITING_FOR_STARTING //ゲームの状態
    protected var bossBar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SEGMENTED_10)
    private val title : String
        get() {
            val time = gameTime - elapsedTime
            return "[${gameType.jp}]残り時間: $time"
        }

    /**
     * このゲームのすべてチームの配列を取得します
     * @return 全てのチーム
     */
    abstract val teams: Array<GameTeam>


    init {
        SkillMaster.INSTANCE.gameList.list.add(this)
        stage.nowGame = this
    }

    /**
     * ゲームをスタートします
     */
    fun start() {
        // テレポート
        teleportAll()

        // ボスバー
        for (team in teams) {
            for (user in team.userArray) {
                bossBar.addPlayer(user.player)
            }
        }

        // ユーザーモード変更
        changeModeAll(UserMode.Battle)

        // カウントダウン
        state = GameState.COUNT_DOWN
        countDownTimer.runTaskTimer(SkillMaster.INSTANCE, 0, 20)

        // タイマースタート
        gameTimer.runTaskTimer(SkillMaster.INSTANCE, countDownTime * 20L, 20)
    }

    /**
     * ゲームを終了します
     */
    fun stop(winners: GameTeam) {

        // ボスバー
        for (team in teams) {
            for (user in team.userArray) {
                bossBar.removePlayer(user.player)
            }
        }

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
        SkillMaster.INSTANCE.runTaskTimer(20) {
            // 残り時間表示
            sendMessageAll("テレポートまで→${remainTime - count}..")

            if (count >= remainTime-1) {
                // todo ロビーへ接続
                sendMessageAll("ロビーへ接続できた気持ちになってください")
                setGameModeElseTeam(winners, GameMode.SURVIVAL)
                cancel()
            }

            count++
        }

        // ゲームリストからこのゲームを削除
        SkillMaster.INSTANCE.gameList.list.remove(this)
        stage.nowGame = null
    }

    /**
     * ゲームタイマーが開始したときの処理
     */
    abstract fun inStartGameTimer()

    /**
     * ゲームタイマーの中で１秒ごとに実行されるタスク
     */
    abstract fun inGameTimer()

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
     * このゲームのステージを取得します
     * @return ステージ
     */
    fun getNowStage(): GameStage {
        for (stage in SkillMaster.INSTANCE.stageList.list) {
            if (stage.nowGame === this) {
                return stage
            }
        }
        throw StageNotFoundException("stage is not found")
    }


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

                // 開始時の処理を呼び出し
                inStartGameTimer()
            }

            // ユーザーの高さ確認
            for (team in teams) {
                for (user in team.userArray) {
                    val stage = getNowStage()

                    // 1/4秒で確認

                    stage.let {
                        var count = 0
                        SkillMaster.INSTANCE.runTaskTimer(5) {
                            val userY = user.player.location.y
                            if (userY >= stage.highestHeight + HEIGHT_LIMIT) {
                                // 下方向に飛ばす
                                val vector = Vector(user.player.velocity.x, -4.0, user.player.velocity.z)
                                user.player.velocity = vector
                                user.sendMessage("高さ制限です!!")
                            }
                            if (count > 3) {
                                this.cancel()
                            }
                            count++
                        }
                    }

                }
            }
            // ゲーム中の処理を追加
            inGameTimer()

            // ゲームの状態がゲーム中でなかったらタスクキャンセル
            if (state !== GameState.IN_GAMING) cancel()

            // 終了処理
            if (elapsedTime >= gameTime) {
                afterGameTimer()
                cancel()
            }

            // ボスバー
            // 60 - 10 / 60
            val progress = ((gameTime - elapsedTime) / gameTime.toDouble())
            bossBar.progress = progress
            bossBar.setTitle(title)

            elapsedTime++
        }
    }
}