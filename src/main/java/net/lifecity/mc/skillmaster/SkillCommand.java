package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import net.lifecity.mc.skillmaster.game.stage.GameStage;
import net.lifecity.mc.skillmaster.game.stage.field.TwoPoint;
import net.lifecity.mc.skillmaster.game.games.Duel;
import net.lifecity.mc.skillmaster.inventory.SkillInventory;
import net.lifecity.mc.skillmaster.inventory.WeaponInventory;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Command("skill")
public class SkillCommand {

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
