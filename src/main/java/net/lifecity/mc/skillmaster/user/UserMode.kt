package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.inventory.UserInventory

sealed class UserMode(val name: String, val user: SkillUser) {

    fun onShift(mode: UserMode) {
        when (mode) {
            is Battle -> toBattle()
            is Training -> toTraining()
            is UnArmed -> toUnArmed()
        }
    }
    abstract fun toBattle()
    abstract fun toTraining()
    abstract fun toUnArmed()

    protected fun initSkills() {
        user.initSkills()
    }
    protected fun initInventory() {
        user.userInventory = UserInventory(user)
    }
    protected fun initHP() {
        user.player.maxHealth = 40.0
        user.player.health = 40.0
    }
    protected fun clearInventory() {
        for (index in 9..35) {
            user.player.inventory.clear(index)
        }
    }

    class Battle(user: SkillUser): UserMode("バトル", user) {
        override fun toBattle() {}
        override fun toTraining() = initSkills()
        override fun toUnArmed() = clearInventory()
    }
    class Training(user: SkillUser): UserMode("トレーニング", user) {
        override fun toBattle() = initSkills()
        override fun toTraining() {}
        override fun toUnArmed() = clearInventory()
    }
    class UnArmed(user: SkillUser): UserMode("武装解除", user) {
        override fun toBattle() {
            initSkills()
            initInventory()
            initHP()
        }
        override fun toTraining() {
            initSkills()
            initInventory()
            initHP()
        }
        override fun toUnArmed() {}
    }
}