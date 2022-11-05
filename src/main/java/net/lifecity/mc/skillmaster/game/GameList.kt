package net.lifecity.mc.skillmaster.game

import net.lifecity.mc.skillmaster.GameNotFoundException
import net.lifecity.mc.skillmaster.SkillUserNotFoundException
import net.lifecity.mc.skillmaster.user.SkillUser

class GameList {

    val list = mutableListOf<Game>()

    /**
     * ユーザーが参加しているゲームを取得します
     * @param user このユーザーを使います
     * @return 参加していたらtrue
     */
    fun getFromUser(user: SkillUser): Game {
        for (game in list) {
            if (game.gameManager.joined(user)) return game
        }
        throw GameNotFoundException("${user.player.name} do not join any games now")
    }

    /**
     * ユーザーがなんらかのゲームに参加しているかを返します
     * @param user このユーザーを使います
     * @return 参加していたらtrueを返します
     */
    fun inGamingUser(user: SkillUser) : Boolean{
        for (game in list) {
            if (game.gameManager.joined(user)) return true
        }
        return false
    }

}