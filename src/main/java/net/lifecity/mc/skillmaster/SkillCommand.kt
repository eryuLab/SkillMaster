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
import net.lifecity.mc.skillmaster.utils.Messager
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor.*
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object SkillCommand {

    val helpMsgList = arrayOf(
        "======== " + RED + "Skill" + WHITE + "-" + YELLOW + "Command" + WHITE + "-" + BLUE + "Help" + WHITE + " ========",
        "/skill weapon [武器] -> 武器を取得します。",
        "/skill mode [モード] -> 自身のモードを変更します。",
        "/skill menu [メニュー] -> メニューを開きます。",
        "/skill duel [マップ] [プレイヤー1] [プレイヤー2] -> マップと2人のプレイヤーを指定してデュエルゲームを開始します。",
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
                player.sendMessage("${name}という武器が見つからない、または変換できません")
            }
        })

    val modeCommands = CommandAPICommand("mode")
        .withArguments(MultiLiteralArgument("battle", "training", "unarmed"))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            val name = args[0] as String
            val user = SkillMaster.INSTANCE.userList[player]
            val mode = UserMode.valueOf(name.uppercase())

            user.let {
                it.mode = mode
                player.sendMessage("モードを${mode.jp}に変更しました")
            }
        })

    val menuCommands = CommandAPICommand("menu")
        .withArguments(MultiLiteralArgument("skill", "weapon"))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            if (args.isEmpty()) {
                val stick = ItemStack(Material.STICK)
                stick.displayName = "メニュー棒"

                player.inventory.addItem(stick)
                player.sendMessage("メニュー棒を付与しました")
            } else {
                val menu = args[0] as String
                val user = SkillMaster.INSTANCE.userList[player]

                if (player.gameMode == GameMode.CREATIVE) {
                    Messager.sendAlert(user.player, "クリエイティブ時のメニューの挙動は補償されていません。")
                }

                user.let {
                    when(menu) {
                        "skill" -> {
                            it.openedInventory = SkillInventory(user, page = 0)

                            it.openedInventory?.open()
                        }
                        "weapon" -> {
                            it.openedInventory = WeaponInventory(it)

                            it.openedInventory?.open()
                        }

                        else -> {}
                    }
                }
            }

        })

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
                player.sendMessage("${player.name}はすでにゲーム中です")
                return@PlayerCommandExecutor
            }

            // ステージ取得
            try {
                val stage = SkillMaster.INSTANCE.stageList.getFromName(stageName)

                //ステージが使用中であるか確認
                if (stage.inUsing()) {
                    player.sendMessage("${stageName}は使用中です")
                    return@PlayerCommandExecutor
                }
                val training = Training(stage, user)
                training.gameManager.start()
            } catch (e: StageNotFoundException) {
                player.sendMessage("${stageName}は登録されていません")
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
                player.sendMessage("一人で戦うことはできません")
                return@PlayerCommandExecutor
            }

            //ユーザー取得
            val user1 = SkillMaster.INSTANCE.userList[player1]
            val user2 = SkillMaster.INSTANCE.userList[player2]

            //ゲームがないか確認
            if (SkillMaster.INSTANCE.gameList.inGamingUser(user1)) {
                player.sendMessage("${player1.name}はすでにゲーム中です")
                return@PlayerCommandExecutor
            }
            if (SkillMaster.INSTANCE.gameList.inGamingUser(user2)) {
                player.sendMessage("${player2.name}はすでにゲーム中です")
                return@PlayerCommandExecutor
            }

            //ステージ取得
            try {
                val stage =
                    SkillMaster.INSTANCE.stageList.getFromName(stageName)

                //ステージが使用中であるか確認
                if (stage.inUsing()) {
                    player.sendMessage("${stageName}は使用中です")
                    return@PlayerCommandExecutor
                }

                //ゲーム開始
                val duel = Duel(stage, user1, user2)
                duel.gameManager.start()

            } catch (e: StageNotFoundException) {
                player.sendMessage("${stageName}は登録されていません")
                return@PlayerCommandExecutor
            }
        })


    val gameCommands = CommandAPICommand("game")
        .withSubcommands(gameTrainingCommand, gameDuelCommand)




    fun register() {
        CommandAPICommand("skill")
            .withSubcommands(weaponCommands, modeCommands, menuCommands, gameCommands)
            .executes(CommandExecutor { sender, _ ->
                helpMsgList.forEach { sender.sendMessage(it) }
            })
            .register()
    }
}
