package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import net.lifecity.mc.skillmaster.user.skillset.SkillKey;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * SkillMasterのユーザーのインベントリを構築、操作するためのクラス
 */
public class UserInventory extends InventoryFrame {

    public UserInventory(SkillUser user) {
        super(user);
    }

    @Override
    public void init() {
        // アイテムを設置
        // 右クリック
        for (SkillKey key : user.getRightSkillSet()) {
            setSkillItem(key);
        }
        // スワップキー
        for (SkillKey key : user.getSwapSkillSet()) {
            setSkillItem(key);
        }
        // ドロップキー
        for (SkillKey key : user.getDropSkillSet()) {
            setSkillItem(key);
        }

    }

    /**
     * スキルと説明アイテムをセットで追加します
     * @param key 追加するSkillKey
     */
    private void setSkillItem(SkillKey key) {
        // 配置するスロットを計算
        int slot = switch (key.getButton()) {
            case DROP -> 10;
            case SWAP -> 13;
            case RIGHT -> 16;
        };
        slot = slot + 9 * key.getNum();

        // Paneを設置
        setItem(slot - 1, paneItem(key, true));
        setItem(slot + 1, paneItem(key, false));
        // SkillItemを設置
        setItem(slot, skillItem(key));
    }

    /**
     * スキル配置を説明するためのアイテムを生成します
     * @param key SkillKeyで生成するアイテムを選択します
     * @return 生成されたスキル配置を説明するアイテム
     */
    private InvItem paneItem(SkillKey key, boolean isLeft) {
        Material material = switch (key.getButton()) {
            case RIGHT -> Material.YELLOW_STAINED_GLASS_PANE;
            case SWAP -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case DROP -> Material.PINK_STAINED_GLASS_PANE;
        };

        String name;
        if (isLeft)
            name = key.getButton().getJp() + ": " + key.getNum() + "→";
        else
            name = "←" + key.getButton().getJp() + ": " + key.getNum();

        return new InvItem(
                createItemStack(
                        material,
                        name,
                        List.of()
                ),
                event -> event.setCancelled(true)
        );
    }

    /**
     * SkillKeyをInvItemに変換します
     * @param key 変換するSkillKey
     * @return 変換されたSkillKey
     */
    private InvItem skillItem(SkillKey key) {

        // スキルアイテムがない
        if (key.getSkill() == null) {
            return new InvItem(
                    createItemStack(Material.IRON_BARS, "スキル未登録", List.of()),
                    event -> {
                        // スキルアイテムを置くとスキル変更

                        // インベントリ確認
                        if (event.getView().getTopInventory().getType() == InventoryType.CRAFTING) {
                            event.setCancelled(true);
                            return;
                        }

                        // インベントリ特定
                        if (event.getView().getTopInventory().getType() == InventoryType.CHEST) {

                            InventoryFrame openedInv = user.getOpenedInventory();

                            if (openedInv == null) {
                                event.setCancelled(true);
                                return;
                            }

                            if (event.getView().getTopInventory() == openedInv.inv) {

                                // カーソルがスキルであればスキルを登録
                                Skill cursorSkill = new SkillManager(user).fromItemStack(event.getCursor());

                                // カーソルがスキルであるか
                                if (cursorSkill == null) {
                                    event.setCancelled(true);
                                    return;
                                }

                                // セットできるか確認
                                if (!user.settable(cursorSkill)) {
                                    event.setCancelled(true);
                                    user.sendMessage("このスキルはすでに登録されています");
                                    return;
                                }

                                // スキルセット
                                key.setSkill(cursorSkill);
                                user.sendMessage("スキルを登録しました: " + key.getButton().getJp() + "[" + key.getNum() + "]: " + cursorSkill.getName());

                                setItem(event.getSlot(), skillItem(key));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        user.getUserInventory().inv.getItem(event.getSlot()).setAmount(1);
                                    }
                                }.runTaskLater(SkillMaster.instance, 1);
                            }
                        }
                    }

            );
        }
        // スキルアイテムがある
        else {
            return new InvItem(
                    key.getSkill().toItemStack(),
                    event -> {
                        // スキルアイテム除外

                        if (event.getView().getTopInventory().getType() == InventoryType.CRAFTING) {
                            event.setCancelled(true);
                            return;
                        }

                        // インベントリ特定
                        if (event.getView().getTopInventory().getType() == InventoryType.CHEST) {
                            InventoryFrame openedInv = user.getOpenedInventory();

                            if (openedInv == null) {
                                event.setCancelled(true);
                                return;
                            }

                            if (event.getView().getTopInventory() == openedInv.inv) {

                                // スキル除外
                                key.setSkill(null);
                                user.sendMessage("スキルを除外しました");
                                setItem(event.getSlot(), skillItem(key));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        user.getUserInventory().inv.getItem(event.getSlot()).setAmount(1);
                                    }
                                }.runTaskLater(SkillMaster.instance, 1);
                                event.setCancelled(true);
                            }
                        }
                    }
            );
        }
    }
}