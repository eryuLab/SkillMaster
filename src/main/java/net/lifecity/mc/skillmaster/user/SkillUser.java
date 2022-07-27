package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.skills.JumpAttack;
import net.lifecity.mc.skillmaster.skill.skills.LeafFlow;
import net.lifecity.mc.skillmaster.utils.EntitySort;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SkillUser {

    private final int SKILL_SET_SIZE = 3;

    @Getter
    private final Player player;

    @Getter
    private Skill[] rightSkillSet;

    @Getter
    private int rightIndex = 0;

    @Getter
    private Skill[] swapSkillSet;

    @Getter
    private int swapIndex = 0;

    @Getter
    private Skill[] dropSkillSet;

    @Getter
    private int dropIndex = 0;

    public SkillUser(Player player) {
        this.player = player;
        this.rightSkillSet = new Skill[] {
                new LeafFlow(this),
                null,
                null
        };
        this.swapSkillSet = new Skill[] {
                new JumpAttack(this),
                null,
                null
        };
        this.dropSkillSet = new Skill[] {
                null,
                null,
                null
        };
    }

    /**
     * 左クリックを入力した時の処理
     */
    public void leftClick() {
        // 発動中の攻撃スキルが存在するか
        Skill activatingSkill = getActivatingSkill();
        if (activatingSkill != null)
            activatingSkill.leftClick();
    }

    /**
     * 右クリックを入力した時の処理
     */
    public void rightClick() {
        // Shiftが押されているか
        if (player.isSneaking()) {
            rightIndex++;
            if (rightIndex == SKILL_SET_SIZE)
                rightIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
            sendMessage("右クリックのスキルを" + rightIndex + "に変更しました。");
        }
        else
            activate(rightSkillSet[rightIndex]);
    }

    /**
     * スワップキーを押した時の処理
     */
    public void swap() {
        // Shiftが押されているか
        if (player.isSneaking()) {
            swapIndex++;
            if (swapIndex == SKILL_SET_SIZE)
                swapIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
            sendMessage("スワップのスキルを" + swapIndex + "に変更しました。");
        }
        else
            activate(swapSkillSet[swapIndex]);
    }

    /**
     * ドロップキーを押した時の処理
     */
    public void drop() {
        // Shiftば押されているか
        if (player.isSneaking()) {
            dropIndex++;
            if (dropIndex == SKILL_SET_SIZE)
                dropIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
            sendMessage("ドロップのスキルを" + dropIndex + "に変更しました。");
        }
        else
            activate(dropSkillSet[dropIndex]);
    }

    /**
     * スキルを起動するためのメソッド
     * @param skill 起動するスキル
     */
    private void activate(Skill skill) {
        // null確認
        if (skill == null) {
            sendMessage("スキルがセットされていません");
            return;
        }

        // すでに発動しているスキルがあるか
        Skill activatingSkill = getActivatingSkill();
        if (activatingSkill != null) {
            sendMessage("すでに発動中のスキルがあります: スキル『" + activatingSkill.getName() + "』");
            return;
        }

        // インターバル中か確認
        if (skill.isInInterval()) {
            sendMessage("インターバル中です");
            return;
        }

        // 発動中であるか確認
        if (skill.isActivating())
            return;

        // スキルを発動
        skill.activate();
    }

    /**
     * 発動中のスキルを返します
     * @return 発動中のスキル
     */
    public Skill getActivatingSkill() {

        for (Skill skill : rightSkillSet) {
            if (skill == null)
                continue;
            if (skill.isActivating())
                return skill;
        }
        for (Skill skill : swapSkillSet) {
            if (skill == null)
                continue;
            if (skill.isActivating())
                return skill;
        }
        for (Skill skill : dropSkillSet) {
            if (skill == null)
                continue;
            if (skill.isActivating())
                return skill;
        }

        return null;
    }

    /**
     * 標的を攻撃します
     * todo 位置から一番近いentityが標的
     * todo 目線から一番近いentityが標的
     * todo 複数の標的に攻撃
     */
    public void attack() {
        Entity target = player.getTargetEntity(4, false);

        if (target == null)
            return;
        player.attack(target);

        // ダメージ
        // ノックバック
    }

    /**
     * 一番近いEntityを攻撃します
     * @param radius この半径内のEntityに攻撃します
     * @param damage このダメージを与えます
     * @param vector このベクトルを与えます
     * @param sound このSEを再生します
     */
    public void attackNearest(double radius, double damage, Vector vector, Sound sound) {
        List<Entity> entities = getNearEntities(radius);

        if (entities.size() == 0)
            return;

        Entity entity = entities.get(0);
        if (entity == null)
            return;

        if (entity instanceof Damageable target) {
            // 標的にダメージを与える
            target.damage(damage);

            // 標的をノックバックさせる
            target.setVelocity(vector);

            // SE再生
            playSound(sound);
        }
    }

    /**
     * 敵からの攻撃を防御します
     * todo 複数の攻撃を防御
     */
    private void defense() {}

    /**
     * このプレイヤーの位置から一番近いentityを取得します
     * @return このプレイヤーの位置から一番近いentity
     */
    public List<Entity> getNearEntities(double radius) {
        // 半径radiusで近くのentityのリストを取得
        List<Entity> near = player.getNearbyEntities(radius, radius, radius);

        // 近い順にする
        EntitySort.quicksort(player, near, 0, near.size() - 1);

        /*
        // near.get(0)の値で初期化
        Entity nearest = near.get(0);
        double distance1 = player.getLocation().distance(nearest.getLocation());

        // 一番近いentityを見つける
        for (Entity entity : near) {
            double distance2 = player.getLocation().distance(entity.getLocation());
            if (distance1 < distance2) {
                distance1 = distance2;
                nearest = entity;
            }
        }

         */

        return near;
    }

    /**
     * メインハンドのMaterialを取得します
     * @return メインハンドのMaterial
     */
    public Material getHandMaterial() {
        return player.getInventory().getItemInMainHand().getType();
    }

    /**
     * このプレイヤーにメッセージログを送信します
     * @param msg 送信するメッセージ
     */
    public void sendMessage(String msg) {
        player.sendMessage(msg);
    }

    /**
     * アクションバーでメッセージを送信します
     * @param msg 送信するメッセージ
     */
    public void sendActionBar(String msg) {
        player.sendActionBar(msg);
    }

    /**
     * このプレイヤーの位置でSEを再生します
     * @param sound 再生するSE
     */
    public void playSound(Sound sound) {
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

    /**
     * UUIDが一致しているかを確認します
     * @param target 比較するPlayer
     * @return UUIDが一致するか
     */
    public boolean match(Player target) {
        return player.getUniqueId().toString().equals(target.getUniqueId().toString());
    }
}
