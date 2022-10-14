package net.lifecity.mc.skillmaster

import dev.jorel.commandapi.CommandAPI
import net.lifecity.mc.skillmaster.game.GameList
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.game.stage.GameStageList
import net.lifecity.mc.skillmaster.game.stage.field.TwoPoint
import net.lifecity.mc.skillmaster.user.SkillUserList
import net.lifecity.mc.skillmaster.utils.FileUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class SkillMaster : JavaPlugin() {

    companion object {
        internal lateinit var INSTANCE : SkillMaster
    }

    init {
        INSTANCE = this
    }

    lateinit var userList: SkillUserList

    lateinit var stageList: GameStageList

    lateinit var gameList: GameList

    override fun onEnable() {
       SkillCommand.register()

        EventListener.register()

        userList = SkillUserList()
        stageList = GameStageList()

        val stage = GameStage("闘技場", -25)
        stage.addField(
            TwoPoint(
                stage,
                Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0),
                Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0)
            )
        )

        stageList.addStage(stage)
        gameList = GameList()

        val fileUtils = FileUtil()
        fileUtils.init()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}