package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import net.lifecity.mc.skillmaster.inventory.SkillInventory;
import net.lifecity.mc.skillmaster.inventory.WeaponInventory;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Command("skill")
public class SkillCommand {

    @Subcommand("generate-weapon")
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
    
    @Subcommand("change-mode")
    public static void mode(Player player, @AMultiLiteralArgument({"battle", "training", "unarmed"}) String name) {
        SkillUser user = SkillMaster.instance.getUserList().get(player);

        UserMode mode = UserMode.valueOf(name);

        user.setMode(mode);

        player.sendMessage("モードを" + name + "に変更しました");
    }

    @Subcommand("open-menu")
    public static void menu(Player player, @AMultiLiteralArgument({"skill", "weapon"}) String menu) {
        SkillUser user = SkillMaster.instance.getUserList().get(player);

        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage("クリエイティブ時のメニューの挙動は補償されていません。");
        }

        if (menu.equals("skill")) {
            user.setOpenedInventory(new SkillInventory(user));

            user.getOpenedInventory().open();
        }
        else if (menu.equals("weapon")) {
            user.setOpenedInventory(new WeaponInventory(user));

            user.getOpenedInventory().open();
        }
    }
}
