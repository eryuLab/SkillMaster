package net.lifecity.mc.skillmaster.utils

import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * Entityのリストをプレイヤーから近い順に並べ替えるクラス
 */
object EntityDistanceSort {
    private fun swap(list: MutableList<Entity>, i: Int, j: Int) {
        val entity = list[i]
        list[i] = list[j]
        list[j] = entity
    }

    private fun partition(player: Player, list: MutableList<Entity>, start: Int, end: Int): Int {
        //アレイからピボットとして右端の要素を選択します
        val pivot = list[end]

        //ピボットよりも小さい要素は`pIndex`の左側にプッシュされます
        //ピボットよりも多くの要素が`pIndex`の右側にプッシュされます
        //等しい要素はどちらの方向にも進むことができます
        var pIndex = start

        //ピボット以下の要素が見つかるたびに、
        // `pIndex`がインクリメントされ、その要素が配置されます
        //ピボットの前。
        for (i in start until end) {
            if (player.location.distance(list[i].location) <= player.location.distance(pivot.location)) {
                swap(list, i, pIndex)
                pIndex++
            }
        }

        //`pIndex`をピボットと交換します
        swap(list, end, pIndex)

        // `pIndex`(ピボット要素のインデックス)を返します
        return pIndex
    }

    fun quicksort(player: Player, list: MutableList<Entity>, start: Int, end: Int) {
        //基本条件
        if (start >= end) return

        //ピボット全体で要素を再配置します
        val pivot = partition(player, list, start, end)

        //ピボットよりも小さい要素を含むサブアレイで繰り返します
        quicksort(player, list, start, pivot - 1)

        //ピボット以外の要素を含むサブアレイで繰り返します
        quicksort(player, list, pivot + 1, end)
    }
}