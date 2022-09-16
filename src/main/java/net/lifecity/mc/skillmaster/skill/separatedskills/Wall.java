package net.lifecity.mc.skillmaster.skill.separatedskills;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.utils.WorldEditUtils;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Wall extends SeparatedSkill {

    public Wall(SkillUser user) {
        super(
                "ウォール",
                Arrays.asList(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.GREAT_SWORD, Weapon.LONG_SWORD, Weapon.RAPIER, Weapon.MACE),
                SkillType.ATTACK,
                Arrays.asList(
                        "ガラスの壁を生成し、相手の攻撃から身を守る",
                        "壁は時間経過で消える"
                ),
                0,
                10,
                20,
                user
        );
    }

    @Override
    public void activate() {
        super.activate();

        Player player = this.user.getPlayer();
        Location loc = player.getLocation();
        try {
            createWall(loc);
        } catch (IOException e) {
            player.sendMessage("壁の生成失敗");
        }
    }

    @Override
    public void additionalInput() {}

    @Override
    public void deactivate() {
        super.deactivate();
    }

    private void createWall(Location origin) throws IOException {
        float yaw = origin.getYaw();
        BlockFace face = yawToFace(yaw, true);
        WorldEditUtils worldEditUtils = new WorldEditUtils();


        switch(face) {
            case NORTH -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/north_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip, activationTime);
            }
            case NORTH_EAST -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/north_east_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
            case EAST -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/east_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
            case SOUTH_EAST -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/south_east_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
            case SOUTH -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/south_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
            case SOUTH_WEST -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/south_west_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
            case WEST -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/west_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
            case NORTH_WEST -> {
                File schematic = new File(SkillMaster.instance.getDataFolder() + File.separator + "/schematics/north_west_wall.schem");
                Clipboard clip = worldEditUtils.load(schematic);
                worldEditUtils.pasteAndAutoUndo(origin, clip,activationTime);
            }
        }
    }


    private BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections)
            return radial[Math.round(yaw / 45f) & 0x7].getOppositeFace();

        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    private static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
}