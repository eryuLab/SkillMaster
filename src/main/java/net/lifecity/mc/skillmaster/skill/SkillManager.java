package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.defenseskills.NormalDefense;
import net.lifecity.mc.skillmaster.skill.separatedskills.JumpAttackSlash;
import net.lifecity.mc.skillmaster.skill.separatedskills.LeafFlow;
import net.lifecity.mc.skillmaster.skill.separatedskills.Wall;
import net.lifecity.mc.skillmaster.skill.skills.HighJump;
import net.lifecity.mc.skillmaster.skill.skills.Kick;
import net.lifecity.mc.skillmaster.skill.skills.MoveFast;
import net.lifecity.mc.skillmaster.skill.skills.VectorAttack;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * スキルを管理するクラス
 */
public class SkillManager {

    private final SkillUser user;

    @Getter
    private final List<Skill> skillList = new ArrayList<>();

    public SkillManager(SkillUser user) {
        this.user = user;

        // 複合スキル
        skillList.add(new LeafFlow(user));
        skillList.add(new JumpAttackSlash(user));
        skillList.add(new Wall(user));
        // 単発スキル
        skillList.add(new MoveFast(user));
        skillList.add(new VectorAttack(user));
        skillList.add(new HighJump(user));
        skillList.add(new Kick(user));
        // 防御スキル
        skillList.add(new NormalDefense(user));

        for (int i = 0 ;i < skillList.size(); i++) {
            skillList.get(i).setId(i);
        }
    }

    /**
     * 武器からスキルリストを取得します
     * @param weapon この武器のスキルリストを取得します
     * @return 武器のスキルリスト
     */
    public List<Skill> fromWeapon(Weapon weapon) {
        List<Skill> list = new ArrayList<>();

        for (Skill skill : skillList) {
            if (skill.getWeaponList().contains(weapon))
                list.add(skill);
        }
        return list;
    }

    /**
     * クラスからスキルインスタンスを取得します
     * @param skillClass スキルクラス
     * @return スキルインスタンス
     */
    public Skill fromClass(Class<? extends Skill> skillClass) {
        for (Skill skill : skillList) {
            if (skillClass.isInstance(skill))
                return skill;
        }
        return null;
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    public Skill fromItemStack(ItemStack itemStack) {
        if (!itemStack.hasItemMeta())
            return null;
        if (!itemStack.getItemMeta().hasCustomModelData())
            return null;

        for (Skill skill : skillList) {
            if (skill.is(itemStack))
                return skill;
        }
        return null;
    }
}
