package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.inventory.UserInventory
import net.lifecity.mc.skillmaster.skill.SeparatedSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.user.skillset.SkillCard
import net.lifecity.mc.skillmaster.user.skillset.SkillSet
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class SkillUser(
    val player: Player,
    var userInventory: UserInventory,
    var openedInventory: InventoryFrame? = null,
    val rightCard: SkillCard = SkillCard(SkillButton.RIGHT),
    val swapCard: SkillCard = SkillCard(SkillButton.SWAP),
    val dropCard: SkillCard = SkillCard(SkillButton.DROP)
) {
    var mode: UserMode = UserMode.TRAINING
    set(value) {
        // バトルからトレーニング
        if (mode == UserMode.BATTLE && mode == UserMode.TRAINING) {
            // 稼働中のスキルの初期化
        }
    }
    var selectedWeapon: Weapon = Weapon.STRAIGHT_SWORD
    set(value) {
        // スキルセットをリセット
        val skillSetArray: Array<SkillSet> = arrayOf(rightCard.skillSet, swapCard.skillSet, dropCard.skillSet)
        for (skillSet in skillSetArray)
            skillSet.clean()

        field = value
    }
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
     * スキルボタンを入力した時の処理
     * スキルの発動、追加入力、またはスキルセット番号の変更
     */
    fun buttonInput(button: SkillButton) {
        // スキルカード特定
        val card: SkillCard = when(button) {
            SkillButton.RIGHT -> rightCard
            SkillButton.SWAP -> swapCard
            SkillButton.DROP -> dropCard
        }

        // シフトが押されているときスキルセット番号変更
        if (player.isSneaking) {
            // セット番号の変更
            card.index++

            // SE再生
            //playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW)

            // ログ出力
            val skill: Skill? = card.now()
            if (skill == null)
                player.sendMessage("${card.button.jp}[${card.index}]はスキルがセットされていません")
            else
                player.sendMessage("${card.button.jp}[${card.index}]を「${skill.name}」に変更しました")
        }
    }

    private fun skillInput(skill: Skill?, weapon: Weapon) {
        // スキルが存在するか
        if (skill == null) {
            player.sendMessage("スキルがセットされていません")
            return
        }

        // 持っている武器を確認
        if (!skill.usable(weapon)) {
            player.sendMessage("この武器ではこのスキルを使用できません")
            return
        }

        // インターバル確認
        if (!skill.isInInterval)
            return

        // 複合スキルのとき
        if (skill is SeparatedSkill) {

            // 現在の発動中スキルを取得
            val activatedSkill: SeparatedSkill? = getActivatedSkill()

            // 発動中スキルが発動しようとしてるスキルと同一でなければスキル解除
            if (activatedSkill != null) {
                if (activatedSkill != skill)
                    activatedSkill.deactivate()
            }

            // 発動中だったら追加入力
            if (skill.isActivated) {
                skill.additionalInput()
                return
            }
        }

        skill.activate()
    }

    /**
     * セット内のスキルを初期化
     */
    fun initSkills() {
        val skillSetArray: Array<SkillSet> = arrayOf(rightCard.skillSet, swapCard.skillSet, dropCard.skillSet)

        for (skillSet in skillSetArray) {
            for (skillKey in skillSet.keyList) {
                skillKey.skill?.init()
            }
        }
    }
}