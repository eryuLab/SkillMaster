package net.lifecity.mc.skillmaster.user

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.inventory.UserInventory
import net.lifecity.mc.skillmaster.playSound
import net.lifecity.mc.skillmaster.skill.DefenseSkill
import net.lifecity.mc.skillmaster.skill.SeparatedSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.user.skillset.SkillCard
import net.lifecity.mc.skillmaster.user.skillset.SkillSet
import net.lifecity.mc.skillmaster.utils.EntityDistanceSort
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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
        if (mode == UserMode.BATTLE && value == UserMode.TRAINING) {
            // 稼働中のスキルの初期化
            initSkills()
        }
        // トレーニングからバトル
        else if (mode == UserMode.TRAINING && value == UserMode.BATTLE) {
            // 稼働中のスキルの初期化
                initSkills()
        }
        // 武装解除からバトル、トレーニング
        else if (mode == UserMode.UNARMED && (value == UserMode.BATTLE || value == UserMode.TRAINING)) {
            // インベントリの初期化
            //userInventory = UserInventory(this)

            // HPの初期化
            player.maxHealth = 40.0
            player.health = 40.0
        }
        field = value
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
     * @param button スキルボタン
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
            player.playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW)

            // ログ出力
            val skill: Skill? = card.now()
            if (skill == null)
                player.sendMessage("${card.button.jp}[${card.index}]はスキルがセットされていません")
            else
                player.sendMessage("${card.button.jp}[${card.index}]を「${skill.name}」に変更しました")
        }
    }

    /**
     * スキルを発動、または追加入力する
     * @param skill スキル
     * @param weapon 手に持っている武器
     */
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

    /**
     * このSkillUserへの攻撃を試みます
     * ただし、防御されるかもしれません
     * @param damage 攻撃の威力
     * @param vector ノックバックの力
     */
    private fun damage(damage: Double, vector: Vector) {
        // 防御スキル取得
        val activatedSkill: SeparatedSkill? = getActivatedSkill()

        // 防御スキルがあれば防御
        if (activatedSkill != null) {
            if (activatedSkill is DefenseSkill) {
                activatedSkill.defense(damage, vector)
                return
            }
        }

        // ダメージとノックバックを与える
        player.damage(damage)
        player.velocity.add(vector)
    }

    /**
     * 指定したユーザーを攻撃します
     * @param user 指定したユーザー
     * @param damage ダメージ
     * @param vector ノックバック
     * @param sound SE
     */
    private fun attackUser(user: SkillUser, damage: Double, vector: Vector, sound: Sound) {
        // SE再生
        player.playSound(sound)

        // トレーニングモード時は攻撃不可
        if (mode == UserMode.TRAINING)
            user.damage(0.0, Vector(0.0, 0.0, 0.0))
        else {
            // ダメージを与える
            user.damage(damage, vector)

            // ゲーム中のときGameのonAttack()を呼び出す
            //val game = SkillMaster.instance.gameList.getFromUser(this)
            //if (game is OnAttack)
                //game.onAttack(this)
        }
    }

    /**
     * 指定したエンティティを攻撃します
     * @param entity 指定したエンティティ
     * @param damage ダメージ
     * @param vector ノックバック
     * @param sound SE
     */
    private fun attackEntity(entity: Entity, damage: Double, vector: Vector, sound: Sound) {
        // SE再生
        player.playSound(sound)

        // トレーニングモード時は攻撃不可
        if (mode == UserMode.TRAINING)
            return

        if (entity is Damageable) {
            // 標的にダメージを与える
            entity.damage(damage)

            // 標的をノックバックさせる
            entity.velocity.add(vector)
        }
    }

    /**
     * 一番近いEntityを攻撃します
     * @param radius この半径以内のエンティティに攻撃します
     * @param damage ダメージ
     * @param vector ノックバック
     * @param sound SE
     * @return 攻撃が成功するとtrueを返します
     */
    fun attackNearest(radius: Double, damage: Double, vector: Vector, sound: Sound): Boolean {
        // 一番近くのエンティティを取得
        val entities = getNearEntities(radius)

        if (entities.isEmpty())
            return false

        val entity = entities[0]

        // プレイヤーだった時の処理
        if (entity is Player) {
            val user = SkillMaster.instance.userList[entity] ?: return false

            // 攻撃
            attackUser(user, damage, vector, sound)
        }
        // プレイヤー以外の時の処理
        else {
            attackEntity(entity, damage, vector, sound)
        }
        return true
    }

    /**
     * このユーザーから近いエンティティを取得します
     * @param radius 検知する範囲の半径
     * @return 近い順のエンティティのリスト
     */
    fun getNearEntities(radius: Double): List<Entity> {
        // 半径radiusで近くのentityのリストを取得
        val near = player.getNearbyEntities(radius, radius, radius)

        // リストを近い順に並べる
        EntityDistanceSort.quicksort(player, near, 0, near.size - 1)

        return near
    }

    /**
     * メインハンドのアイテムを取得します
     * @return メインハンドのアイテム
     */
    fun getHandItem(): ItemStack {
        return player.inventory.itemInMainHand
    }

    /**
     * メインハンドの武器を取得します
     * @return メインハンドの武器
     */
    fun getHandWeapon(): Weapon? {
        return Weapon.fromItemStack(getHandItem())
    }
}