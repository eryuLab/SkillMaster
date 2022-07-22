package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import net.lifecity.mc.skillmaster.skill.skills.LeafFlow;
import org.bukkit.entity.Player;

@Command("skill")
public class SkillCommand {

    @Default
    public static void skill(Player player, @ADoubleArgument double radius) {
        LeafFlow skill = (LeafFlow) SkillMaster.instance.getUserList().get(player).getRightClick().getSkill();
        player.sendMessage("リーフフローの半径を" + skill.getRadius() + "から" + radius + "に設定しました");
        skill.setRadius(radius);
    }
}
