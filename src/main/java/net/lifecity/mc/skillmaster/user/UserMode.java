package net.lifecity.mc.skillmaster.user;

/**
 * SkillUserのモードの列挙
 */
public enum UserMode {
    BATTLE("バトル"),
    TRAINING("トレーニング"),
    UNARMED("武装解除");

    private String jp;

    UserMode(String jp) {
        this.jp = jp;
    }
}
