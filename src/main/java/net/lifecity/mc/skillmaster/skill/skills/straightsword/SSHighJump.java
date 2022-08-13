package net.lifecity.mc.skillmaster.skill.skills.straightsword;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

public class SSHighJump extends Skill {

    public SSHighJump(SkillUser user) {
        super("大ジャンプ", Weapon.STRAIGHT_SWORD, SkillType.MOVE, 0, 30, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection();

        // x方向を制限
        vector.setX(vector.getX() * 0.65);
        // z方向を制限
        vector.setZ(vector.getZ() * 0.65);
        // y方向を拡大
        vector.setY(1);

        user.getPlayer().setVelocity(vector);
    }
}
