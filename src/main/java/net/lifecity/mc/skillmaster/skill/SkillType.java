package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import org.bukkit.Material;

/**
 * スキルのおおまかな種類の列挙
 */
public enum SkillType {
    ATTACK("攻撃", Material.STONE_SWORD),
    DEFENSE("防御", Material.SHIELD),
    MOVE("移動", Material.CHAINMAIL_BOOTS);

    @Getter
    private String jp;
    @Getter
    private Material material;

    SkillType(String jp, Material material) {
        this.jp = jp;
        this.material = material;
    }
}
