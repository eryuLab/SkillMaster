package net.lifecity.mc.skillmaster.skill;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * すべてのスキルのスーパークラス
 */
public class Skill {

    @Getter
    protected final String name;

    protected final Weapon weapon;

    @Getter
    protected final SkillType type;

    protected final List<String> lore;

    protected final int num;

    protected final int point;

    protected final int interval;

    protected final SkillUser user;

    @Getter
    protected boolean inInterval = false;

    protected Skill(String name, Weapon weapon, SkillType type, List<String> lore, int num, int point, int interval, SkillUser user) {
        this.name = name;
        this.weapon = weapon;
        this.type = type;
        this.lore = lore;
        this.num = num;
        this.point = point;
        this.interval = interval;
        this.user = user;
    }

    /**
     * スキルを発動します
     */
    public void activate() {
        // ログ
        user.sendActionBar(ChatColor.DARK_AQUA + "スキル『" + name + "』発動");

        deactivate();
    }

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    public void deactivate() {
        if (inInterval)
            return;

        // インターバル中にする
        inInterval = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                inInterval = false;
            }
        }.runTaskLater(SkillMaster.instance, interval);
    }

    /**
     * パーティクルを表示します
     * @param particle 表示するパーティクル
     * @param location 表示する位置
     */
    protected void particle(Particle particle, Location location) {
        user.getPlayer().getWorld().spawnParticle(particle, location, 1);
    }

    /**
     * パーティクルを表示します
     * @param particle 表示するパーティクル
     * @param location 表示する位置
     * @param blockData パーティクルのデータ
     */
    protected void particle(Particle particle, Location location, BlockData blockData) {
        user.getPlayer().getWorld().spawnParticle(particle, location, 1, blockData);
    }

    /**
     * 引数の武器が使用可能かを返します
     * @param weapon この武器が使えるか確かめます
     * @return 武器が使えるかどうか
     */
    public boolean usable(Weapon weapon) {
        return this.weapon == weapon;
    }

    /**
     * このスキルをItemStackとして現します
     * @return ItemStackになったスキル
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(Material.ARROW);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.WHITE + "武器: " + weapon.getJp());
        lore.add(ChatColor.WHITE + "タイプ: " + type);
        for (String str : this.lore) {
            lore.add(ChatColor.WHITE + str);
        }

        meta.setLore(lore);

        meta.setCustomModelData(id());

        item.setItemMeta(meta);

        return item;
    }

    /**
     * ItemStackがSkillであるか判定します
     * @param itemStack 比較するアイテム
     * @return 一致したらtrue
     */
    public boolean is(ItemStack itemStack) {
        if (!itemStack.hasItemMeta())
            return false;
        if (!itemStack.getItemMeta().hasCustomModelData())
            return false;
        return id() == itemStack.getItemMeta().getCustomModelData();
    }

    /**
     * このスキルが他のスキルと同じものであるか判定します
     * @param other 比較するスキル
     * @return 一致するかどうか
     */
    public boolean is(Skill other) {
        return id() == other.id();
    }

    /**
     * このスキルのIDを取得します
     * @return スキルのID
     */
    private int id() {
        return weapon.getNumber() * 100 + num;
    }
}
