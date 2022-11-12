package net.lifecity.mc.skillmaster.rankboard

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.ChatColor
import org.bukkit.Location

class RankBoard {

    private fun setRankBoardTexts(hologram: Hologram, text: List<String>) {
        hologram.appendTextLine("${ChatColor.YELLOW}>>>>> Rank Board <<<<<")
        for ((index,t) in text.withIndex()) {
            hologram.appendTextLine("${ChatColor.YELLOW}${index + 1}. ${ChatColor.GREEN}$t")
        }
    }

    fun create(location: Location, text: List<String>) : Hologram {
        val hologram = HologramsAPI.createHologram(SkillMaster.INSTANCE, location.add(0.0, 2.0, 0.0))
        setRankBoardTexts(hologram, text)

        return hologram
    }

    fun update(hologram: Hologram, text: List<String>) : Hologram {
        hologram.clearLines()
        setRankBoardTexts(hologram, text)

        return hologram
    }

    fun delete(hologram: Hologram) {
        hologram.delete()
    }
}