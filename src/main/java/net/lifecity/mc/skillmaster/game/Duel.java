package net.lifecity.mc.skillmaster.game;

import lombok.Getter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * １対１の決闘モード
 *
 * どちらかが倒れると終了する
 * 制限時間を過ぎた場合はその瞬間から先に攻撃を当てたほうが勝ちとなる
 */
public class Duel {

    @Getter
    private final DuelField field;

    @Getter
    private final SkillUser userA;

    @Getter
    private final SkillUser userB;

    private final int timeLimit;

    private int count = 0;

    private boolean suddenDeath = false;

    private boolean finished = false;

    public Duel(DuelField field, SkillUser userA, SkillUser userB, int timeLimit) {
        this.field = field;
        this.userA = userA;
        this.userB = userB;
        this.timeLimit = timeLimit;
    }

    public void start() {
        SkillMaster.instance.getDuelList().add(this);

        // 開始地点へテレポート
        userA.sendMessage("テレポートしました(嘘だよ)");
        userB.sendMessage("テレポートしました(嘘だよ)");

        // カウントダウン
        // 3..2..1..battle start!!
        new BukkitRunnable() {
            int countDown = 3;
            @Override
            public void run() {
                if (countDown <= 3 && countDown >= 1) {
                    String msg = countDown + "..";
                    userA.sendMessage(ChatColor.YELLOW + msg);
                    userB.sendMessage(ChatColor.YELLOW + msg);

                    Sound sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
                    userA.playSound(sound);
                    userB.playSound(sound);
                } else {
                    String msg = "START!!";
                    userA.sendMessage(ChatColor.RED + msg);
                    userB.sendMessage(ChatColor.RED + msg);

                    Sound sound = Sound.ENTITY_GENERIC_EXPLODE;
                    userA.playSound(sound);
                    userB.playSound(sound);

                    cancel();
                }
                countDown--;
            }
        }.runTaskTimer(SkillMaster.instance, 0, 20);

        // 戦闘中のメインタイマースタート
        new BukkitRunnable() {
            @Override
            public void run() {
                // 終了していたらキャンセル
                if (finished) {
                    cancel();
                }
                // 制限時間を過ぎていた場合サドンデスにする
                if (count >= timeLimit && !suddenDeath) {
                    suddenDeath = true;
                }
                // 時間表示

                count++;
            }
        }.runTaskTimer(SkillMaster.instance, 80, 1);
    }

    public void stop(SkillUser winner) {
        // 勝敗表示
        String win = ChatColor.YELLOW + "Win";
        String lose = ChatColor.BLUE + "Lose";

        // タイトル送信
        if (winner.match(userA)) {
            userA.sendTitle(win, "");
            userB.sendTitle(lose, "");
        } else if (winner.match(userB)) {
            userA.sendTitle(lose, "");
            userB.sendTitle(win, "");
        }

        // ロビーへテレポート
        userA.sendMessage("3秒後に強制テレポートします...");
        userB.sendMessage("3秒後に強制テレポートします...");

        new BukkitRunnable() {
            @Override
            public void run() {
                userA.sendMessage("テレポートしました(嘘だよ)");
                userB.sendMessage("テレポートしました(嘘だよ)");
            }
        }.runTaskLater(SkillMaster.instance, 60);

        SkillMaster.instance.getDuelList().remove(this);
    }

    /**
     * 攻撃時の処理
     * 制限時間経過後に攻撃が行われたらゲームを終了します
     * 攻撃後、どちらかが死んでいたらゲームを終了します
     * 基本的に左クリック、スキル攻撃の処理の中で呼び出されます
     * @param attacker
     */
    public void onAttack(SkillUser attacker) {
        // 引数がこのゲームのプレイヤーじゃなかったらreturn
        if (!(attacker.match(userA) || attacker.match(userB)))
            return;

        // プレイヤーが死んでいたら終了
        if (userA.getPlayer().isDead() || userB.getPlayer().isDead()) {
            Bukkit.getPlayer("Koryu25").sendMessage("isDead()");
            stop(attacker);
            return;
        }
        if (userA.getPlayer().getHealth() == 0 || userB.getPlayer().getHealth() == 0) {
            Bukkit.getPlayer("Koryu25").sendMessage("health == 0");
            stop(attacker);
        }

        // 制限時間が過ぎていたら終了
        if (finished)
            stop(attacker);
    }

    public boolean joined(SkillUser user) {
        if (userA.match(user))
            return true;
        else if (userB.match(user))
            return true;
        else
            return false;
    }

    public class DuelTimer extends BukkitRunnable {
        @Getter
        private int count = 0;

        @Override
        public void run() {

        }
    }
}