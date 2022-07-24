package net.lifecity.mc.skillmaster;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

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

            if (event.getAction().isLeftClick()) //攻撃を入力
                user.leftClick();

            else if (event.getAction().isRightClick()) //右クリックスキルを入力
                user.rightClick();
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // プレイヤーが木の剣で攻撃したらイベントキャンセル
        if (event.getDamager() instanceof Player player) {
            SkillUser user = SkillMaster.instance.getUserList().get(player);
            if (user.getHandMaterial() == Material.WOODEN_SWORD) {

                if (user.getActivatingSkill() != null) { //発動中のすきるがあるか
                    event.setCancelled(true);
                } else {
                    event.setDamage(1.5);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        SkillUser user = SkillMaster.instance.getUserList().get(event.getPlayer());

        if (user.getHandMaterial() == Material.WOODEN_SWORD) { //Fスキル入力
            event.setCancelled(true);
            user.f();
        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        SkillUser user = SkillMaster.instance.getUserList().get(event.getPlayer());

        if (event.getItemDrop().getItemStack().getType() == Material.WOODEN_SWORD) { //Qスキル入力
            event.setCancelled(true);
            user.q();
        }
    }
}
