package net.lifecity.mc.skillmaster.skill.skills.vectorattack;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * 自分のベクトルの強さを攻撃力に変換して攻撃するスキル
 */
public class SSVectorAttack extends VectorAttack {

    public SSVectorAttack(SkillUser user) {
        super(
                Weapon.STRAIGHT_SWORD,
                0,
                40,
                user,
                1.2,
                2,
                1.8,
                0.25,
                0.15,
                5
        );
    }
}
