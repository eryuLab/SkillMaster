package net.lifecity.mc.skillmaster.user

import org.bukkit.entity.Player

/**
 * スキルユーザーを管理するためのリストクラス
 */
class SkillUserList : ArrayList<SkillUser>() {
    /**
     * PlayerのUUIDが一致するSkillUserを取得します
     * @param player このプレイヤーのUUIDを使用します
     * @return UUIDが一致したSkillUserまたはnull
     */
    operator fun get(player: Player?): SkillUser? {
        for (user in this) {
            if (user.match(player)) return user
        }
        return null
    }

    /**
     * Playerが一致するSkillUserがこのリストに入っているか
     * @param player 対象となるPlayer
     * @return リストにPlayerが入っているか
     */
    operator fun contains(player: Player): Boolean {
        return get(player) != null
    }

    /**
     * プレイヤーをSkillUserとしてリストに追加します
     * @param player 追加するPlayer
     */
    fun add(player: Player) {
        if (!contains(player)) {
            add(SkillUser(player, UserMode.BATTLE))
        }
    }

    /**
     * Playerが一致するSkillUserを削除します
     * @param player 対象となるPlayer
     */
    fun remove(player: Player) {
        val user = get(player)
        user?.let { remove(it) }
    }
}