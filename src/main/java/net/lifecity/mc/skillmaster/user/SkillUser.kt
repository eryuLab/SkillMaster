package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.inventory.UserInventory
import net.lifecity.mc.skillmaster.skill.SeparatedSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.user.skillset.SkillCard
import net.lifecity.mc.skillmaster.user.skillset.SkillSet
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class SkillUser(
    val player: Player,
    var mode: UserMode = UserMode.TRAINING,
    var userInventory: UserInventory,
    var openedInventory: InventoryFrame? = null,
    var selectedWeapon: Weapon = Weapon.STRAIGHT_SWORD,
    val rightCard: SkillCard = SkillCard(SkillButton.RIGHT),
    val swapCard: SkillCard = SkillCard(SkillButton.SWAP),
    val dropCard: SkillCard = SkillCard(SkillButton.DROP)
) {
    init {
        //userInventory = UserInventory(this)

        // HPを設定
        player.maxHealth = 40.0
        player.health = 40.0
    }

    /**
     * 左クリックを入力した時の処理
     * 発動中のスキルの解除と、自身のベクトルを0にする
     */
    fun leftClick() {
        // 発動中のスキルを解除
        getActivatedSkill()?.deactivate()

        // プレイヤーのベクトルを0にする
        player.velocity = Vector(0.0, player.velocity.y, 0.0)
    }

    /**
     * 発動中のスキルを返します
     * @return 発動中のスキル
     */
    fun getActivatedSkill(): SeparatedSkill? {
        // スキルセットの配列を作成
        val skillSetArray: Array<SkillSet> = arrayOf(rightCard.skillSet, swapCard.skillSet, dropCard.skillSet)

        // 配列で繰り返し
        for (skillSet in skillSetArray) {

            // スキルセットのスキルリストで繰り返し
            keyList@for (skillKey in skillSet.keyList) {
                // スキルがnullだったらcontinue
                val skill: Skill = skillKey.skill ?: continue@keyList

                // スキルが複合スキルのとき発動中か確認
                if (skill is SeparatedSkill) {
                    if (skill.isActivated)
                        return skill
                }
            }
        }
        return null
    }

    /**
     * 右クリックを入力した時の処理
     * 右クリックスキルの発動、追加入力、またはスキルセット番号の変更
     */
    fun rightClick() {

    }
}