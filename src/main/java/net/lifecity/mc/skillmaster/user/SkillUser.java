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
     */
    public void reinforce() {
        if (!canBeReinforced) { //武器を構えられるか
            playSound(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE);
            if (reinforced)
                sendMessage("すでに武器を構えている");
            else
                sendMessage("まだ武器を構えられない");
        }

        reinforced = true; //武器を構える
        canBeReinforced = false; //一定時間武器を構えられなくなる
        //スキルポイント消費

        sendMessage("武器を構えた"); //ログ

        attack();

        new BukkitRunnable() { //時間経過で構えが崩れる
            @Override
            public void run() {
                //メイン処理
                reinforced = false;

                //演出
                playSound(Sound.ENTITY_PLAYER_ATTACK_STRONG);
                sendMessage("構えが崩れた");
            }
        }.runTaskTimer(SkillMaster.instance, 0, 3);


        new BukkitRunnable() { //一定時間後、再び構えられるようになる
            @Override
            public void run() {
                canBeReinforced = true;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 5);
    }

    /**
     * 標的を攻撃します
     * todo 位置から一番近いentityが標的
     * todo 目線から一番近いentityが標的
     * todo 複数の標的に攻撃
     */
    public void attack() {
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
