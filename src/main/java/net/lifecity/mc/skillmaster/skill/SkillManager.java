package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.separatedskills.straightsword.SSJumpAttack;
import net.lifecity.mc.skillmaster.skill.separatedskills.straightsword.SSLeafFlow;
import net.lifecity.mc.skillmaster.skill.skills.straightsword.SSMoveFast;
import net.lifecity.mc.skillmaster.skill.skills.straightsword.SSVectorAttack;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * スキルを管理するクラス
 */
public class SkillManager {

    @Getter
    private final Set<Skill> allSkill = new HashSet<>();

    @Getter
    private final Set<Skill> straightSword = new HashSet<>();

    public SkillManager(SkillUser user) {
        straightSword.add(new SSMoveFast(user));
        straightSword.add(new SSVectorAttack(user));
        straightSword.add(new SSLeafFlow(user));
        straightSword.add(new SSJumpAttack(user));

        allSkill.addAll(straightSword);
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
