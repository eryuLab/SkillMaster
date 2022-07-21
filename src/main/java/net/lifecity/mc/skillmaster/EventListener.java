package net.lifecity.mc.skillmaster;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SkillMaster.instance.getUserList().add(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SkillMaster.instance.getUserList().remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        SkillUser user = SkillMaster.instance.getUserList().get(event.getPlayer());

        if (user.getHandMaterial() == Material.WOODEN_SWORD) { //木の剣を持っているときだけ

            if (event.getAction().isLeftClick()) //武器を構える
                user.leftClick();

            else if (event.getAction().isRightClick()) //素早く移動する
                user.rightClick();
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // プレイヤーが木の剣で攻撃したらイベントキャンセル
        if (event.getDamager() instanceof Player player) {
            SkillUser user = SkillMaster.instance.getUserList().get(player);
            if (user.getHandMaterial() == Material.WOODEN_SWORD) {
                event.setCancelled(true);
            }
        }
    }
}
