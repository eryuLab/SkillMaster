package net.lifecity.mc.skillmaster.utils

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.WorldEditException
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class WorldEditUtils {
    @Throws(IOException::class)
    fun load(file: File?): Clipboard {
        val format = ClipboardFormats.findByFile(file)
        format!!.getReader(FileInputStream(file)).use { reader -> return reader.read() }
    }

    fun pasteAndAutoUndo(pasteLoc: Location, clipboard: Clipboard?, seconds: Int): Boolean {
        WorldEdit.getInstance().editSessionFactory.getEditSession(BukkitWorld(pasteLoc.world), -1).use { editSession ->
            val operation = ClipboardHolder(clipboard)
                .createPaste(editSession)
                .to(BlockVector3.at(pasteLoc.x, pasteLoc.y, pasteLoc.z))
                .ignoreAirBlocks(true)
                .build()
            object : BukkitRunnable() {
                var count = 0
                override fun run() {
                    if (count > seconds) {
                        editSession.undo(editSession)
                        cancel()
                    }
                    count++
                }
            }.runTaskTimer(SkillMaster.instance, 0, 20L)
            try {
                Operations.complete(operation)
            } catch (e: WorldEditException) {
                throw RuntimeException(e)
            }
            return true
        }
    }
}