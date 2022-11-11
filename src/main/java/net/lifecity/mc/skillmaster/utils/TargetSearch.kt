package net.lifecity.mc.skillmaster.utils

import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.acos

class TargetSearch {

    fun getTargetPlayer(entity: Entity, range: Double) =
        getTargetEntity(entity, entity.world.getNearbyPlayers(entity.location, range, range, range))

    fun getTargetLivingEntity(entity: Entity, range: Double) =
        getTargetEntity(entity, entity.world.getNearbyLivingEntities(entity.location, range, range, range))

    fun getTargetEntity(entity: Entity, range: Double) =
        getTargetEntity(
            entity = entity,
            entities = entity.world.getNearbyEntities(entity.location, range, range, range)
        )

    fun getBehindPlayer(entity: Entity, range: Double) =
        getBehindEntity(entity, entity.world.getNearbyPlayers(entity.location, range, range, range))

    fun getBehindLivingEntity(entity: Entity, range: Double) =
        getBehindEntity(entity, entity.world.getNearbyLivingEntities(entity.location, range, range, range))

    fun getBehindEntity(entity: Entity, range: Double) =
        getBehindEntity(
            entity = entity,
            entities = entity.world.getNearbyEntities(entity.location, range, range, range)
        )


    fun getPlayerPositionRelation(playerA: Player, playerB: Player) =
        getEntityPositionalRelation(playerA, playerB)

    fun getLivingEntityPositionRelation(livingentityA: LivingEntity, livingentityB: LivingEntity) =
        getEntityPositionalRelation(livingentityA, livingentityB)

    /**
     * @param entity: 始点となるエンティティ
     * @param entities: 探索対象のEntityのIterableリスト
     * @return: 探索によって得られたEntity
     */
    private fun <T : Entity> getTargetEntity(entity: Entity, entities: Iterable<T>): T? {
        val threshold = 1.0
        var target: T? = null

        for (other in entities) { //iterableリストの中を走査
            // スペクテイターか確認
            if (other is Player && other.gameMode == GameMode.SPECTATOR)
                continue

            //自分のベクトルと、対象のベクトルの差を計算 => 相手が向いている方向と自分が向いている方向が逆ならば最大, 同じならば最小
            val vec = other.location.toVector().subtract(entity.location.toVector())

            // 自分のベクトルと、vecの外積の長さが1未満である => 自分のベクトルとvecがなす平行四辺形の面積が1未満、
            // つまりここでもどの程度同じ方向を向いているか判定している。
            // かつvecと自分のベクトルの内積が0以上 => 自分のベクトルがvecの向いている方向と同じ方向
            // であるならば
            if (entity.location.direction.normalize().crossProduct(vec).lengthSquared() < threshold
                && vec.normalize().dot(entity.location.direction.normalize()) >= 0
            ) {
                if (target == null || target.location.distanceSquared(entity.location) > other.location.distanceSquared(
                        entity.location
                    )
                ) {
                    target = other
                }
            }
        }
        return target
    }

    /**
     * @param entity: 始点となるエンティティ
     * @param entities: 探索対象のEntityのIterableリスト
     * @return: 探索によって得られた背後にいるEntity
     */
    private fun <T : Entity> getBehindEntity(entity: Entity, entities: Iterable<T>): T? {
        var target: T? = null

        for (other in entities) { //iterableリストの中を走査
            // スペクテイターか確認
            if (other is Player && other.gameMode == GameMode.SPECTATOR)
                continue

            //自分のベクトルと、対象のベクトルの差を計算 => 相手が向いている方向と自分が向いている方向が逆ならば最大, 同じならば最小
            val vec = entity.location.toVector().subtract(other.location.toVector()).multiply(-1)

            val minusDirection = entity.location.direction.normalize().multiply(-1)
            // 自分のベクトルと、vecの外積の長さが1未満である => 自分のベクトルとvecがなす平行四辺形の面積が1未満、
            // つまりここでもどの程度同じ方向を向いているか判定している。
            // かつvecと自分のベクトルの内積が0以上 => 自分のベクトルがvecの向いている方向と同じ方向
            // であるならば
            if (vec.normalize().dot(minusDirection) >= 0
            ) {
                if (target == null || target.location.distanceSquared(entity.location) > other.location.distanceSquared(
                        entity.location
                    )
                ) {
                    target = other
                }
            }
        }
        return target
    }


    /**
     * ２体のエンティティの位置からΘとΦ(ラジアン)を求めるメソッド
     * @return: Pair(Θ, Φ)
     */
    fun getEntityPositionalRelation(entityA: Entity, entityB: Entity): Pair<Double, Double> {
        val vec = entityB.location.toVector().subtract(entityA.location.toVector())

        val unitY = Vector(0, 1, 0)
        val unitZ = Vector(0, 0, 1)

        val cosTheta = unitY.dot(vec) / vec.length()
        val cosPhi = unitZ.dot(vec.setY(0)) / vec.setY(0).length()

        val theta = acos(cosTheta) * (PI / 180)
        val phi = acos(cosPhi) * (PI / 180)

        return Pair(theta, phi)
    }
}