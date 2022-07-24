package net.lifecity.mc.skillmaster.user;

import com.google.j2objc.annotations.ObjectiveCName;
import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.ActionableSkill;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.skills.LeafFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class SkillUser {

    private final int SKILL_SET_SIZE = 3;

    @Getter
    private final Player player;

    @Getter
    private ActionableSkill activatingSkill;

    @Getter
    private UserSkill[] rightSkillSet;

    @Getter
    private int rightIndex = 0;

    @Getter
    private UserSkill[] qSkillSet;

    @Getter
    private int qIndex = 0;

    @Getter
    private UserSkill[] fSkillSet;

    @Getter
    private int fIndex = 0;

    public SkillUser(Player player) {
        this.player = player;
        this.rightSkillSet = new UserSkill[] {
                new UserSkill(new LeafFlow()),
                null,
                null
        };
        this.qSkillSet = new UserSkill[] {
                null,
                null,
                null
        };
        this.fSkillSet = new UserSkill[] {
                null,
                null,
                null
        };
    }

    public void leftClick() {
        // 発動中の攻撃スキルが存在するか
        if (activatingSkill != null)
            activatingSkill.action(this);
    }

    public void rightClick() {
        // Shiftが押されているか
        if (player.isSneaking()) {
            rightIndex++;
            if (rightIndex == SKILL_SET_SIZE)
                rightIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
        }
        else
            activate(rightSkillSet[rightIndex]);
    }
    public void q() {
        // Shiftば押されているか
        if (player.isSneaking()) {
            qIndex++;
            if (qIndex == SKILL_SET_SIZE)
                qIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
        }
        else
            activate(qSkillSet[qIndex]);
    }
    public void f() {
        // Shiftが押されているか
        if (player.isSneaking()) {
            fIndex++;
            if (fIndex == SKILL_SET_SIZE)
                fIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
        }
        else
            activate(fSkillSet[fIndex]);
    }

    private void activate(UserSkill userSkill) {
        // null確認
        if (userSkill == null) {
            sendMessage("スキルがセットされていません");
            return;
        }

        // スキルが追加動作を持っている場合、動作を使えるように登録
        if (userSkill.getSkill() instanceof ActionableSkill actionableSkill)
            activatingSkill = actionableSkill;

        // スキルを発動
        userSkill.activate();

        // ログ
        sendActionBar(ChatColor.DARK_AQUA + "スキル『" + userSkill.getSkill().getName() + "』発動");
    }

    public class UserSkill {

        @Getter
        private final Skill skill;

        private boolean inInterval = false;

        public UserSkill(Skill skill) {
            this.skill = skill;
        }

        public void activate() {
            if (inInterval) //インターバル中は発動不可
                return;

            skill.activate(SkillUser.this); //スキル発動
            inInterval = true; //「インターバル中」に設定

            new BukkitRunnable() { //インターバルが過ぎたら再度発動可能
                @Override
                public void run() {
                    inInterval = false;

                    if (SkillUser.this.activatingSkill == skill)
                        SkillUser.this.activatingSkill = null;
                        ((ActionableSkill) skill).setActionable(true);

                    sendActionBar(ChatColor.RED + "スキル『" + skill.getName() + "』終了");
                }
            }.runTaskLater(SkillMaster.instance, skill.getInterval()); //インターバル後に実行
        }
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
     * 敵からの攻撃を防御します
     * todo 複数の攻撃を防御
     */
    private void defense() {}

    /**
     * このプレイヤーの位置から一番近いentityを取得します
     * @return このプレイヤーの位置から一番近いentity
     */
    public Entity getNearestEntity(double radius) {
        // 半径radiusで近くのentityのリストを取得
        List<Entity> near = player.getNearbyEntities(radius, radius, radius);

        // 近くになにもいなかったらnullを返す
        if (near.size() == 0) return null;

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

        return nearest;
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
