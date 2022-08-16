package net.lifecity.mc.skillmaster.skill.skills.vectorattack;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * 自分のベクトルの強さを攻撃力に変換して攻撃するスキル
 */
public class SSVectorAttack extends Skill {

    public SSVectorAttack(SkillUser user) {
        super("ベクトルアタック", Weapon.STRAIGHT_SWORD, SkillType.ATTACK, Arrays.asList("ユーザーが持つベクトルを力に変換して攻撃します。"), 0, 0, 40, user);
    }

    @Override
    public void activate() {
        super.activate();

        Vector vector = user.getPlayer().getEyeLocation().getDirection().multiply(1.2);

        user.getPlayer().setVelocity(vector);

        double damage = user.getPlayer().getVelocity().length();
        damage *= 2;

        boolean b = user.attackNearest(
                1.8,
                damage,
                user.getPlayer().getVelocity().multiply(0.25).setY(0.15),
                Sound.ENTITY_PLAYER_ATTACK_CRIT
        );

        if (b)
            user.sendMessage("damage: " + damage);
    }
}
