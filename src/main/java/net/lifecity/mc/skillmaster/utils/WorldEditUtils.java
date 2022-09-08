package net.lifecity.mc.skillmaster.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.lifecity.mc.skillmaster.SkillMaster;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEditUtils {

    public Clipboard load(File file) throws IOException {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            return reader.read();
        }
    }

    public boolean pasteAndAutoUndo(Location pasteLoc, Clipboard clipboard, int seconds) {
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(pasteLoc.getWorld()), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(pasteLoc.getX(), pasteLoc.getY(), pasteLoc.getZ()))
                    .ignoreAirBlocks(true)
                    .build();

            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    if(count > seconds) {
                        editSession.undo(editSession);
                        this.cancel();
                    }

                    count++;
                }

            }.runTaskTimer(SkillMaster.instance, 0, 20L);

            try {
                Operations.complete(operation);
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }
}
