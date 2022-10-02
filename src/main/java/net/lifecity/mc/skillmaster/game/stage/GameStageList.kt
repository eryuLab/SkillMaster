package net.lifecity.mc.skillmaster.game.stage

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.user.SkillUser

class GameStageList {
    val list = arrayListOf<GameStage>()

    /**
     * ステージを追加
     * @param target
     */
    fun addStage(target: GameStage) {
        for(stage in list) {
            if(stage.name == target.name) return
        }
        list.add(target)
    }

    /**
     * 名前からステージを取得します
     * @param name 名前
     * @return ステージ
     */
    fun getFromName(name: String) : GameStage? {
        for(stage in list) {
            if(stage.name == name) return stage
        }

        return null
    }

    /**
     * ユーザーが参加しているゲームのフィールドを取得します
     * @param user ユーザー
     * @return ユーザーが参加しているゲームのフィールド
     */
    fun getFromUser(user: SkillUser) : GameStage? {
        val game = SkillMaster.INSTANCE.gameList.getFromUser(user)
        return game?.let { getFromGame(it) }
    }

    /**
     * 引数のゲームで使用しているフィールドを取得します
     * @param game ゲーム
     * @return 使用中のフィールド
     */
    fun getFromGame(game: Game) : GameStage? {
        for(field in list) {
            if(field.nowGame == game) return field
        }
        return null
    }

}