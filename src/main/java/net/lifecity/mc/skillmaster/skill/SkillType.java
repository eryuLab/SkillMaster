package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;

/**
 * スキルのおおまかな種類の列挙
 */
public enum SkillType {
    ATTACK("攻撃"),
    DEFENSE("防御"),
    MOVE("移動");

    @Getter
    private String jp;

    SkillType(String jp) {
        this.jp = jp;
    }
}
