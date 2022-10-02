package net.lifecity.mc.skillmaster.utils

import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.sk89q.worldedit.EditSession
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
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class WorldEditUtils {
    @Throws(IOException::class)
    fun load(file: File): Clipboard {
        val format = ClipboardFormats.findByFile(file)
        val reader = format!!.getReader(FileInputStream(file))
        try {
            return reader.read()
        } finally {
            reader.close()
        }
    }

    fun pasteAndAutoUndo(pasteLoc: Location, clipboard: Clipboard?, seconds: Int) {
        val editSession : EditSession? = WorldEdit.getInstance().editSessionFactory.getEditSession(BukkitWorld(pasteLoc.world), -1)
        try {
            val operation = ClipboardHolder(clipboard)
                .createPaste(editSession)
                .to(BlockVector3.at(pasteLoc.x, pasteLoc.y, pasteLoc.z))
                .ignoreAirBlocks(true)
                .build()

            var count = 0
            SkillMaster.runTaskTimer(20L) {
                if (count > seconds) {
                    editSession?.undo(editSession)
                    editSession?.close()
                    cancel()
                }
                count++
            }

            try {
                Operations.complete(operation)
            } catch (e: WorldEditException) {
                e.printStackTrace()
            }
        } finally {
            editSession?.close()
        }
    }
}
