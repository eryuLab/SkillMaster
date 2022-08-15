package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import org.bukkit.Material;

public class SkillKey {

    // ドロップキーの入力
    public static final SkillKey DROP_ONE = new SkillKey(SkillButton.DROP, 0, 10);
    public static final SkillKey DROP_TWO = new SkillKey(SkillButton.DROP, 1, 19);
    public static final SkillKey DROP_THREE = new SkillKey(SkillButton.DROP, 2, 28);

    // スワップキーの入力
    public static final SkillKey SWAP_ONE = new SkillKey(SkillButton.SWAP, 0, 12);
    public static final SkillKey SWAP_TWO = new SkillKey(SkillButton.SWAP, 1, 21);
    public static final SkillKey SWAP_THREE = new SkillKey(SkillButton.SWAP, 2, 30);

    // 右クリックの入力
    public static final SkillKey RIGHT_ONE = new SkillKey(SkillButton.RIGHT, 0, 14);
    public static final SkillKey RIGHT_TWO = new SkillKey(SkillButton.RIGHT, 1, 23);
    public static final SkillKey RIGHT_THREE = new SkillKey(SkillButton.RIGHT, 2, 32);

    @Getter
    private final SkillButton button;
    @Getter
    private final int num;
    @Getter
    private final int invSlot;

    private SkillKey(SkillButton button, int num, int invSlot) {
        this.button = button;
        this.num = num;
        this.invSlot = invSlot;
    }

    public enum SkillButton {
        RIGHT(Material.YELLOW_STAINED_GLASS_PANE, "右クリック"),
        SWAP(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "スワップ"),
        DROP(Material.ORANGE_STAINED_GLASS_PANE, "ドロップ");

        @Getter
        private Material material;

        @Getter
        private String jp;

        SkillButton(Material material, String jp) {
            this.material = material;
            this.jp = jp;
        }
    }
}
