package net.lifecity.mc.skillmaster.skill.compositeskills

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.WorldEditUtils
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockFace.*
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt

class Wall(user: SkillUser) : CompositeSkill(
    "ウォール",
    listOf(
        Weapon.STRAIGHT_SWORD,
        Weapon.DAGGER,
        Weapon.GREAT_SWORD,
        Weapon.LONG_SWORD,
        Weapon.RAPIER,
        Weapon.MACE
    ),
    SkillType.ATTACK,
    listOf(
        "ガラスの壁を生成し、相手の攻撃から身を守る",
        "壁は時間経過で消える"
    ),
    10,
    240,
    user
) {
    override fun onActivate() {
        try {
            createWall(user.player.location)
        } catch (e: IOException) {
            user.player.sendMessage("壁の生成失敗")
        }
    }


    @Throws(IOException::class)
    private fun createWall(origin: Location) {
        val yaw = origin.yaw
        val face = yawToFace(yaw)
        val worldEditUtils = WorldEditUtils()
        when (face) {
            NORTH -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/north_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            NORTH_EAST -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/north_east_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            EAST -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/east_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            SOUTH_EAST -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/south_east_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            SOUTH -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/south_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            SOUTH_WEST -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/south_west_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            WEST -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/west_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            NORTH_WEST -> {
                val schematic =
                    File(SkillMaster.INSTANCE.dataFolder.toString() + File.separator + "/schematics/north_west_wall.schem")
                val clip = worldEditUtils.load(schematic)
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime)
            }
            else -> {}
        }
    }

    private fun yawToFace(yaw: Float): BlockFace {
        return radial[(yaw / 45f).roundToInt() and 0x7].oppositeFace
    }

    companion object {
        private val radial = arrayOf(
            NORTH,
            NORTH_EAST,
            EAST,
            SOUTH_EAST,
            SOUTH,
            SOUTH_WEST,
            WEST,
            NORTH_WEST
        )
    }
}