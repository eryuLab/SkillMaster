package net.lifecity.mc.skillmaster.skill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/**
 * スキルのおおまかな種類の列挙
 */
@AllArgsConstructor
public enum SkillType {
    ATTACK("攻撃", Material.STONE_SWORD),
    DEFENSE("防御", Material.SHIELD),
    MOVE("移動", Material.CHAINMAIL_BOOTS);

    @Getter
    private final String jp;
    @Getter
    private final Material material;
}
