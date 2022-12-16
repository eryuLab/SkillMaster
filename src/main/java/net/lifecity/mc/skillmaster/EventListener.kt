package net.lifecity.mc.skillmaster

import com.github.syari.spigot.api.event.events
import com.github.syari.spigot.api.item.displayName
import com.github.syari.spigot.api.sound.playSound
import net.kyori.adventure.text.Component
import net.lifecity.mc.skillmaster.inventory.SkillInventory
import net.lifecity.mc.skillmaster.user.UserEffect
import net.lifecity.mc.skillmaster.user.mode.UserMode
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.utils.Messenger
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
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
                val user = SkillMaster.INSTANCE.userList[it.player]
                UserEffect(user).onSpawn()
            }

            event<PlayerQuitEvent> {
                val user = SkillMaster.INSTANCE.userList[it.player]
                SkillMaster.INSTANCE.skillSetConfig.onPlayerQuit(user)
                try {
                    val game = SkillMaster.INSTANCE.gameList.getFromUser(user)

                    game.onUserLogout(user)

                } catch (e: GameNotFoundException) {
                    return@event
                }
                SkillMaster.INSTANCE.userList.remove(it.player)
            }

            event<PlayerInteractEvent> {
                val user = SkillMaster.INSTANCE.userList[it.player]

                // 手のアイテムがメニュー棒のとき
                if (user.handItem.type == Material.STICK) {
                    if (user.handItem.displayName == "メニュー棒") {
                        if(user.player.gameMode == GameMode.CREATIVE) {
                            Messenger.sendAlert(user.player, "クリエイティブ時のメニューの挙動は補償されていません。")
                        }

                        user.openedInventory = SkillInventory(user,  page = 0)
                        user.openedInventory?.open()
                        return@event
                    }
                }

                // 手のアイテムが設定用ツールのとき
                if (user.handItem.type == Material.AMETHYST_SHARD) {
                    if (user.handItem.displayName == "設定用ツール") {
                        if (it.action == Action.RIGHT_CLICK_BLOCK) {
                            // コンソール追加
                            val state = it.interactionPoint?.block?.state
                            if (state is Sign) {
                                // すでに登録されているか確認
                                if (SkillMaster.INSTANCE.signList.contains(state)) {
                                    Messenger.sendLog(it.player, "すでに登録されています")
                                    return@event
                                }

                                // テキスト編集
                                state.line(0, Component.text("[ GameConsole ]"))
                                state.line(1, Component.text("Type: none"))
                                state.line(2, Component.text("Stage: none"))
                                state.line(3, Component.text("State: none"))
                                state.update()

                                // 音
                                it.player.location.playSound(Sound.ENTITY_ARROW_SHOOT, pitch = 0.7f)

                                // 登録
                                SkillMaster.INSTANCE.signList.add(state)

                                // ログ
                                Messenger.sendLog(it.player, "ゲームコンソールを実装しました")
                                return@event
                            }
                        }
                    }
                }

                // ゲームの看板をクリックしたときの処理
                if (it.action == Action.LEFT_CLICK_BLOCK || it.action == Action.RIGHT_CLICK_BLOCK) {
                    val loc = it.interactionPoint ?: return@event

                    if (loc.block.state is Sign) {
                        val sign = loc.block.state as Sign

                        if (SkillMaster.INSTANCE.signList.contains(sign)) {
                            it.player.sendMessage("ゲームコンソールにアクセス")
                            return@event
                        }
                    }
                }

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

            event<PlayerInteractEntityEvent> {
                // 特定のタグのついた村人をクリックしたときスキルメニューを開く
                // エンティティが村人のとき
                val entity = it.rightClicked
                if (entity is Villager) {
                    if ("skill_console" in entity.scoreboardTags) {
                        val user = SkillMaster.INSTANCE.userList[it.player]

                        user.openedInventory = SkillInventory(user,  page = 0)
                        user.openedInventory?.open()
                        return@event
                    }
                }
            }

            event<BlockBreakEvent> {
                // ブロックがゲームの看板であるとき
                if (it.block.state is Sign) {
                    val sign = it.block.state as Sign

                    // 登録されているか確認
                    if (!SkillMaster.INSTANCE.signList.contains(sign))
                        return@event

                    val handItem = it.player.inventory.itemInMainHand
                    if (handItem.type != Material.AMETHYST_SHARD || handItem.displayName != "設定用ツール") {
                        it.isCancelled = true
                        return@event
                    }
                    // 音
                    it.player.location.playSound(Sound.ITEM_CROSSBOW_SHOOT, pitch = 0.7f)

                    // 削除
                    SkillMaster.INSTANCE.signList.remove(sign)

                    // ログ
                    Messenger.sendLog(it.player, "ゲームコンソールを削除しました")
                }
            }

            event<EntityDamageByEntityEvent> {
                // プレイヤーが木の剣で攻撃したらイベントキャンセル
                val player = it.damager as? Player
                player?.let { pl ->
                    val user = SkillMaster.INSTANCE.userList[pl]

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

                            try {
                                // ゲーム中ならonAttack()呼び出し
                                val game = SkillMaster.INSTANCE.gameList.getFromUser(user)
                                game.onUserAttack(user)
                            } catch (e: GameNotFoundException) {
                                return@event
                            }
                        }
                    }
                }
            }

            event<EntityDamageEvent> {
                val player = it.entity as? Player
                player?.let { pl ->
                    val user = SkillMaster.INSTANCE.userList[pl]

                    // 戦闘モードじゃなかったらダメージなし
                    if(user.mode != UserMode.BATTLE) {
                        it.isCancelled = true
                        return@event
                    }

                    if(it.cause == EntityDamageEvent.DamageCause.FALL) {
                        it.isCancelled = true
                        return@event
                    }

                    if(pl.health - it.damage <= 0) { //HPが０以下＝＞死んだとき
                        val dead = SkillMaster.INSTANCE.userList[pl]

                        //ゲーム中なら
                        try {
                            val game = SkillMaster.INSTANCE.gameList.getFromUser(dead)
                            it.isCancelled = true
                            game.onUserDead(dead)
                            UserEffect(dead).onDead()
                        } catch (e: GameNotFoundException) {
                            return@event
                        }
                    }
                }
            }

            event<PlayerSwapHandItemsEvent> {
                val user = SkillMaster.INSTANCE.userList[it.player]

                if(user.mode == UserMode.UNARMED) return@event

                if(user.handItem.type == Material.WOODEN_SWORD) {
                    it.isCancelled = true
                    user.buttonInput(SkillButton.SWAP)
                }
            }

            event<PlayerDropItemEvent> {
                val user = SkillMaster.INSTANCE.userList[it.player]

                if(user.mode == UserMode.UNARMED) return@event

                if(it.itemDrop.itemStack.type == Material.WOODEN_SWORD) {
                    // 空中を見ていたら(視線の先にブロックがなければ)
                    if(user.player.getTargetBlock(5) != null) {
                        leftFlag = true
                    }

                    it.isCancelled = true
                    user.buttonInput(SkillButton.DROP, Weapon.fromItemStack(it.itemDrop.itemStack))
                }
                // インターバルアイテムの確認
                val item = it.itemDrop.itemStack
                val map = mapOf(
                    Material.YELLOW_DYE to SkillButton.RIGHT.jp,
                    Material.LIGHT_BLUE_DYE to SkillButton.SWAP.jp,
                    Material.PINK_DYE to SkillButton.DROP.jp
                )
                for (button in SkillButton.values()) {
                    if (button.material == item.type && button.jp == item.displayName) {
                        it.isCancelled = true
                        break
                    }
                }
            }

            event<InventoryClickEvent>(priority = EventPriority.HIGHEST) {
                val player = it.whoClicked as? Player
                player?.let { pl ->
                    val user = SkillMaster.INSTANCE.userList[pl]

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