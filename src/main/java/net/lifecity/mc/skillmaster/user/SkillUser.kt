package net.lifecity.mc.skillmaster.user

import com.github.syari.spigot.api.sound.playSound
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.title.Title
import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.inventory.UserInventory
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.function.AdditionalInput
import net.lifecity.mc.skillmaster.skill.function.Defense
import net.lifecity.mc.skillmaster.user.mode.ModeManager
import net.lifecity.mc.skillmaster.user.mode.UserMode
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.user.skillset.SkillCard
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class SkillUser(
    val player: Player,
    var openedInventory: InventoryFrame? = null,
    val rightCard: SkillCard = SkillCard(SkillButton.RIGHT),
    val swapCard: SkillCard = SkillCard(SkillButton.SWAP),
    val dropCard: SkillCard = SkillCard(SkillButton.DROP)
) {
    private val modeManager: ModeManager = ModeManager(this)
    var mode: UserMode
        get() = modeManager.mode
        set(value) = modeManager.shift(value)

    var selectedWeapon = Weapon.STRAIGHT_SWORD
        set(value) {
            // スキルセットをリセット
            val skillSetArray = arrayOf(rightCard.skillSet, swapCard.skillSet, dropCard.skillSet)
            for (skillSet in skillSetArray)
                skillSet.clean()

            field = value
        }
    val handItem
        get() = player.inventory.itemInMainHand
    private val handWeapon
        get() = Weapon.fromItemStack(handItem)

    init {
        // HPを設定
        player.maxHealth = 40.0
        player.health = 40.0
        // スキル設定
    }

    var userInventory: UserInventory = UserInventory(this)

    /**
     * 左クリックを入力した時の処理
     * 発動中のスキルの解除と、自身のベクトルを0にする
     */
    fun leftClick() {
        getActivatedSkill()?.let {
            if(it.canCancel) { //もしスキル解除可能だったら
                it.deactivate() // 発動中のスキルを解除

                // プレイヤーのベクトルを0にする
                player.velocity = Vector(0.0, player.velocity.y, 0.0)
            }
        }
    }

    /**
     * 発動中のスキルを返します
     * @return 発動中のスキル
     */
    fun getActivatedSkill(): CompositeSkill? {
        // スキルセットの配列を作成
        val skillSetArray = arrayOf(rightCard.skillSet, swapCard.skillSet, dropCard.skillSet)

        // 配列で繰り返し
        for (skillSet in skillSetArray) {

            // スキルセットのスキルリストで繰り返し
            keyList@ for (skillKey in skillSet.keyList) {
                // スキルがnullだったらcontinue
                val skill: Skill = skillKey.skill ?: continue@keyList

                // スキルが複合スキルのとき発動中か確認
                if (skill is CompositeSkill) {
                    if (skill.activated)
                        return skill
                }
            }
        }
        return null
    }

    fun settable(skill: Skill): Boolean {
        // スキルセットの配列を作成
        val skillSetArray = arrayOf(rightCard.skillSet, swapCard.skillSet, dropCard.skillSet)

        for (skillSet in skillSetArray) {
            keyList@for (skillKey in skillSet.keyList) {
                if (skillKey.skill == null)
                   continue@keyList
                if (skillKey.skill!!.match(skill))
                    return false
            }
        }
        return true
    }

    /**
     * スキルボタンを入力した時の処理
     * スキルの発動、追加入力、またはスキルセット番号の変更
     * @param button スキルボタン
     */
    fun buttonInput(button: SkillButton, weapon: Weapon? = handWeapon) {
        // スキルカード特定
        val card: SkillCard = when (button) {
            SkillButton.RIGHT -> rightCard
            SkillButton.SWAP -> swapCard
            SkillButton.DROP -> dropCard
        }

        // シフトが押されているときスキルセット番号変更
        if (player.isSneaking) {
            val size = card.skillSet.containedSize()
            // スキルセットが0のとき変更なし
            if (size == 0) {
                player.sendMessage("セットされているスキルがありません")
                return
            }
            // スキルセットが1のとき
            if (size == 1) {
                // 現在のスキルがあるとき変更なし
                if (card.now() != null) {
                    player.sendMessage("セットされているスキルが少ないため変更できません")
                    return
                }
            }
            // 現在のスキルがないのとき変更
            // スキルセットが2以上のとき次のスキルがあるまで変更

            // セット番号の変更
            card.index++
            // SE再生
            player.location.playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW)
            // ログ出力
            val skill: Skill = card.now()!!
            player.sendMessage("${card.button.jp}[${card.index}]を「${skill.name}」に変更しました")

            // インターバルアイテムの更新
            userInventory.updateInterval(card.nowKey())
        }
        else {
            // スキルを入力
            skillInput(card.now(), weapon)

            // インターバルアイテムの更新
            userInventory.updateInterval(card.nowKey())
        }
    }

    /**
     * スキルを発動、または追加入力する
     * @param skill スキル
     * @param weapon 手に持っている武器
     */
    private fun skillInput(skill: Skill?, weapon: Weapon?) {
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
        if (skill.inInterval)
            return

        // 複合スキルのとき
        if (skill is CompositeSkill) {

            // 現在の発動中スキルを取得
            val activatedSkill: CompositeSkill? = getActivatedSkill()


            // 複合スキル発動中のとき
            if (activatedSkill != null) {
                // 発動しようとしてるスキルが違う場合
                // かつスキルキャンセル可能だったらキャンセルし、スキル発動
                if (activatedSkill != skill && activatedSkill.canCancel) {
                    activatedSkill.deactivate()
                    skill.activate()
                    return
                    // スキルキャンセル不可だったら操作不能
                }
                // 同じスキルのとき追加入力
                else {
                    if (skill.activated) {
                        if (skill is AdditionalInput) {
                            skill.additionalInput()
                            return
                        }
                        return
                    }
                }
            }
        }
        // 単発スキルのとき
        skill.activate()
    }

    /**
     * このSkillUserへの攻撃を試みます
     * ただし、防御されるかもしれません
     * 加算によってベクトルが与えられます
     * @param damage 攻撃の威力
     * @param vector ノックバック
     */
    fun damageAddVector(damage: Double = 0.0, vector: Vector = Vector(0.0, 0.0, 0.0), noDefense: Boolean = false) {
        if (noDefense || !canDefense(damage, vector)) {
            // ダメージとノックバックを与える
            player.damage(damage)
            player.velocity.add(vector)
        }
    }

    /**
     * このSkillUserへの攻撃を試みます
     * ただし、防御されるかもしれません
     * @param damage 攻撃の威力
     * @param vector ノックバック
     */
    fun damageChangeVector(damage: Double = 0.0, vector: Vector, noDefense: Boolean = false) {
        if (noDefense || !canDefense(damage, vector)) {
            // ダメージとノックバックを与える
            player.damage(damage)
            player.velocity = vector
        }
    }

    /**
     * 防御スキルがあれば防御します
     * @param damage 攻撃の威力
     * @param vector ノックバック
     */
    private fun canDefense(damage: Double, vector: Vector): Boolean {
        // 防御スキル取得
        val activatedSkill: CompositeSkill? = getActivatedSkill()

        // 防御スキルがあれば防御
        if (activatedSkill != null) {
            if (activatedSkill is Defense) {
                activatedSkill.defense(damage, vector)
                return true
            }
        }
        return false
    }

    fun sendMessage(msg: String) = player.sendMessage(msg)
    fun sendActionBar(msg: String) = player.sendActionBar(PlainTextComponentSerializer.plainText().deserialize(msg))
    fun sendTitle(title: String, sub: String) = player.showTitle(
        Title.title(
            PlainTextComponentSerializer.plainText().deserialize(title),
            PlainTextComponentSerializer.plainText().deserialize(sub)
        )
    )
    fun playSound(sound: Sound) = player.location.playSound(sound)
    fun teleport(location: Location) = player.teleport(location)
}