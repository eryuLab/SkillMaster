package net.lifecity.mc.skillmaster.game

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.StageNotFoundException
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.mode.UserMode
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

/**
 * ゲームに対して具体的な処理を行うクラス
 */
class GameManager(val game: Game) {

    private val HEIGHT_LIMIT = 30
    //private val countDownTimer = CountDownTimer()
    //private val gameTimer = GameTimer()
    var elapsedTime = 0 //経過時間
    private var bossBar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SEGMENTED_10)
    private val title : String
        get() {
            val time = game.gameTime - elapsedTime
            return "[${game::class.simpleName}]残り時間: $time"
        }

    init {
        SkillMaster.INSTANCE.gameList.list.add(game)
        game.stage.nowGame = game
    }

    /**
     * ゲームをスタートします
     */
    fun start() {
        // テレポート
        game.teleportAll()

        // ボスバー
        for (team in game.teams) {
            for (user in team.userArray) {
                bossBar.addPlayer(user.player)
            }
        }

        // ユーザーモード変更
        changeModeAll(UserMode.BATTLE)

        // カウントダウン
        game.state = GameState.COUNT_DOWN
        CountDownTimer().runTaskTimer(SkillMaster.INSTANCE, 0, 20)

        val delay = game.countDownTime * 20L
        // タイマースタート
        GameTimer().runTaskTimer(SkillMaster.INSTANCE, delay, 20)
        // 高さ制限
        HeightTimer().runTaskTimer(SkillMaster.INSTANCE, delay, 5)
    }

    /**
     * ゲームを終了します
     */
    fun stop() {
        if (game.state === GameState.WAITING_FOR_FINISH)
            return

        // タイマーの停止
        //if (game.state === GameState.COUNT_DOWN) countDownTimer.cancel()
        //if (game.state === GameState.IN_GAMING) gameTimer.cancel()

        // ゲーム状態移行
        game.state = GameState.WAITING_FOR_FINISH

        // 勝利チーム以外のゲームモードをスペクテイターにする
        setGameModeElseTeam(game.winners, GameMode.SPECTATOR)

        // 勝敗表示
        game.sendResult()

        // ロビーへ接続
        var count = 0
        val remainTime = 6
        SkillMaster.INSTANCE.runTaskTimer(20) {
            // 残り時間表示
            sendMessageAll("テレポートまで→${remainTime - count}..")

            if (count >= remainTime-1) {
                // todo ロビーへ接続
                sendMessageAll("ロビーへ接続できた気持ちになってください")
                setGameModeElseTeam(game.winners, GameMode.SURVIVAL)

                // ボスバーからプレイヤーを削除
                bossBar.removeAll()

                cancel()
            }

            count++
        }

        // ゲームリストからこのゲームを削除
        SkillMaster.INSTANCE.gameList.list.remove(game)
        game.stage.nowGame = null
    }



    /**
     * ユーザーがこのゲームに参加しているかを返します
     * @param user ユーザー
     * @return 参加しているときtrue
     */
    fun joined(user: SkillUser): Boolean {
        for (team in game.teams) {
            if (team.belongs(user)) return true
        }
        return false
    }

    /**
     * ゲームに指定チームが存在するか返します
     * @param team 指定チーム
     * @return 存在したらtrue
     */
    fun hasTeam(targetTeam: GameTeam) : Boolean{
        var result = false
        for(team in this.game.teams) {
            if(team == targetTeam) {
                result = true
            }
        }

        return result
    }

    /**
     * ゲームのステージを取得します
     * @return ステージ
     */
    fun getNowStage(): GameStage {
        for (stage in SkillMaster.INSTANCE.stageList.list) {
            if (stage.nowGame === game) {
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
        for (team in game.teams) {
            if (team !== elseTeam) team.setGameMode(mode)
        }
    }

    /**
     * ゲーム内すべてのプレイヤーのユーザーモードを変更します
     * @param mode このモードに変更します
     */
    fun changeModeAll(mode: UserMode) {
        for (team in game.teams) {
            team.changeMode(mode)
        }
    }

    /**
     * ゲーム内のすべてのプレイヤーにメッセージを送信します
     */
    fun sendMessageAll(msg: String) {
        for (team in game.teams) {
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
        for (team in game.teams) {
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
        for (team in game.teams) {
            team.sendActionbar(msg)
        }
    }

    fun sendActionbarTeam(team: GameTeam, msg: String) {
        if (hasTeam(team)) team.sendActionbar(msg)
    }

    fun playSoundAll(sound: Sound) {
        for (team in game.teams) {
            team.playSound(sound)
        }
    }

    fun playSoundTeam(team: GameTeam, sound: Sound) {
        if (hasTeam(team)) team.playSound(sound)
    }


    inner class CountDownTimer : BukkitRunnable() {
        private var count = 0
        override fun run() {
            // ゲームの状態がカウントダウン中でなかったらタスクキャンセル
            if (game.state !== GameState.COUNT_DOWN) cancel()

            // カウント確認
            if (count >= game.countDownTime - 1) {
                game.state = GameState.IN_GAMING
                cancel()
            }

            // タイトル表示
            val title = "${ChatColor.GREEN}${game.countDownTime - count}.."
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
                game.inStartGameTimer()
            }




            // ゲーム中の処理を追加
            game.inGameTimer()

            // ゲームの状態がゲーム中でなかったらタスクキャンセル
            if (game.state !== GameState.IN_GAMING) cancel()

            // 終了処理
            if (elapsedTime >= game.gameTime) {
                game.afterGameTimer()
                cancel()
            }

            // ボスバー
            // 60 - 10 / 60
            val progress = ((game.gameTime - elapsedTime) / game.gameTime.toDouble())
            bossBar.progress = progress
            bossBar.setTitle(title)

            elapsedTime++
        }
    }

    inner class HeightTimer: BukkitRunnable() {

        private val stage = getNowStage()
        override fun run() {
            if (game.state !== GameState.IN_GAMING)
                cancel()

            // ユーザーの高さ確認
            for (team in game.teams) {
                for (user in team.userArray) {

                    // 1/4秒で確認
                    val userY = user.player.location.y
                    if (userY >= stage.highestHeight + HEIGHT_LIMIT) {
                        // 下方向に飛ばす
                        val vector = Vector(user.player.velocity.x, -4.0, user.player.velocity.z)
                        user.player.velocity = vector
                        user.sendMessage("高さ制限です!!")
                    }
                }
            }
        }
    }
}