package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ひとつのゲームを管理します
 * フィールドとチームは継承先で実装してください
 */
public abstract class Game {

    protected final GameType type; //ゲームのタイプ
    protected final int gameTime; //ゲーム時間
    protected final int countDownTime;

    protected CountDownTimer countDownTimer = new CountDownTimer();
    protected GameTimer gameTimer = new GameTimer();
    protected int elapsedTime = 0; //経過時間
    protected GameState state = GameState.WAITING_FOR_STARTING; //ゲームの状態

    /**
     * ゲームのインスタンスを生成します
     * @param type ゲームのタイプ
     * @param gameTime ゲームの時間(秒)
     * @param countDownTime ゲーム開始前のカウントダウンの時間(秒)
     */
    protected Game(GameType type, int gameTime, int countDownTime) {
        this.type = type;
        this.gameTime = gameTime;
        this.countDownTime = countDownTime;
    }

    /**
     * ゲームをスタートします
     */
    public void start() {
        // テレポート
        teleportAll();

        // ユーザーモード変更
        changeModeAll(UserMode.BATTLE);

        // カウントダウン
        countDownTimer.runTaskTimer(SkillMaster.instance, 0, 20);

        // ゲーム状態移行
        state = GameState.IN_GAMING;

        // タイマースタート
        gameTimer.runTaskTimer(SkillMaster.instance, countDownTime * 20L, gameTime);
    }

    /**
     * ゲームを終了します
     */
    public void stop() {
        // タイマーの停止
        if (state == GameState.COUNT_DOWN)
            countDownTimer.cancel();

        if (state == GameState.IN_GAMING)
            gameTimer.cancel();

        // ゲーム状態移行
        state = GameState.WAITING_FOR_FINISH;

        // 勝敗表示
        sendResult();

        // ロビーへ接続
        new BukkitRunnable() {
            private int count = 0;
            @Override
            public void run() {
                // 時間が経過していたら終了処理
                if (count >= 6) {
                    // todo ロビーへ接続
                    sendMessageAll("ロビーへ接続できた気持ちになってください");

                    cancel();
                }

                // 残り時間表示
                sendMessageAll(6 - count + "..");

                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 20);
    }

    /**
     * 勝敗の結果を表示します
     */
    public abstract void sendResult();

    // ゲームに指定チームが所属しているか確認するメソッド
    // チームのリストを取得するメソッド

    /**
     * このゲームに指定チームが存在するか返します
     * @param team 指定チーム
     * @return 存在したらtrue
     */
    public abstract boolean hasTeam(GameTeam team);

    /**
     * このゲームのすべてチームの配列を取得します
     * @return 全てのチーム
     */
    public abstract GameTeam[] getTeams();

    /**
     * ゲーム内すべてのプレイヤーのユーザーモードを変更します
     * @param mode このモードに変更します
     */
    public void changeModeAll(UserMode mode) {
        for (GameTeam team : getTeams()) {
            team.changeMode(mode);
        }
    }

    /**
     * 指定チームのユーザーモードを変更します
     * @param team 指定チーム
     * @param mode モード
     */
    public void changeModeTeam(GameTeam team, UserMode mode) {
        if (hasTeam(team))
            team.changeMode(mode);
    }

    /**
     * ゲーム内のすべてのプレイヤーにメッセージを送信します
     */
    public void sendMessageAll(String msg) {
        for (GameTeam team : getTeams()) {
            team.sendMessage(msg);
        }
    }

    /**
     * 指定チームにメッセージを送信します
     * @param team 指定チーム
     * @param msg メッセージ
     */
    public void sendMessageTeam(GameTeam team, String msg) {
        if (hasTeam(team))
            team.sendMessage(msg);
    }

    /**
     * ゲーム内のすべてのプレイヤーにタイトルを送信します
     */
    public void sendTitleAll(String title, String sub) {
        for (GameTeam team : getTeams()) {
            team.sendTitle(title, sub);
        }
    }

    /**
     * 指定チームにタイトルを送信します
     * @param team 指定チーム
     */
    public void sendTitleTeam(GameTeam team, String title, String sub) {
        if (hasTeam(team))
            team.sendTitle(title, sub);
    }

    /**
     * ゲーム内すべてのプレイヤーを初期地点にテレポートします
     */
    public abstract void teleportAll();

    /**
     * チームごとにプレイヤーを初期地点にテレポートします
     */
    public abstract void teleportTeam(GameTeam team);

    /**
     * ゲーム内の特定のプレイヤーを初期地点にテレポートします
     */
    public abstract void teleportOne(SkillUser target);

    private class CountDownTimer extends BukkitRunnable {

        private int count = 0;

        @Override
        public void run() {
            // ゲームの状態がカウントダウン中でなかったらタスクキャンセル
            if (state != GameState.COUNT_DOWN)
                cancel();

            // タイトル表示
            String title = ChatColor.GREEN + "" + (countDownTime - count) + "..";
            sendTitleAll(title, "");

            count++;
        }
    }

    private class GameTimer extends BukkitRunnable {

        @Override
        public void run() {
            // ゲームの状態がゲーム中でなかったらタスクキャンセル
            if (state != GameState.IN_GAMING)
                cancel();

            // ボスバー編集

            elapsedTime++;
        }
    }
}
