package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.SkillUserNotFoundException
import org.bukkit.entity.Player

class SkillUserList {
    val list = mutableListOf<SkillUser>()

    /**
     * PlayerからSkillUserを特定します
     * @param player プレイヤー
     * @return 特定されたSkillUser
     */
    operator fun get(player: Player): SkillUser {
        for (user in list) {
            if (user.player == player)
                return user
        }
        throw SkillUserNotFoundException("SkillUser:${player.name} is not found")
    }

    /**
     * playerが格納されているかを返します
     * @param player プレイヤー
     * @return 格納されていたらtrue
     */
    fun contains(player: Player): Boolean {
        for (user in list) {
            if(user.player == player) return true
        }
        return false
    }

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
        list.remove(user)
    }
}