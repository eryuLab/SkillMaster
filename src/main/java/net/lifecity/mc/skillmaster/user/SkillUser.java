package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SkillUser {

    @Getter
    private final Player player;

    @Getter
    private boolean reinforced = false;
    @Getter
    private boolean canBeReinforced = true;

    public SkillUser(Player player) {
        this.player = player;
    }

    /**
     * 武器を構えます
     * ケース１:お互いが構えた状態で接する
     * ケース２:どちらかが構えた時、すでに接している状態
     * 攻撃失敗 and 防御成功
     * ケース３:Aが構えているが、Bが構えていない
     * A攻撃成功 and B防御失敗
     */
    public void reinforce() {
        if (!canBeReinforced) { //クールタイム確認
            playSound(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE);
            if (reinforced)
                sendMessage("すでに武器を構えている");
            else
                sendMessage("まだ武器を構えられない");
        }

        new Reinforce(3, 5); //処理開始
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

            sendMessage("武器を構えた");

            runTaskTimer(SkillMaster.instance, 0, next);
        }

        @Override
        public void run() {
            //タイマー:近くに敵がいたら
            // 敵が武器を構えていたら防御
            // 構えていなかったら待機
            //タイマー切れ:近くに敵がいたら攻撃
            tick--;
        }
    }

    /**
     * 標的を攻撃します
     * todo 位置から一番近いentityが標的
     * todo 目線から一番近いentityが標的
     * todo 複数の標的に攻撃
     */
    public void attack() {
        List<Entity> targets = player.getNearbyEntities(1.5, 1.5, 1.5);
    }

    /**
     * このプレイヤーの位置から一番近いentityを取得します
     * @return このプレイヤーの位置から一番近いentity
     */
    private Entity getNearestEntityByLocation(double radius) {
        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
        if (entities.size() == 0) return null;
        return entities.get(0);
    }

    /**
     * このプレイヤーにメッセージログを送信します
     * @param msg 送信するメッセージ
     */
    public void sendMessage(String msg) {
        player.sendMessage(msg);
    }

    /**
     * このプレイヤーの位置でSEを再生します
     * @param sound 再生するSE
     */
    public void playSound(Sound sound) {
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }
}
