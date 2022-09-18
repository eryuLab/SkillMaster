package net.lifecity.mc.skillmaster.game;

public abstract class Game {

    protected final String name;
    protected final int time;
    // フィールドはサブクラスのフィールド
    // チームはサブクラスのフィールド

    protected int elapsedTime; //経過時間
    protected GameState state; //ゲームの状態

    protected Game(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public void start() {

    }

    public void stop() {

    }
}
