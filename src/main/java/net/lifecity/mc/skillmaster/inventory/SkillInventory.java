package net.lifecity.mc.skillmaster.inventory;

import lombok.AllArgsConstructor;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillInventory extends InventoryFrame {

    private SkillManager sm;
    private final int page;

    public SkillInventory(SkillUser user, int page) {
        super(user, 6, "スキルメニュー：" + user.getSelectedWeapon().getJp());
        this.sm = new SkillManager(user);
        this.page = page;
        init();
    }

    @Override
    public void init() {
        SkillManager skillManager = new SkillManager(user);
        List<Skill> skillList = skillManager.fromWeapon(user.getSelectedWeapon());

        // 仕切り
        for (int i = 1; i <= 54; i += 9) {
            setItem(i, new InvItem(
                    createItemStack(
                            Material.BLACK_STAINED_GLASS_PANE,
                            " ",
                            List.of()
                    ),
                    event -> event.setCancelled(true)
            ));
        }

        // 武器メニューに戻る
        setItem(45, new InvItem(
                user.getSelectedWeapon().toItemStack(),
                event -> {
                    // 武器メニューへ移動
                    user.setOpenedInventory(new WeaponInventory(user));
                    user.getOpenedInventory().open();
                }
        ));

        // 下辺
        for (int i = 48; i <= 52; i++) {
            setItem(i, new InvItem(
                    createItemStack(
                            Material.IRON_BARS,
                            " ",
                            List.of()
                    ),
                    event -> event.setCancelled(true)
            ));
        }

        // スキルアイテム
        // すべての行を生成
        List<TypeRow> rowList = new ArrayList<>();
        for (SkillType type : SkillType.values()) {

            // 種類分けされたスキルリストを生成
            List<Skill> skillListByType = new ArrayList<>();
            for (Skill skill : skillList) {
                if (skill.getType() == type)
                    skillListByType.add(skill);
            }
            // 行数を計算
            int maxRowNum = skillListByType.size() / 7;
            if (skillListByType.size() % 7 != 0)
                maxRowNum++;

            // 行数だけリストに行を追加
            for (int rowNum = 0; rowNum < maxRowNum; rowNum++) {
                rowList.add(new TypeRow(type, skillListByType, rowNum));
            }
        }
        List<SkillType> setIcon = new ArrayList<>();
        // 行数だけ繰り返す
        for (int row = 0; row < 5; row++) {

            int rowIndex = page * 5 + row;
            int first = row * 9 + 2;

            // 行が存在しなかったら空白行設置
            if (rowList.size() <= rowIndex) {
                for (int slot = first; slot < first + 7; slot++) {
                    setItem(slot, getAirItem());
                }
                continue;
            }
            // 行が存在したらスキルアイテム行設置
            TypeRow typeRow = rowList.get(rowIndex);
            int num = 0;
            for (int slot = first; slot < first + 7; slot++) {
                // アイコン設置
                SkillType type = typeRow.type;
                if (!setIcon.contains(type)) {
                    setItem(first - 2, new InvItem(
                            createItemStack(
                                    type.getMaterial(),
                                    "タイプ: " + type.getJp(),
                                    List.of()
                            ),
                            event -> event.setCancelled(true)
                    ));
                    setIcon.add(type);
                }

                // スキルアイテム設置
                setItem(slot, typeRow.getSkillItem(num));
                num++;
            }
        }

        // ページ
        int maxPage = rowList.size() / 5;

        String lore = (page + 1) + "/" + (maxPage + 1);
        // 前のページ
        setItem(47, new InvItem(
                createItemStack(
                        Material.ARROW,
                        "前のページ",
                        List.of(lore)
                ),
                event -> {
                    event.setCancelled(true);

                    if (page == 0)
                        return;

                    // ページ移動
                    user.setOpenedInventory(new SkillInventory(user, page - 1));
                    user.getOpenedInventory().open();
                }
        ));
        // 次のページ
        int finalMaxPage = maxPage;
        setItem(53, new InvItem(
                createItemStack(
                        Material.ARROW,
                        "次のページ",
                        List.of(lore)
                ),
                event -> {
                    event.setCancelled(true);

                    if (page == finalMaxPage)
                        return;
                    user.getPlayer().sendMessage("次のページ");

                    // ページ移動
                    user.setOpenedInventory(new SkillInventory(user, page + 1));
                    user.getOpenedInventory().open();
                }
        ));
    }

    private InvItem getAirItem() {
        return new InvItem(
                new ItemStack(Material.AIR),
                event -> {
                    //カーソルがairじゃなかったらカーソルをairに変更
                    if (event.getCursor().getType() != Material.AIR) {
                        event.setCancelled(true);

                        Skill cursorSkill = sm.fromItemStack(event.getCursor());

                        if (cursorSkill != null)
                            event.setCursor(new ItemStack(Material.AIR));
                    }
                }
        );
    }

    @AllArgsConstructor
    private class TypeRow {

        private final SkillType type;
        private final List<Skill> skillList;
        private final int rowNum;

        /**
         * 指定番号のアイテムを生成して返します
         * @param num この番号のアイテムを生成します
         * @return 生成されたSkillItem
         */
        private InvItem getSkillItem(int num) {
            int index = rowNum * 7 + num;
            if (index < skillList.size()) {
                Skill skill = skillList.get(index);
                return new InvItem(
                        skill.toItemStack(),
                        event -> {
                            event.setCancelled(true);
                            if (event.getCursor().getType() == Material.AIR) {
                                // カーソルがairだったらスキルアイテム取得
                                event.setCursor(skill.toItemStack());

                            } else {
                                // カーソルがSkillItemだったらカーソルをairに変更
                                Skill cursorSkill = sm.fromItemStack(event.getCursor());

                                if (cursorSkill != null)
                                    event.setCursor(new ItemStack(Material.AIR));

                                event.getCurrentItem().setAmount(1);
                            }
                        }
                );
            } else {
                return getAirItem();
            }
        }
    }
}