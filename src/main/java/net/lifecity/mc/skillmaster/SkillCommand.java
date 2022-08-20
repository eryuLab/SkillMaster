package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import net.lifecity.mc.skillmaster.inventory.SkillInventory;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import net.lifecity.mc.skillmaster.weapon.Weapon;
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
    public static void mode(Player player, @AMultiLiteralArgument({"LOBBY", "BATTLE"}) String name) {
        SkillUser user = SkillMaster.instance.getUserList().get(player);

        UserMode mode = UserMode.valueOf(name);

        user.setMode(mode);

        player.sendMessage("モードを" + name + "に変更しました");
    }

    @Subcommand("menu")
    public static void menu(Player player, @AMultiLiteralArgument({"skill", "weapon"}) String menu) {
        SkillUser user = SkillMaster.instance.getUserList().get(player);

        if (menu.equalsIgnoreCase("skill")) {
            user.setOpenedInventory(new SkillInventory(user, user.getSelectedWeapon()));

            user.getOpenedInventory().open();

            return;

        }
        if (menu.equalsIgnoreCase("weapon")) {
            player.sendMessage("現在、このコマンドは使用できません。");
            return;
        }
    }
}
