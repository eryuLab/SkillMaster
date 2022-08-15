package net.lifecity.mc.skillmaster.user;

import lombok.Getter;

public class SkillKey {

    // 右クリックの入力
    public static final SkillKey RIGHT_ONE = new SkillKey(SkillButton.RIGHT, 0, 10);
    public static final SkillKey RIGHT_TWO = new SkillKey(SkillButton.RIGHT, 1, 19);
    public static final SkillKey RIGHT_THREE = new SkillKey(SkillButton.RIGHT, 2, 28);

    // スワップキーの入力
    public static final SkillKey SWAP_ONE = new SkillKey(SkillButton.SWAP, 0, 11);
    public static final SkillKey SWAP_TWO = new SkillKey(SkillButton.SWAP, 1, 20);
    public static final SkillKey SWAP_THREE = new SkillKey(SkillButton.SWAP, 2, 29);

    // ドロップキーの入力
    public static final SkillKey DROP_ONE = new SkillKey(SkillButton.DROP, 0, 12);
    public static final SkillKey DROP_TWO = new SkillKey(SkillButton.DROP, 1, 21);
    public static final SkillKey DROP_THREE = new SkillKey(SkillButton.DROP, 2, 30);

    private final SkillButton button;
    private final int num;
    @Getter
    private final int invSlot;

    private SkillKey(SkillButton button, int num, int invSlot) {
        this.button = button;
        this.num = num;
        this.invSlot = invSlot;
    }

    private enum SkillButton {
        RIGHT,
        SWAP,
        DROP
    }
}
