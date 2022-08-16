package net.lifecity.mc.skillmaster.user.skillset;

import lombok.Getter;

/**
 * スキルに使うボタンの列挙
 */
public enum SkillButton {
    RIGHT("右クリック"),
    SWAP("スワップ"),
    DROP("ドロップ");

    @Getter
    private String jp;

    SkillButton(String jp) {
        this.jp = jp;
    }
}
