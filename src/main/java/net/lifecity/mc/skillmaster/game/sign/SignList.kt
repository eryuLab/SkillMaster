package net.lifecity.mc.skillmaster.game.sign

import org.bukkit.Location
import org.bukkit.block.Sign

class SignList {

    val list = mutableListOf<Location>()

    /**
     * 看板の座標が登録されているか確認します
     * @param sign 看板
     * @return 登録されているときtrue
     */
    fun contains(sign: Sign): Boolean {
        return contains(sign.location)
    }
    fun contains(location: Location): Boolean {
        for (loc in list) {
            if (loc.blockX == location.blockX && loc.blockY == location.blockY && loc.blockZ == location.blockZ)
                return true
        }
        return false
    }

    /**
     * 看板をリストに追加します
     * @param sign 看板
     */
    fun add(sign: Sign) {
        add(sign.location)
    }
    fun add(location: Location) {
        if (contains(location))
            return
        list.add(location)
    }

    /**
     * 看板をリストから削除します
     * @param sign 看板
     */
    fun remove(sign: Sign) {
        remove(sign.location)
    }
    fun remove(location: Location) {
        for (loc in list) {
            if (loc.blockX == location.blockX && loc.blockY == location.blockY && loc.blockZ == location.blockZ) {
                list.remove(loc)
                break
            }
        }
    }
}