package net.lifecity.mc.skillmaster.skill.skills.straightsword;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

public class SSMoveFast extends Skill {

    public SSMoveFast(SkillUser user) {
        super("高速移動", Weapon.STRAIGHT_SWORD, 0, 25, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(1.55);

        user.getPlayer().setVelocity(vector);
    }
}
