package net.lifecity.mc.skillmaster.user;

import lombok.Getter;

/**
 * SkillUserのモードの列挙
 */
public enum UserMode {
    BATTLE("バトル"),
    TRAINING("トレーニング"),
    UNARMED("武装解除");

    @Getter
    private String jp;

    UserMode(String jp) {
        this.jp = jp;
    }
}
