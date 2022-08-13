package net.lifecity.mc.skillmaster;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class EventListener implements Listener {

    private boolean rightFlag = false;
    private boolean leftFlag = false;

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

        if (user.getMode() == UserMode.LOBBY)
            return;

        if (user.getHandItem().getType() == Material.WOODEN_SWORD) { //木の剣を持っているときだけ


            if (event.getAction().isLeftClick()) { //攻撃を入力
                if (leftFlag) {
                    leftFlag = false;
                    return;
                }
                user.sendMessage("左クリック検知");
                user.leftClick();
            }

            else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                user.rightClick();
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(!rightFlag) {
                    user.rightClick();
                    rightFlag = true;
                } else {
                    rightFlag = false;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // プレイヤーが木の剣で攻撃したらイベントキャンセル
        if (event.getDamager() instanceof Player player) {
            SkillUser user = SkillMaster.instance.getUserList().get(player);

            if (user.getMode() == UserMode.LOBBY)
                return;

            if (user.getHandItem().getType() == Material.WOODEN_SWORD) {

                if (user.getActivatingSkill() != null) { //発動中のすきるがあるか
                    event.setCancelled(true);
                } else {
                    event.setDamage(1.5);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        // 落下ダメージを無効化
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
            if (event.getEntity() instanceof Player)
                event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        SkillUser user = SkillMaster.instance.getUserList().get(event.getPlayer());

        if (user.getMode() == UserMode.LOBBY)
            return;

        if (user.getHandItem().getType() == Material.WOODEN_SWORD) { //Fスキル入力
            event.setCancelled(true);
            user.swap();
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        SkillUser user = SkillMaster.instance.getUserList().get(event.getPlayer());

        if (user.getMode() == UserMode.LOBBY)
            return;

        if (event.getItemDrop().getItemStack().getType() == Material.WOODEN_SWORD) { //Qスキル入力
            // 空中を見ていたら(視線の先にブロックがなければ)
            if (user.getPlayer().getTargetBlock(5) != null) {
                leftFlag = true;
            }
            event.setCancelled(true);
            user.drop(Weapon.fromItemStack(event.getItemDrop().getItemStack()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    }
}
