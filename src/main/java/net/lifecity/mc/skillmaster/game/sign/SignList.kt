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
        val singLoc = sign.location
        for (loc in list) {
            if (loc.blockX == singLoc.blockX && loc.blockY == singLoc.blockY && loc.blockZ == singLoc.blockZ)
                return true
        }
        return false
    }

    /**
     * 看板をリストに追加します
     * @param sign 看板
     */
    fun add(sign: Sign) {
        if (contains(sign))
            return
        list.add(sign.location)
    }

    /**
     * 看板をリストから削除します
     * @param sign 看板
     */
    fun remove(sign: Sign) {
        val singLoc = sign.location
        for (loc in list) {
            if (loc.blockX == singLoc.blockX && loc.blockY == singLoc.blockY && loc.blockZ == singLoc.blockZ) {
                list.remove(loc)
                break
            }
        }
    }
}