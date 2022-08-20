package net.lifecity.mc.skillmaster.skill.skills.movefast;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * 上以外の方向に素早く移動するスキル
 */
public class SSMoveFast extends Skill {

    public SSMoveFast(SkillUser user) {
        super("高速移動", Weapon.STRAIGHT_SWORD, SkillType.MOVE, Arrays.asList("向いている方向に高速移動します。", "上方向には飛べません。"), 2, 0, 25, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(1.55);

        // Yの値が+だったら
        if (vector.getY() > 0)
            vector.setY(0.15);

        user.getPlayer().setVelocity(vector);
    }
}
