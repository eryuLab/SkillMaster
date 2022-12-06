package net.lifecity.mc.skillmaster.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Entityのリストをプレイヤーから近い順に並べ替えるクラス
 */
object NearTargets {
    @JvmStatic
    fun search(location: Location, distance: Double): LivingEntity? {
        val entities = location.getNearbyEntities(distance, distance, distance)

        val livingEntities = mutableListOf<LivingEntity>()
        for (entity in entities) {
            if (entity is LivingEntity)
                livingEntities.add(entity)
        }

        if (livingEntities.isEmpty())
            return null

        var nearestEntity = livingEntities[0]
        var nearestDistance = nearestEntity.location.distance(location)
        for (entity in livingEntities) {
            val entityDistance = entity.location.distance(location)
            if (nearestDistance > entityDistance)
                nearestEntity = entity
        }

        return nearestEntity
    }
}