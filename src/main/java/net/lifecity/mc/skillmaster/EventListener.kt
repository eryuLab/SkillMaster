package net.lifecity.mc.skillmaster

import com.github.syari.spigot.api.event.events
import net.lifecity.mc.skillmaster.game.function.OnAttack
import net.lifecity.mc.skillmaster.game.function.OnDie
import net.lifecity.mc.skillmaster.user.UserMode
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*

object EventListener {

    private var rightFlag = false
    private var leftFlag = true

    fun register() {
        SkillMaster.INSTANCE.events {
            event<PlayerJoinEvent> {
                SkillMaster.INSTANCE.userList.add(it.player)
            }

            event<PlayerQuitEvent> {
                SkillMaster.INSTANCE.userList.remove(it.player)
            }

            event<PlayerInteractEvent> {
                val user = SkillMaster.INSTANCE.userList.get(it.player) ?: return@event

                if (user.mode == UserMode.UNARMED) return@event

                if (user.handItem.type == Material.WOODEN_SWORD) { //木の剣を持っているときだけ
                    if (it.action.isLeftClick) { //攻撃を入力
                        if (leftFlag) {
                            leftFlag = false
                            return@event
                        }
                        user.leftClick()
                    } else if (it.action == Action.RIGHT_CLICK_AIR) {
                        user.buttonInput(SkillButton.RIGHT)
                    } else if (it.action == Action.RIGHT_CLICK_BLOCK) {
                        rightFlag = if (!rightFlag) {
                            user.buttonInput(SkillButton.RIGHT)
                            true
                        } else {
                            false
                        }
                    }
                }
            }

            event<EntityDamageByEntityEvent> {
                // プレイヤーが木の剣で攻撃したらイベントキャンセル
                val player = it.damager as? Player
                player?.let { pl ->
                    val user = SkillMaster.INSTANCE.userList.get(pl) ?: return@event

                    if(user.mode == UserMode.UNARMED) return@event

                    if(user.handItem.type == Material.WOODEN_SWORD) {
                        if(user.getActivatedSkill() != null) {
                            it.isCancelled = true
                        } else {
                            if(user.mode == UserMode.TRAINING) {
                                it.isCancelled = true
                                return@event
                            }

                            it.damage = 1.5

                            // ゲーム中ならonAttack()呼び出し
                            val game = SkillMaster.INSTANCE.gameList.getFromUser(user) ?: return@event
                            val onAttack = game as? OnAttack
                            onAttack?.onAttack(user)
                        }
                    }
                }
            }

            event<EntityDamageEvent> {
                val player = it.entity as? Player
                player?.let { pl ->
                    val user = SkillMaster.INSTANCE.userList.get(pl) ?: return@event

                    // 戦闘モードじゃなかったらダメージなし
                    if(user.mode != UserMode.BATTLE) {
                        it.isCancelled = true
                        return@event
                    }

                    if(it.cause == EntityDamageEvent.DamageCause.FALL) {
                        it.isCancelled = true
                        return@event
                    }

                    if(pl.health < 1) { //HPが１より小さい＝＞死んだとき
                        val dead = SkillMaster.INSTANCE.userList.get(pl) ?: return@event

                        //ゲーム中なら
                        if(SkillMaster.INSTANCE.gameList.inGamingUser(dead)) {
                            val game = SkillMaster.INSTANCE.gameList.getFromUser(dead) ?: return@event
                            it.isCancelled = true

                            val onDie = game as? OnDie
                            onDie?.onDie(dead)
                        }
                    }
                }
            }

            event<PlayerSwapHandItemsEvent> {
                val user = SkillMaster.INSTANCE.userList.get(it.player) ?: return@event

                if(user.mode == UserMode.UNARMED) return@event

                if(user.handItem.type == Material.WOODEN_SWORD) {
                    it.isCancelled = true
                    user.buttonInput(SkillButton.SWAP)
                }
            }

            event<PlayerDropItemEvent> {
                val user = SkillMaster.INSTANCE.userList.get(it.player) ?: return@event

                if(user.mode == UserMode.UNARMED) return@event

                if(it.itemDrop.itemStack.type == Material.WOODEN_SWORD) {
                    // 空中を見ていたら(視線の先にブロックがなければ)
                    if(user.player.getTargetBlock(5) != null) {
                        leftFlag = true
                    }

                    it.isCancelled = true
                    user.buttonInput(SkillButton.DROP, Weapon.fromItemStack(it.itemDrop.itemStack))
                }
            }

            event<InventoryClickEvent>(priority = EventPriority.HIGHEST) {
                val player = it.whoClicked as? Player
                player?.let { pl ->
                    val user = SkillMaster.INSTANCE.userList.get(pl) ?: return@event

                    if(it.clickedInventory == null) return@event

                    //プレイヤーインベントリの処理
                    if(it.clickedInventory?.type == InventoryType.PLAYER) {
                        if(user.mode == UserMode.UNARMED) return@event

                        val inv = user.userInventory

                        inv.onClick(it)
                    }

                    //チェストインベントリの処理
                    if(it.clickedInventory?.type == InventoryType.CHEST) {
                        val inv = user.openedInventory ?: return@event

                        if(it.clickedInventory != inv.inv) return@event

                        inv.onClick(it)
                    }
                }
            }
        }
    }
}