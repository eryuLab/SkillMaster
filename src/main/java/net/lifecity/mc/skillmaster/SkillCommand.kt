package net.lifecity.mc.skillmaster

import com.github.syari.spigot.api.item.displayName
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.lifecity.mc.skillmaster.game.games.Duel
import net.lifecity.mc.skillmaster.game.games.Training
import net.lifecity.mc.skillmaster.inventory.SkillInventory
import net.lifecity.mc.skillmaster.inventory.WeaponInventory
import net.lifecity.mc.skillmaster.user.mode.UserMode
import net.lifecity.mc.skillmaster.utils.Messenger
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor.*
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object SkillCommand {

    val helpMsgList = arrayOf(
        "======== ${RED}Skill${WHITE}-${YELLOW}Command${WHITE}-${BLUE}Help${WHITE} ========",
        "/skill weapon [武器] -> 武器を取得します。",
        "/skill item [アイテム] -> 専用のアイテムを取得します。",
        "/skill mode [モード] -> 自身のモードを変更します。",
        "/skill menu [メニュー] -> メニューを開きます。",
        "/skill game [ゲーム] [マップ] [プレイヤー]... -> ゲームを開始します。",
        "すべてプレイヤー用のコマンドです。コンソールからは入力しないでください。",
        "================"
    )

    val weaponCommands = CommandAPICommand("weapon")
        .withArguments(MultiLiteralArgument("直剣", "短剣", "大剣", "太刀", "刺剣", "槌矛"))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            val name = args[0] as String
            try {
                val weapon = Weapon.fromJP(name)
                player.inventory.addItem(weapon.toItemStack())
            } catch (e: WeaponConvertException) {
                Messenger.sendError(player, "${name}という武器が見つからない、または変換できません")
            }
        })

    val itemCommands = CommandAPICommand("item")
        .withSubcommand(CommandAPICommand("メニュー棒")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                val stick = ItemStack(Material.STICK)
                stick.displayName = "メニュー棒"

                player.inventory.addItem(stick)
                Messenger.sendLog(player, "メニュー棒を付与しました")
            })
        )
        .withSubcommand(CommandAPICommand("設定用ツール")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                val tool = ItemStack(Material.AMETHYST_SHARD)
                tool.displayName = "設定用ツール"

                player.inventory.addItem(tool)
                Messenger.sendLog(player, "設定用ツールを付与しました")
            })
        )

    val modeCommands = CommandAPICommand("mode")
        .withArguments(MultiLiteralArgument("battle", "training", "unarmed"))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            val name = args[0] as String
            val user = SkillMaster.INSTANCE.userList[player]
            val mode = UserMode.valueOf(name.uppercase())

            user.let {
                it.mode = mode
                Messenger.sendLog(player, "モードを${mode.jp}に変更しました")
            }
        })

    val menuCommands = CommandAPICommand("menu")
        .withSubcommand(CommandAPICommand("skill")
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val user = SkillMaster.INSTANCE.userList[player]

                if (player.gameMode == GameMode.CREATIVE) {
                    Messenger.sendAlert(user.player, "クリエイティブ時のメニューの挙動は補償されていません。")
                }

                user.openedInventory = SkillInventory(user, page = 0)
                user.openedInventory?.open()
            })
        )
        .withSubcommand(CommandAPICommand("weapon")
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val user = SkillMaster.INSTANCE.userList[player]

                if (player.gameMode == GameMode.CREATIVE) {
                    Messenger.sendAlert(user.player, "クリエイティブ時のメニューの挙動は補償されていません。")
                }

                user.openedInventory = WeaponInventory(user, page = 0)
                user.openedInventory?.open()
            })
        )


    val gameTrainingCommand = CommandAPICommand("training")
        .withArguments(MultiLiteralArgument("闘技場"))
        .withArguments(PlayerArgument("player"))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            val stageName = args[0] as String
            val gamePlayer = args[1] as Player

            // ユーザー取得
            val user = SkillMaster.INSTANCE.userList[gamePlayer]

            // プレイヤーがゲーム中か確認
            if (SkillMaster.INSTANCE.gameList.inGamingUser(user)) {
                Messenger.sendError(player, "${gamePlayer.name}はすでにゲーム中です")
                return@PlayerCommandExecutor
            }

            // ステージ取得
            try {
                val stage = SkillMaster.INSTANCE.stageList.getFromName(stageName)

                //ステージが使用中であるか確認
                if (stage.inUsing()) {
                    Messenger.sendError(player, "現在${stageName}は使用中です")
                    return@PlayerCommandExecutor
                }
                val training = Training(stage, user)
                training.gameManager.start()
            } catch (e: StageNotFoundException) {
                Messenger.sendError(player, "${stageName}は登録されていません")
                return@PlayerCommandExecutor
            }

        })

    val gameDuelCommand = CommandAPICommand("duel")
        .withArguments(MultiLiteralArgument("闘技場"))
        .withArguments(PlayerArgument("player1"))
        .withArguments(PlayerArgument("player2"))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            val stageName = args[0] as String
            val player1 = args[1] as Player
            val player2 = args[2] as Player

            //プレイヤー引数確認
            if (player1 == player2) {
                Messenger.sendError(player, "一人で戦うことはできません")
                return@PlayerCommandExecutor
            }

            //ユーザー取得
            val user1 = SkillMaster.INSTANCE.userList[player1]
            val user2 = SkillMaster.INSTANCE.userList[player2]

            //ゲームがないか確認
            if (SkillMaster.INSTANCE.gameList.inGamingUser(user1)) {
                Messenger.sendError(player, "${player1.name}はすでにゲーム中です")
                return@PlayerCommandExecutor
            }
            if (SkillMaster.INSTANCE.gameList.inGamingUser(user2)) {
                Messenger.sendError(player, "${player2.name}はすでにゲーム中です")
                return@PlayerCommandExecutor
            }

            //ステージ取得
            try {
                val stage =
                    SkillMaster.INSTANCE.stageList.getFromName(stageName)

                //ステージが使用中であるか確認
                if (stage.inUsing()) {
                    Messenger.sendError(player, "現在${stageName}は使用中です")
                    return@PlayerCommandExecutor
                }

                //ゲーム開始
                val duel = Duel(stage, user1, user2)
                duel.gameManager.start()

            } catch (e: StageNotFoundException) {
                Messenger.sendError(player, "${stageName}は登録されていません")
                return@PlayerCommandExecutor
            }
        })


    val gameCommands = CommandAPICommand("game")
        .withSubcommands(gameTrainingCommand, gameDuelCommand)


    fun register() {
        CommandAPICommand("skill")
            .withSubcommands(weaponCommands, itemCommands, modeCommands, menuCommands, gameCommands)
            .executes(CommandExecutor { sender, _ ->
                helpMsgList.forEach { sender.sendMessage(it) }
            })
            .register()
    }
}
