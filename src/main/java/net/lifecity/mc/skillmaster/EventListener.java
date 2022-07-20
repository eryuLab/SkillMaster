package net.lifecity.mc.skillmaster;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        if (event.getAction().isLeftClick())
            user.reinforce();
    }
}
