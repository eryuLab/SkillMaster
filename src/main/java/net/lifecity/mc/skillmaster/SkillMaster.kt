package net.lifecity.mc.skillmaster

import dev.jorel.commandapi.CommandAPI
import lombok.Getter
import net.lifecity.mc.skillmaster.EventListener.register
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
    @Getter
    private var userList: SkillUserList? = null

    @Getter
    private var stageList: GameStageList? = null

    @Getter
    private var gameList: GameList? = null

    override fun onEnable() {
        instance = this
        CommandAPI.registerCommand(SkillCommand::class.java)
        register()
        userList = SkillUserList()
        stageList = GameStageList()
        val stage = GameStage("闘技場")
        stage.addField(
            TwoPoint(
                stage,
                Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0),
                Location(Bukkit.getWorlds()[0], 123.0, -25.0, -18.0)
            )
        )
        stageList!!.addStage(stage)
        gameList = GameList()
        val fileUtils = FileUtil()
        fileUtils.init()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        @JvmField
        var instance: SkillMaster? = null
    }
}