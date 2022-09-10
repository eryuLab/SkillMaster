package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.skill.SkillType;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillInventory extends InventoryFrame {

    private int page;

    public SkillInventory(SkillUser user, int page) {
        super(user, 6, "スキルメニュー：" + user.getSelectedWeapon().getJp());
        this.page = page;
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

        // 設置したかのマップ作成
        List<SkillMap> typeMap = new ArrayList<>();
        for (SkillType type : SkillType.values()) {

            List<Skill> byType = new ArrayList<>();

            for (Skill skill : skillList) {
                if (skill.getType() == type)
                    byType.add(skill);
            }
            typeMap.add(new SkillMap(type, byType));
        }

        // ページ確認
        // どのタイプのどの行からはじまるのかを算出する
        int allRows = typeMap.stream().mapToInt(SkillMap::row).sum();
        int maxPage = allRows / 5;

        // 現在のページがどの行からはじまるのかを算出する
        int nowType = 0; //現在のタイプ
        int firstRow = 0; //始めの行数
        final int preRow = page * 5;
        // firstRowがpreRowを超えたらbreak
        boolean brk = false;
        for (SkillMap skillMap : typeMap) {
            if (brk)
                break;

            nowType = typeMap.indexOf(skillMap);

            for (int i = 0; i < skillMap.row(); i++) {
                skillMap.set++;
                firstRow++;
                if (firstRow >= preRow) {
                    brk = true;
                    break;
                }
            }
        }

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

                    user.setOpenedInventory(new SkillInventory(user, page - 1));
                }
        ));
        // 次のページ
        setItem(53, new InvItem(
                createItemStack(
                        Material.ARROW,
                        "次のページ",
                        List.of(lore)
                ),
                event -> {
                    event.setCancelled(true);

                    if (page == maxPage)
                        return;

                    user.setOpenedInventory(new SkillInventory(user, page + 1));
                }
        ));

        // スキルアイテム
        // 一行ずつ処理
        for (int row = 0; row < 5; row++) {

            // 種類がなければ処理終了
            if (nowType >= typeMap.size())
                break;

            // nowSkillMap
            SkillMap map = typeMap.get(nowType);

            // アイコンが設置されていないとき
            if (!map.setIcon) {
                // アイコン設置
                setItem(row * 9, new InvItem(
                        createItemStack(
                                map.type.getMaterial(),
                                map.type.getJp(),
                                List.of()
                        ),
                        event -> event.setCancelled(true)
                ));
                map.setIcon = true;
            }

            // 一行の処理
            for (int slot = row * 9 + 2; slot < row * 9 + 9; slot++) {

                // 種類に何も入っていないとき
                if (map.skillList.size() == 0) {
                    // 行を変えないで次の処理へ移行
                    row--;
                    break;
                }

                // スキル設置
                InvItem item;
                // スキルアイテムがある場合
                if (!map.setAll()) {
                    ItemStack skillItem = map.get().toItemStack();
                    item = new InvItem(
                            skillItem,
                            event -> {
                                event.setCancelled(true);
                                if (event.getCursor().getType() == Material.AIR) {
                                    // カーソルがairだったらスキルアイテム取得
                                    event.setCursor(skillItem);

                                } else {
                                    // カーソルがSkillItemだったらカーソルをairに変更
                                    Skill cursorSkill = skillManager.fromItemStack(event.getCursor());

                                    if (cursorSkill != null)
                                        event.setCursor(new ItemStack(Material.AIR));
                                }
                            }
                    );
                } else { //スキルアイテムがない場合
                    item = new InvItem(
                            new ItemStack(Material.AIR),
                            event -> {
                                //カーソルがairじゃなかったらカーソルをairに変更
                                if (event.getCursor().getType() != Material.AIR) {
                                    event.setCancelled(true);

                                    Skill cursorSkill = skillManager.fromItemStack(event.getCursor());

                                    if (cursorSkill != null)
                                        event.setCursor(new ItemStack(Material.AIR));
                                }
                            }
                    );
                }
                setItem(slot, item);
            }

            // タイプ確認
            if (map.setAll())
                nowType++;
        }
    }

    /**
     * スキルアイテムがどこまで設置されているかを表すデータ
     */
    private class SkillMap {

        private final SkillType type;

        private final List<Skill> skillList;

        private int set = -1;

        private boolean setIcon = false;

        private SkillMap(SkillType type, List<Skill> skillList) {
            this.type = type;
            this.skillList = skillList;
        }

        private Skill get() {
            set++;
            return skillList.get(set);
        }
        /**
         * すべてのスキルを設置したかを返します
         * @return 全てのスキルを設置していたらtrue
         */
        private boolean setAll() {
            return skillList.size() <= set + 1;
        }

        /**
         * 使用する行数を返します
         * @return 使用する行数
         */
        private int row() {
            int row = skillList.size() / 7;
            if (skillList.size() % 7 != 0)
                row++;

            return row;
        }
    }
}