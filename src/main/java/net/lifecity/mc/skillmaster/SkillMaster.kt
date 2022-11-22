package net.lifecity.mc.skillmaster

import net.lifecity.mc.skillmaster.game.GameList
import net.lifecity.mc.skillmaster.game.GameType
import net.lifecity.mc.skillmaster.game.stage.GameStage
import net.lifecity.mc.skillmaster.game.stage.GameStageList
import net.lifecity.mc.skillmaster.user.SkillUserList
import net.lifecity.mc.skillmaster.utils.FileUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class SkillMaster : JavaPlugin() {

    companion object {
        internal lateinit var INSTANCE : SkillMaster

        const val PLUGIN_NAME = "SkillMaster"
    }

    init {
        INSTANCE = this
    }

    lateinit var userList: SkillUserList

    lateinit var stageList: GameStageList

    lateinit var gameList: GameList

    var consoleLocList = mutableListOf<Location>()

    override fun onEnable() {
        SkillCommand.register()

        EventListener.register()

        userList = SkillUserList()
        stageList = GameStageList()

        val stage = GameStage("闘技場", -25)
        stage.addField(
            GameType.TRAINING,
            listOf(Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0)),
            listOf()
        )
        stage.addField(
            GameType.DUEL,
            listOf(
                Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0),
                Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0)
            ),
            listOf()
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