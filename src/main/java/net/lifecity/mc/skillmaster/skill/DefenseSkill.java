package net.lifecity.mc.skillmaster.skill;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * 防御スキルのスーパークラス
 */
public abstract class DefenseSkill extends SeparatedSkill {

    public DefenseSkill(String name, Weapon weapon, List<String> lore, int point, int activationTime, int interval, SkillUser user) {
        super(name, weapon, SkillType.DEFENSE, lore, point, activationTime, interval, user);
    }

    @Override
    public void additionalInput() {}

    /**
     * 防御するときに呼び出されます
     * @param damage 攻撃力
     * @param vector ノックバックの強さ
     */
    public abstract void defense(double damage, Vector vector);
}
