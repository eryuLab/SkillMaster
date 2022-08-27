package net.lifecity.mc.skillmaster.skill;

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

import java.util.ArrayList;
import java.util.List;

/**
 * スキルを管理するクラス
 */
public class SkillManager {

    private final SkillUser user;

    public SkillManager(SkillUser user) {
        this.user = user;
    }

    /**
     * すべてのスキルのリストを取得します
     * @return
     */
    public List<Skill> all() {
        List<Skill> list = new ArrayList<>();

        list.addAll(straightSword());

        return list;
    }

    /**
     * 直剣のスキルリストを取得します
     * @return 直剣のスキル一覧
     */
    public List<Skill> straightSword() {
        List<Skill> list = new ArrayList<>();

        list.add(new SSMoveFast(user));
        list.add(new SSVectorAttack(user));
        list.add(new SSHighJump(user));
        list.add(new SSLeafFlow(user));
        list.add(new SSJumpAttack(user));
        list.add(new SSNormalDefense(user));
        list.add(new SSKick(user));

        return list;
    }

    public List<Skill> fromWeapon(Weapon weapon) {
        if (weapon == Weapon.STRAIGHT_SWORD)
            return straightSword();
        else
            return new ArrayList<>();
    }

    /**
     * ItemStackからSkillを特定します
     * @param itemStack 特定の対象となるItemStack
     * @return 一致したSkill
     */
    public Skill fromItemStack(ItemStack itemStack) {
        Weapon weapon = Weapon.fromItemStack(itemStack);

        List<Skill> list = switch (weapon) {
            case STRAIGHT_SWORD -> straightSword();
            default -> null;
        };
        for (Skill skill : list) {
            if (skill.is(itemStack))
                return skill;
        }
        return null;
    }
}
