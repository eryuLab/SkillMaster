package net.lifecity.mc.skillmaster.game

enum class GameState(val jp: String) {
    WAITING_FOR_STARTING("待機中"), COUNT_DOWN("カウントダウン中"), IN_GAMING("ゲーム中"), WAITING_FOR_FINISH("終了処理中");
}