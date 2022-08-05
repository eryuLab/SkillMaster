package net.lifecity.mc.skillmaster.user;

import net.lifecity.mc.skillmaster.weapon.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillSetList extends ArrayList<SkillSet> {

    public SkillSetList(SkillUser user) {
        for (Weapon weapon : Weapon.values())
            add(new SkillSet(user, weapon));
    }

    /**
     *
     * @param weapon
     * @return
     */
    public SkillSet get(Weapon weapon) {
        for (SkillSet set : this) {
            if (set.getWeapon() == weapon)
                return set;
        }
        return null;
    }
}
