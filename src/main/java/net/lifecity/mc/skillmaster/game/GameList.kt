package net.lifecity.mc.skillmaster.game

import net.lifecity.mc.skillmaster.user.SkillUser

class GameList {

    val list = mutableListOf<Game>()

    /**
     * ユーザーが参加しているゲームを取得します
     * @param user このユーザーを使います
     * @return 参加していたらtrue
     */
    fun getFromUser(user: SkillUser): Game? {
        for (game in list) {
            if (game.joined(user)) return game
        }
        return null
    }

    /**
     * ユーザーがなんらかのゲームに参加しているかを返します
     * @param user このユーザーを使います
     * @return 参加していたらtrueを返します
     */
    fun inGamingUser(user: SkillUser) = getFromUser(user) != null

}