package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ひとつのゲームを管理します
 * フィールドとチームは継承先で実装してください
 */
public abstract class Game {

    protected final GameType type; //ゲームのタイプ
    protected final int time; //ゲーム時間
    protected final int countDownTime;

    protected int elapsedTime = 0; //経過時間
    protected GameState state = GameState.WAITING_FOR_STARTING; //ゲームの状態

    /**
     * ゲームのインスタンスを生成します
     * @param type ゲームのタイプ
     * @param time ゲームの時間(秒)
     * @param countDownTime ゲーム開始前のカウントダウンの時間(秒)
     */
    protected Game(GameType type, int time, int countDownTime) {
        this.type = type;
        this.time = time;
        this.countDownTime = countDownTime;
    }

    public void start() {
        // テレポート
        teleportAll();

        // カウントダウン

        // ゲーム状態移行
        state = GameState.IN_GAMING;

        // タイマースタート
    }

    public void stop() {
        // ゲーム状態移行
        state = GameState.WAITING_FOR_FINISH;

        // 勝敗表示

        // ロビーへ接続
    }

    /**
     * ゲーム内のすべてのプレイヤーにタイトルを送信します
     */
    public abstract void sendTitleAll();

    /**
     * 指定チームにタイトルを送信します
     * @param team 指定チーム
     */
    public abstract void sendTitleTeam(GameTeam team);

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
        }
    }
}
