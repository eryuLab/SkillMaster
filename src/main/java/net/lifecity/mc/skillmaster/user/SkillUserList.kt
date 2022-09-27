package net.lifecity.mc.skillmaster.user

import org.bukkit.entity.Player

class SkillUserList {
    val list = mutableListOf<SkillUser>()

    /**
     * PlayerからSkillUserを特定します
     * @param player プレイヤー
     * @return 特定されたSkillUser
     */
    fun get(player: Player): SkillUser? {
        for (user in list) {
            if (user.player == player)
                return user
        }
        return null
    }

    /**
     * playerが格納されているかを返します
     * @param player プレイヤー
     * @return 格納されていたらtrue
     */
    fun contains(player: Player): Boolean = get(player) != null

    /**
     * プレイヤーをSkillUserとして登録します
     * @param player プレイヤー
     */
    fun add(player: Player) {
        if (!contains(player))
            list.add(SkillUser(player))
    }

    /**
     * playerからSkillUserを特定し、格納されていたらリストから削除します
     * @param player プレイヤー
     */
    fun remove(player: Player) {
        val user = get(player)
        if (user != null) list.remove(user)
    }
}