package net.lifecity.mc.skillmaster.skill.separatedskills.leafflow;

import com.destroystokyo.paper.ParticleBuilder;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;

/**
 * 前方に突進しながら敵を攻撃するスキル
 */
public class SSLeafFlow extends LeafFlow {

    public SSLeafFlow(SkillUser user, int num) {
        super(
                Weapon.STRAIGHT_SWORD,
                num,
                0,
                8,
                15,
                user,
                1,
                0.15,
                1.8,
                3,
                1,
                0.15
        );
    }
}
