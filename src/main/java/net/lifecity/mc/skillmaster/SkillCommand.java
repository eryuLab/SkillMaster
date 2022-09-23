package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import net.lifecity.mc.skillmaster.game.games.Duel;
import net.lifecity.mc.skillmaster.game.stage.GameStage;
import net.lifecity.mc.skillmaster.inventory.SkillInventory;
import net.lifecity.mc.skillmaster.inventory.WeaponInventory;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("skill")
public class SkillCommand {

    private static final String[] helpMsgList = {
            "======== " + ChatColor.RED + "Skill" + ChatColor.WHITE + "-" + ChatColor.YELLOW + "Command" + ChatColor.WHITE + "-" + ChatColor.BLUE + "Help" + ChatColor.WHITE + " ========",
            "/skill weapon [武器] -> 武器を取得します。",
            "/skill mode [モード] -> 自身のモードを変更します。",
            "/skill menu [メニュー] -> メニューを開きます。",
            "/skill duel [マップ] [プレイヤー1] [プレイヤー2] -> マップと2人のプレイヤーを指定してデュエルゲームを開始します。",
            "すべてプレイヤー用のコマンドです。コンソールからは入力しないでください。",
            "================"
    };

    @Default
    public static void skill(CommandSender sender) {
        for (String msg : helpMsgList) {
            sender.sendMessage(msg);
        }
    }

    @Subcommand("weapon")
    public static void weapon(Player player, @AMultiLiteralArgument({
            "直剣",
            "短剣",
            "大剣",
            "太刀",
            "刺剣",
            "槌矛"
    }) String name) {
        Weapon weapon = Weapon.fromJP(name);

        if (weapon == null) {
            player.sendMessage("武器が見つかりません");
            return;
        }

        player.getInventory().addItem(weapon.toItemStack());
    }
    
    @Subcommand("mode")
    public static void mode(Player player, @AMultiLiteralArgument({"battle", "training", "unarmed"}) String name) {
        SkillUser user = SkillMaster.instance.getUserList().get(player);

        UserMode mode = UserMode.valueOf(name.toUpperCase());

        user.changeMode(mode);

        player.sendMessage("モードを" + mode.getJp() + "に変更しました");
    }

    @Subcommand("menu")
    public static void menu(Player player, @AMultiLiteralArgument({"skill", "weapon"}) String menu) {
        SkillUser user = SkillMaster.instance.getUserList().get(player);

        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(
                    ChatColor.WHITE + "["
                    + ChatColor.RED+ "警告"
                    + ChatColor.WHITE + "]: クリエイティブ時のメニューの挙動は補償されていません。"
            );
        }

        if (menu.equals("skill")) {
            user.setOpenedInventory(new SkillInventory(user, 0));

            user.getOpenedInventory().open();
        }
        else if (menu.equals("weapon")) {
            user.setOpenedInventory(new WeaponInventory(user));

            user.getOpenedInventory().open();
        }
    }

    @Subcommand("duel")
    public static void duel(Player player, @AMultiLiteralArgument({"闘技場"}) String stageName, @APlayerArgument Player player1, @APlayerArgument Player player2) {
        // プレイヤー引数確認
        if (player1 == player2) {
            player.sendMessage("一人で戦うことはできません");
            return;
        }

        // ユーザー取得
        SkillUser user1 = SkillMaster.instance.getUserList().get(player1);
        SkillUser user2 = SkillMaster.instance.getUserList().get(player2);

        // ゲームがないか確認
        if (SkillMaster.instance.getGameList().inGamingUser(user1)) {
            player.sendMessage(player1.getName() + "はすでにゲーム中です");
            return;
        }
        if (SkillMaster.instance.getGameList().inGamingUser(user2)) {
            player.sendMessage(player2.getName() + "はすでにゲーム中です");
            return;
        }

        // ステージ取得
        GameStage stage = SkillMaster.instance.getStageList().getFromName(stageName);

        if (stage == null) {
            player.sendMessage(stageName + "は登録されていません");
            return;
        }

        // ステージが使用中であるか確認
        if (stage.inUsing()) {
            player.sendMessage(stageName + "は使用中です");
            return;
        }

        // ゲーム開始
        Duel duel = new Duel(stage, user1, user2);
        duel.start();
    }
}
