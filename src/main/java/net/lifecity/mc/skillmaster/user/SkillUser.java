package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class SkillUser {

    @Getter
    private final Player player;

    @Getter
    private boolean reinforced = false;
    @Getter
    private boolean canBeReinforced = true;

    @Getter
    private boolean canMoveQuickly = true;

    public SkillUser(Player player) {
        this.player = player;
    }

    /**
     * 攻撃する
     * 入力してから数tick防御状態となる
     * 攻撃が発動したら防御状態にならない
     */
    public void reinforce() {
        if (!canBeReinforced) { //クールタイム確認
            playSound(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE);
            if (reinforced)
                sendMessage("すでに武器を構えている");
            else
                sendMessage("まだ武器を構えられない");
        }

        new Reinforce(3, 10); //処理開始
    }

    private class Reinforce extends BukkitRunnable {

        private int hold;
        private int next;

        private int tick = 0;

        public Reinforce(int hold, int next) {
            this.hold = hold;
            this.next = next;

            reinforced = true; //武器を構える
            canBeReinforced = false; //一定時間武器を構えられなくなる

            sendActionBar(ChatColor.BLUE + "武器を構えた");

            runTaskTimer(SkillMaster.instance, 0, 1);
        }

        @Override
        public void run() {
            // 一番近いentityを標的とする
            Entity target = getNearestEntity(1.5);

            // hold前
            // hold
            // next
            if (tick < hold) { //武器を構え中
                // 敵が武器を構えていたら防御
                if (target instanceof Player) {
                    SkillUser opponent = SkillMaster.instance.getUserList().get((Player) target);
                    if (opponent.reinforced) {
                        // 防御処理
                        defense(opponent.getPlayer());

                        // 構えが崩れる処理
                        reinforced = false;
                        sendActionBar(ChatColor.RED + "武器を振った");
                    }
                }
                // 構えていなかったら待機

            } else if (tick == hold) { //武器を振る
                // 近くに敵がいたら攻撃
                if (target != null) {
                    attack(target);
                }
                // 構え解除
                if (reinforced) {
                    reinforced = false;
                    sendActionBar(ChatColor.RED + "武器を振った");
                }

            } else if (tick == next) { // 構えのクールタイム
                canBeReinforced = true;
                sendActionBar(ChatColor.GREEN + "クールタイム終了");
                cancel();
            }
            tick++;
        }

        /**
         * 標的を攻撃します
         * todo 位置から一番近いentityが標的
         * todo 目線から一番近いentityが標的
         * todo 複数の標的に攻撃
         */
        private void attack(Entity entity) {
            // HP減らす
            Damageable target = (Damageable) entity;
            target.damage(3);
            // playerの見ている方向にノックバック
            target.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(0.7).setY(0.4));
            // 音
            playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP);
        }

        /**
         * 敵からの攻撃を防御します
         * @param opponent 敵
         */
        private void defense(Entity opponent) {
            // シフト押しているとき：パリング
            // シフト押していないとき：ノックバック

            if (player.isSneaking()) //パリング
                player.setVelocity(player.getVelocity().multiply(-0.47));

            else //ノックバック
                player.setVelocity(player.getVelocity().multiply(-1));

            // 間のLocationを取得
            Location loc1 = player.getLocation();
            Location loc2 = opponent.getLocation();
            double x = Math.abs(loc1.getX() - loc2.getX());
            double y = Math.abs(loc1.getY() - loc2.getY());
            double z = Math.abs(loc1.getZ() - loc2.getZ());
            Location center = new Location(player.getWorld(), x, y, z);

            // 間のLocationでSE再生
            center.getWorld().playSound(center, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1f, 1f);
        }
    }

    /**
     * 向いている方向に素早く移動します
     * todo 移動している方向に素早く移動
     */
    public void moveQuickly() {
        if (canMoveQuickly) {
            Vector vector = player.getEyeLocation().getDirection()
                    .normalize()
                    .multiply(1.75)
                    .setY(0.15);
            player.setVelocity(vector);

            canMoveQuickly = false;

            new BukkitRunnable() {
                @Override
                public void run() {
                    canMoveQuickly = true;
                }
            }.runTaskLater(SkillMaster.instance, 8);
        }
    }

    /**
     * このプレイヤーの位置から一番近いentityを取得します
     * @return このプレイヤーの位置から一番近いentity
     */
    private Entity getNearestEntity(double radius) {
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
