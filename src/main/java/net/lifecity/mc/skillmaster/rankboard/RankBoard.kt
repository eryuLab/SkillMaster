package net.lifecity.mc.skillmaster.rankboard

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.Location

class RankBoard {

    fun spawn(location: Location, text: List<String>) {
        val hologram = HologramsAPI.createHologram(SkillMaster.INSTANCE, location.add(0.0, 2.0, 0.0))
        hologram.appendTextLine(">>>>> Rank Board <<<<<")
        for ((index,t) in text.withIndex()) {
            hologram.appendTextLine("${index + 1}. $t")
        }
    }
}