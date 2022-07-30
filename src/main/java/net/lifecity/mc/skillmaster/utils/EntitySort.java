package net.lifecity.mc.skillmaster.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

/**
 * Entityのリストをプレイヤーから近い順に並べ替えるクラス
 */
public class EntitySort {

    private static void swap(List<Entity> list, int i, int j) {
        Entity entity = list.get(i);
        list.set(i, list.get(j));
        list.set(j, entity);
    }

    private static int partition(Player player, List<Entity> list, int start, int end) {
        //アレイからピボットとして右端の要素を選択します
        Entity pivot = list.get(end);

        //ピボットよりも小さい要素は`pIndex`の左側にプッシュされます
        //ピボットよりも多くの要素が`pIndex`の右側にプッシュされます
        //等しい要素はどちらの方向にも進むことができます
        int pIndex = start;

        //ピボット以下の要素が見つかるたびに、
        // `pIndex`がインクリメントされ、その要素が配置されます
        //ピボットの前。
        for (int i = start; i < end; i++) {
            if (player.getLocation().distance(list.get(i).getLocation()) <= player.getLocation().distance(pivot.getLocation())) {
                swap(list, i, pIndex);
                pIndex++;
            }
        }

        //`pIndex`をピボットと交換します
        swap(list, end, pIndex);

        // `pIndex`(ピボット要素のインデックス)を返します
        return pIndex;
    }

    public static void quicksort(Player player, List<Entity> list, int start, int end) {
        //基本条件
        if (start >= end)
            return;

        //ピボット全体で要素を再配置します
        int pivot = partition(player, list, start, end);

        //ピボットよりも小さい要素を含むサブアレイで繰り返します
        quicksort(player, list, start, pivot - 1);

        //ピボット以外の要素を含むサブアレイで繰り返します
        quicksort(player, list, pivot + 1, end);
    }
}
