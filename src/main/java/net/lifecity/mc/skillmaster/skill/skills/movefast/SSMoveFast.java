package net.lifecity.mc.skillmaster.skill.skills.movefast;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * 上以外の方向に素早く移動するスキル
 */
public class SSMoveFast extends MoveFast {

    public SSMoveFast(SkillUser user, int num) {
        super(
                Weapon.STRAIGHT_SWORD,
                num,
                0,
                25,
                user,
                1.55,
                0.15
        );
    }
}
