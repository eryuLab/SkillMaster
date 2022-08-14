package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;

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
