package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.defenseskills.normaldefense.SSNormalDefense;
import net.lifecity.mc.skillmaster.skill.separatedskills.jumpattack.SSJumpAttack;
import net.lifecity.mc.skillmaster.skill.separatedskills.leafflow.SSLeafFlow;
import net.lifecity.mc.skillmaster.skill.skills.highjump.SSHighJump;
import net.lifecity.mc.skillmaster.skill.skills.kick.SSKick;
import net.lifecity.mc.skillmaster.skill.skills.movefast.SSMoveFast;
import net.lifecity.mc.skillmaster.skill.skills.vectorattack.SSVectorAttack;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * スキルを管理するクラス
 */
public class SkillManager {

    private final SkillUser user;

    @Getter
    private final List<Skill> all = new ArrayList<>();

    @Getter
    private final List<Skill> straightSword = new ArrayList<>();

    public SkillManager(SkillUser user) {
        this.user = user;

        // 直剣
        // 複合スキル
        straightSword.add(new SSLeafFlow(user ,1));
        straightSword.add(new SSJumpAttack(user, 2));
        // 単発スキル
        straightSword.add(new SSMoveFast(user, 3));
        straightSword.add(new SSVectorAttack(user, 4));
        straightSword.add(new SSHighJump(user, 5));
        straightSword.add(new SSKick(user, 6));
        // 防御スキル
        straightSword.add(new SSNormalDefense(user, 7));

        all.addAll(straightSword);
    }

    /**
     * 武器からスキルリストを取得します
     * @param weapon この武器のスキルリストを取得します
     * @return 武器のスキルリスト
     */
    public List<Skill> fromWeapon(Weapon weapon) {
        if (weapon == Weapon.STRAIGHT_SWORD)
            return straightSword;
        else
            return new ArrayList<>();
    }

    /**
     * クラスからスキルインスタンスを取得します
     * @param skillClass スキルクラス
     * @return スキルインスタンス
     */
    public Skill fromClass(Class<? extends Skill> skillClass) {
        for (Skill skill : all) {
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

        int num = (int) (itemStack.getItemMeta().getCustomModelData() * 0.01);
        Weapon weapon = Weapon.fromNumber(num);

        List<Skill> list = fromWeapon(weapon);

        for (Skill skill : list) {
            if (skill.is(itemStack))
                return skill;
        }
        return null;
    }
}
