package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.separatedskills.jumpattack.SSJumpAttack;
import net.lifecity.mc.skillmaster.skill.separatedskills.leafflow.SSLeafFlow;
import net.lifecity.mc.skillmaster.skill.skills.movefast.SSMoveFast;
import net.lifecity.mc.skillmaster.skill.skills.vectorattack.SSVectorAttack;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * スキルを管理するクラス
 */
public class SkillManager {

    @Getter
    private final List<Skill> allSkill = new ArrayList<>();

    @Getter
    private final List<Skill> straightSword = new ArrayList<>();

    public SkillManager(SkillUser user) {
        straightSword.add(new SSMoveFast(user));
        straightSword.add(new SSVectorAttack(user));
        straightSword.add(new SSLeafFlow(user));
        straightSword.add(new SSJumpAttack(user));

        allSkill.addAll(straightSword);
    }

    public List<Skill> fromWeapon(Weapon weapon) {
        return switch (weapon) {
            case STRAIGHT_SWORD -> straightSword;
            default -> null;
        };
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    public Skill fromItemStack(ItemStack itemStack) {
        for (Skill skill : allSkill) {
            if (skill.is(itemStack))
                return skill;
        }
        return null;
    }
}
