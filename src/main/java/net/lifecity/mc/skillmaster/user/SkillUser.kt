package net.lifecity.mc.skillmaster.user

import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.game.function.OnAttack
import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.inventory.UserInventory
import net.lifecity.mc.skillmaster.skill.DefenseSkill
import net.lifecity.mc.skillmaster.skill.SeparatedSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.defenseskills.NormalDefense
import net.lifecity.mc.skillmaster.skill.separatedskills.LeafFlow
import net.lifecity.mc.skillmaster.skill.skills.Kick
import net.lifecity.mc.skillmaster.skill.skills.MoveFast
import net.lifecity.mc.skillmaster.skill.skills.VectorAttack
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.user.skillset.SkillSet
import net.lifecity.mc.skillmaster.utils.EntityDistanceSort
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class SkillUser(val player: Player, var mode: UserMode) {

    private val SKILL_SET_SIZE = 3;
    private val sm: SkillManager = SkillManager(this)

    var userInventory: UserInventory = UserInventory(this)
    var openedInventory: InventoryFrame? = null
    var selectedWeapon = Weapon.STRAIGHT_SWORD

    val rightSkillSet = SkillSet(
        SkillButton.RIGHT,
        zero = sm.fromClass(VectorAttack::class.java),
        one = sm.fromClass(LeafFlow::class.java)
    )

    val swapSkillSet = SkillSet(
        SkillButton.SWAP,
        zero = sm.fromClass(MoveFast::class.java)
    )

    val dropSkillSet = SkillSet(
        SkillButton.DROP,
        zero = sm.fromClass(Kick::class.java),
        one = sm.fromClass(NormalDefense::class.java)
    )

    var rightIndex = 0
    var swapIndex = 0
    var dropIndex = 0

    init {
        // HPを40に設定
        player.maxHealth = 40.0
        player.health = 40.0
    }

    /**
     * 左クリックを入力した時の処理
     * 発動中のスキルの解除と、自身のベクトルを0にする
     */
    fun leftClick() {
        // 左クリックでスキル解除とベクトルの大きさを0にする
        val activatingSkill: SeparatedSkill? = getActivatedSkill()
        activatingSkill?.let {
            activatingSkill.deactivate()
        }
        player.velocity = Vector(0.0, player.velocity.y, 0.0)
    }

    /**
     * 右クリックを入力した時の処理
     * 右クリックのスキルを発動、追加入力する
     */
    fun rightClick() {
        // Shiftが押されているか
        if (player.isSneaking) {
            rightIndex++
            if (rightIndex == SKILL_SET_SIZE) rightIndex = 0

            player.location.playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW)

            // 右クリック[1]を「LeafFlow」に変更しました。
            val skill = rightSkillSet[rightIndex].skill
            if (skill == null) {
                player.sendMessage("右クリック[$rightIndex]:スキルがセットされていません")
            } else {
                player.sendMessage("右クリック[$rightIndex]を「${skill.name}」に変更しました。")
            }
        } else {
            skillInput(rightSkillSet[rightIndex].skill, getHandWeapon()!!)
        }
    }

    /**
     * スワップキーを押した時の処理
     * スワップキーのスキルを発動、追加入力する
     */
    fun swap() {
        // Shiftが押されているか
        if (player.isSneaking) {
            swapIndex++
            if (swapIndex == SKILL_SET_SIZE) swapIndex = 0
            player.playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW)
            val skill = swapSkillSet[swapIndex].skill
            if (skill == null) {
                player.sendMessage("右クリック[$swapIndex]:スキルがセットされていません")
            } else {
                player.sendMessage("右クリック[$swapIndex]を「${skill.name}」に変更しました。")
            }
        } else {
            skillInput(swapSkillSet[swapIndex].skill, getHandWeapon()!!)
        }
    }

    /**
     * ドロップキーを押した時の処理
     * ドロップキーのスキルを発動、追加入力する
     */
    fun drop(weapon: Weapon) {
        // Shiftば押されているか
        if (player.isSneaking) {
            dropIndex++
            if (dropIndex == SKILL_SET_SIZE) dropIndex = 0
            player.playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW)
            val skill = dropSkillSet[dropIndex].skill
            if (skill == null) {
                player.sendMessage("右クリック[$dropIndex]:スキルがセットされていません")
            } else {
                player.sendMessage("右クリック[$dropIndex]を「${skill.name}」に変更しました。")
            }
        } else skillInput(dropSkillSet[dropIndex].skill, weapon)
    }

    /**
     * スキルを発動、追加入力する
     * @param skill 操作するスキル
     * @param weapon 手に持っている武器
     */
    fun skillInput(skill: Skill?, weapon: Weapon) {
        if(skill == null) {
            player.sendMessage("スキルがセットされていません")
            return
        }

        // 持っている武器を確認
        if (!skill.usable(weapon)) {
            player.sendMessage("この武器ではこのスキルを使用できません")
            return
        }

        // インターバル確認
        if (skill.inInterval) return

        // 複合スキルの場合
        val separatedSkill = skill as? SeparatedSkill
        separatedSkill?.let { sepSkill ->
            val activatedSkill = getActivatedSkill()

            if(activatedSkill != sepSkill) {
                activatedSkill?.deactivate()
            }

            if(sepSkill.activated) {
                sepSkill.additionalInput()
                return
            }
        }

        skill.activate()
    }


    /**
     * 発動中のスキルを返します
     * @return 発動中のスキル
     */
    fun getActivatedSkill() : SeparatedSkill? {
        for(skillKey in rightSkillSet) {
            val skill = skillKey.skill
            skill?.let {
                val combinedSkill = it as? SeparatedSkill
                if(combinedSkill?.activated == true) {
                    return combinedSkill
                }
            }
        }
        for(skillKey in swapSkillSet) {
            val skill = skillKey.skill
            skill?.let {
                val combinedSkill = it as? SeparatedSkill
                if(combinedSkill?.activated == true) {
                    return combinedSkill
                }
            }
        }
        for(skillKey in dropSkillSet) {
            val skill = skillKey.skill
            skill?.let {
                val combinedSkill = it as? SeparatedSkill
                if(combinedSkill?.activated == true) {
                    return combinedSkill
                }
            }
        }

        return null
    }

    /**
     * スキルがセット可能であるか確かめます
     * @param skill セットするスキル
     * @return スキルがセット可能であるか
     */
    fun settable(skill: Skill): Boolean {
        for (set in rightSkillSet) {
            set.skill?.let {
                if (it.`is`(skill)) return false
            }
        }
        for (set in swapSkillSet) {
            set.skill?.let {
                if (it.`is`(skill)) return false
            }
        }
        for (set in dropSkillSet) {
            set.skill?.let {
                if (it.`is`(skill)) return false
            }
        }
        return true
    }

    /**
     * 使用する武器を変更します
     * @param weapon 変更後の武器
     */
    fun changeWeapon(weapon: Weapon) {
        // スキルセットをリセット
        rightSkillSet.clear()
        rightSkillSet.apply { SkillSet(SkillButton.RIGHT,null,null,null) }
        swapSkillSet.clear()
        swapSkillSet.apply { SkillSet(SkillButton.SWAP,null,null,null) }
        dropSkillSet.clear()
        dropSkillSet.apply { SkillSet(SkillButton.DROP,null,null,null) }

        // 武器を変更
        selectedWeapon = weapon
    }

    /**
     * モードを変更します
     * @param mode 変更するモード
     */
    fun changeMode(mode: UserMode) {
        // バトルからトレーニング
        if (this.mode === UserMode.BATTLE && mode === UserMode.TRAINING) {
            // 稼働中のスキルの初期化
            initSkills()
        } else if (this.mode === UserMode.TRAINING && mode === UserMode.BATTLE) {
            // 稼働中のスキルの初期化
            initSkills()
        } else if (this.mode === UserMode.UNARMED && (mode === UserMode.BATTLE || mode === UserMode.TRAINING)) {
            // インベントリ初期化
            userInventory = UserInventory(this)
            // HPの初期化
            player.maxHealth = 40.0
            player.health = 40.0
        }
        this.mode = mode
    }

    /**
     * 登録されているスキルを初期化
     */
    fun initSkills() {
        val setList = listOf(rightSkillSet, swapSkillSet, dropSkillSet)
        for (set in setList) {
            for (key in set) {
                val skill = key.skill
                skill?.init()
            }
        }
    }

    /**
     * 指定したユーザーを攻撃します
     * @param user このユーザーを攻撃します
     * @param damage このダメージを与えます
     * @param vector このノックバックを与えます
     * @param sound このSEを再生します
     */
    fun attackUser(user: SkillUser, damage: Double, vector: Vector, sound: Sound) {
        player.playSound(sound)

        if(mode == UserMode.TRAINING) {
            user.damage(0.0, Vector(0, 0, 0))
        } else {
            user.damage(damage, vector)

            val game = SkillMaster.instance.gameList.getFromUser(this)
            val onAttack = game as? OnAttack
            onAttack?.onAttack(this)
        }
    }

    /**
     * 指定したEntityを攻撃します
     * @param entity このEntityを攻撃します
     * @param damage このダメージを与えます
     * @param vector このノックバックを与えます
     * @param sound このSEを再生します
     */
    fun attackEntity(entity: Entity, damage: Double, vector: Vector, sound: Sound) {
        player.playSound(sound)

        if(mode == UserMode.TRAINING) return

        val target = entity as? Damageable
        target?.damage(damage)
        target?.velocity = vector
    }

    fun attackNearest(radius: Double, damage: Double, vector: Vector, sound: Sound) : Boolean {
        val entities = getNearEntities(radius)

        if(entities.isEmpty()) return false

        val entity = entities[0]
        val player = entity as? Player

        if(player != null) {
            val user = SkillMaster.instance.userList[player] ?: return false

            attackUser(user, damage, vector, sound)
        } else {
            attackEntity(entity, damage, vector, sound)
        }
        return true
    }

    /**
     * このSkillUserへの攻撃を試みます
     * ただし、防御されるかもしれません
     * @param damage 攻撃力
     * @param vector ノックバック
     */
    private fun damage(damage: Double, vector: Vector) {
        val activatingSkill = getActivatedSkill()

        activatingSkill?.let {
            val defenseSkill = it as? DefenseSkill
            defenseSkill?.defense(damage,vector)
            return
        }

        player.damage(damage)
        player.velocity = vector
    }

    /**
     * このSkillUserから近いEntityを取得します
     * @param radius 検知する範囲の半径
     * @return 近い順にEntityのリスト
     */
    fun getNearEntities(radius: Double): List<Entity> {
        // 半径radiusで近くのentityのリストを取得
        val near = player.getNearbyEntities(radius, radius, radius)

        // Entityのリストを近い順に並べ替える
        EntityDistanceSort.quicksort(player, near, 0, near.size - 1)
        return near
    }

    fun getHandItem() : ItemStack = player.inventory.itemInMainHand

    /**
     * メインハンドの武器を取得します
     * @return メインハンドの武器
     */
    fun getHandWeapon(): Weapon? = Weapon.fromItemStack(getHandItem())


    /**
     * UUIDが一致しているかを確認します
     * @param other 比較するPlayer
     * @return UUIDが一致するか
     */
    fun match(other: Player): Boolean = player.uniqueId.toString() == other.uniqueId.toString()

    /**
     * UUIDが一致しているかを確認します
     * @param other 比較するSkillUser
     * @return UUIDが一致するか
     */
    fun match(other: SkillUser): Boolean = match(other.player)


}