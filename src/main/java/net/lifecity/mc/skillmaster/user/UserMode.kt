package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.UserModeNotFoundException
import net.lifecity.mc.skillmaster.inventory.UserInventory
import java.lang.reflect.TypeVariable

sealed class UserMode(val name: String, val jp: String) {

    companion object {
        fun valueOf(name: String): UserMode {
            val modeArray = arrayOf(Battle, Training, UnArmed)
            for (mode in modeArray) {
                if (mode.name.equals(name, true))
                    return mode
            }
            throw UserModeNotFoundException("user-mode is not found")
        }
    }

    fun onShift(user: SkillUser, mode: UserMode) {
        when (mode) {
            is Battle -> toBattle(user)
            is Training -> toTraining(user)
            is UnArmed -> toUnArmed(user)
        }
    }
    abstract fun toBattle(user: SkillUser)
    abstract fun toTraining(user: SkillUser)
    abstract fun toUnArmed(user: SkillUser)

    protected fun initSkills(user: SkillUser) {
        user.initSkills()
    }
    protected fun initInventory(user: SkillUser) {
        user.userInventory = UserInventory(user)
    }
    protected fun initHP(user: SkillUser) {
        user.player.maxHealth = 40.0
        user.player.health = 40.0
    }
    protected fun clearInventory(user: SkillUser) {
        for (index in 9..35) {
            user.player.inventory.clear(index)
        }
    }

    object Battle : UserMode("Battle", "バトル") {
        override fun toBattle(user: SkillUser) {}
        override fun toTraining(user: SkillUser) = initSkills(user)
        override fun toUnArmed(user: SkillUser) = clearInventory(user)
    }
    object Training : UserMode("Training", "トレーニング") {
        override fun toBattle(user: SkillUser) = initSkills(user)
        override fun toTraining(user: SkillUser) {}
        override fun toUnArmed(user: SkillUser) = clearInventory(user)
    }
    object UnArmed : UserMode("UnArmed", "武装解除") {
        override fun toBattle(user: SkillUser) {
            initSkills(user)
            initInventory(user)
            initHP(user)
        }
        override fun toTraining(user: SkillUser) {
            initSkills(user)
            initInventory(user)
            initHP(user)
        }
        override fun toUnArmed(user: SkillUser) {}
    }
}