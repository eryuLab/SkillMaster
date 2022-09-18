package net.lifecity.mc.skillmaster.game;

import net.lifecity.mc.skillmaster.user.SkillUser;

public abstract class Game {

    protected final GameType type; //ゲームのタイプ
    protected final int time; //ゲーム時間
    protected final int countDownTime;

    protected int elapsedTime = 0; //経過時間
    protected GameState state = GameState.WAITING_FOR_STARTING; //ゲームの状態

    /**
     * ゲームのインスタンスを生成します
     * @param type ゲームのタイプ
     * @param time ゲームの時間
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
        // タイマースタート
    }

    public void stop() {
        // ゲーム状態移行
        // 勝敗表示
        // ロビーへ接続
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
}
