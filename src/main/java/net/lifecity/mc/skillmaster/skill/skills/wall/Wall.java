package net.lifecity.mc.skillmaster.skill.skills.wall;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Wall extends Skill {
    protected Wall(Weapon weapon,
                   int num,
                   int point,
                   int interval,
                   SkillUser user) {
        super(
                "ウォール",
                weapon,
                SkillType.ATTACK,
                Arrays.asList(
                        "発動するとガラスの球体を生成し、",
                        "相手の攻撃から身を守る。",
                        "壁は時間経過や攻撃で消える"),
                num,
                point,
                interval,
                user
        );
    }

    @Override
    public void activate() {
        super.activate();

        Player player = this.user.getPlayer();
        Location loc = player.getLocation().add(0,1,0);
        createWall(loc,3,5, Material.GLASS);

    }

    @Override
    public void deactivate() {
        super.deactivate();

        Player player = this.user.getPlayer();
        Location loc = player.getLocation().add(0,1,0);
        createWall(loc,3,5, Material.AIR);
    }

    private void createWall(Location origin, int radius, int size, Material material) {
        Location loc;
        loc = origin.clone().add(radius,0, 0);
        //x正
        for(int z=-size; z <= size; z++) {
            for(int y=0; y <= size; y++) {
                Location l = loc.add(0,y,z);
                l.getBlock().setType(material);
            }
        }

        loc = origin.clone().add(-radius,0, 0);
        //x負
        for(int z=-size; z <= size; z++) {
            for(int y=0; y <= size; y++) {
                Location l = loc.add(0,y,z);
                l.getBlock().setType(material);
            }
        }

        loc = origin.clone().add(0,0, radius);
        //z正
        for(int x=-size; x <= size; x++) {
            for(int y=0; y <= size; y++) {
                Location l = loc.add(x,y,0);
                l.getBlock().setType(material);
            }
        }

        loc = origin.clone().add(0,0,-radius);
        //z負
        for(int x=-size; x <= size; x++) {
            for(int y=0; y <= size; y++) {
                Location l = loc.add(x,y,0);
                l.getBlock().setType(material);
            }
        }

        loc = origin.clone().add(0,radius,0);
        //y正
        for(int x=-size; x <= size; x++) {
            for(int z=-size; z <= size; z++) {
                Location l = loc.add(x,0,z);
                l.getBlock().setType(material);
            }
        }
    }
}
