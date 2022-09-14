package net.lifecity.mc.skillmaster.user.skillset;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * スキルに使うボタンの列挙
 */
@AllArgsConstructor
public enum SkillButton {
    RIGHT("右クリック"),
    SWAP("スワップ"),
    DROP("ドロップ");

    @Getter
    private final String jp;
}
