package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.inventory.UserInventory

class ModeManager(val user: SkillUser, var mode: UserMode = UserMode.BATTLE) {

    fun shift(to: UserMode) {
        mode.also {from ->
            when (from) {
                UserMode.BATTLE -> {
                    when (to) {
                        UserMode.BATTLE -> return
                        UserMode.TRAINING -> initSkills()
                        UserMode.UNARMED -> clearInv()
                    }
                }
                UserMode.TRAINING -> {
                    when (to) {
                        UserMode.BATTLE -> initSkills()
                        UserMode.TRAINING -> return
                        UserMode.UNARMED -> clearInv()
                    }
                }
                UserMode.UNARMED -> {
                    when (to) {
                        UserMode.BATTLE -> {
                            initSkills()
                            initInv()
                            initHP()
                        }
                        UserMode.TRAINING -> {
                            initSkills()
                            initInv()
                            initHP()
                        }
                        UserMode.UNARMED -> return
                    }
                }
            }
        }
        mode = to
    }

    private fun initSkills() {
        val skillSetArray = arrayOf(user.rightCard.skillSet, user.swapCard.skillSet, user.dropCard.skillSet)

        for (skillSet in skillSetArray) {
            for (skillKey in skillSet.keyList) {
                skillKey.skill?.init()
            }
        }
    }
    private fun initInv() {
        user.userInventory = UserInventory(user)
    }
    private fun initHP() {
        user.player.maxHealth = 40.0
        user.player.health = 40.0
    }
    private fun clearInv() {
        val invStartIdx = 9
        val invEndIdx = 35
        for (index in invStartIdx..invEndIdx) {
            user.player.inventory.clear(index)
        }
    }
}