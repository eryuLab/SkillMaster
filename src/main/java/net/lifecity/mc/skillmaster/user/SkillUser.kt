package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.inventory.UserInventory
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.entity.Player

class SkillUser(
    val player: Player,
    var mode: UserMode = UserMode.TRAINING,
    var userInventory: UserInventory,
    var openedInventory: InventoryFrame? = null,
    var selectedWeapon: Weapon = Weapon.STRAIGHT_SWORD,
    val rightSkillSet: SkillSet = SkillSet(SkillButton.RIGHT),
    val swapSkillSet: SkillSet = SkillSet(SkillButton.SWAP),
    val dropSkillSet: SkillSet = SkillSet(SkillButton.DROP),
    var rightIndex: Int = 0,
    var swapIndex: Int = 0,
    var dropIndex: Int = 0
) {
    init {
        userInventory = UserInventory(this)

        // HPを設定
        player.maxHealth = 40.0
        player.health = 40.0
    }
}