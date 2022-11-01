package net.lifecity.mc.skillmaster.skill.function

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.game.function.OnAttack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.lang.Exception

interface Attack {

    /**
     * 対象を攻撃します
     * 加算によってベクトルを与えます
     * @param attackUser 攻撃者
     * @param entity 攻撃対象
     * @param damage ダメージ
     * @param vector ノックバック
     */
    fun attackAddVector(attackUser: SkillUser, entity: LivingEntity, damage: Double, vector: Vector) {
        // プレイヤーだった時の処理
        if (entity is Player) {
            val target = SkillMaster.INSTANCE.userList.get(entity) ?: return

            // 攻撃
            attackUserAddVector(attackUser, target, damage, vector)
        }
        // プレイヤー以外の時の処理
        else {
            attackEntityAddVector(attackUser, entity, damage, vector)
        }
    }

    /**
     * 対象を攻撃します
     * 代入によってベクトルを与えます
     * @param attackUser 攻撃者
     * @param entity 攻撃対象
     * @param damage ダメージ
     * @param vector ノックバック
     */
    fun attackChangeVector(attackUser: SkillUser, entity: LivingEntity, damage: Double, vector: Vector) {
        // プレイヤーだった時の処理
        if (entity is Player) {
            val target = SkillMaster.INSTANCE.userList.get(entity) ?: return

            // 攻撃
            attackUserChangeVector(attackUser, target, damage, vector)
        }
        // プレイヤー以外の時の処理
        else {
            attackEntityChangeVector(attackUser, entity, damage, vector)
        }
    }

    /**
     * 指定したユーザーを攻撃します
     * 加算によってベクトルが与えられます
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     * @param damage ダメージ
     * @param vector ノックバック
     */
    private fun attackUserAddVector(attackUser: SkillUser, target: SkillUser, damage: Double, vector: Vector) {
        // トレーニングモード時は攻撃不可
        if (attackUser.mode == UserMode.Training)
            target.damageAddVector(0.0, Vector(0.0, 0.0, 0.0))
        else {
            // ダメージを与える
            target.damageAddVector(damage, vector)

            // ゲーム中のときGameのonAttack()を呼び出す
            try {
                val game = SkillMaster.INSTANCE.gameList.getFromUser(target)
                if (game is OnAttack)
                    game.onAttack(target)
            } catch (e: Exception) {
                return
            }
        }
    }

    /**
     * 指定したユーザーを攻撃します
     * 代入によってベクトルが与えられます
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     * @param damage ダメージ
     * @param vector ノックバック
     */
    private fun attackUserChangeVector(attackUser: SkillUser, target: SkillUser, damage: Double, vector: Vector) {
        // トレーニングモード時は攻撃不可
        if (attackUser.mode == UserMode.Training)
            target.damageAddVector(0.0, Vector(0.0, 0.0, 0.0))
        else {
            // ダメージを与える
            target.damageChangeVector(damage, vector)

            // ゲーム中のときGameのonAttack()を呼び出す
            try {
                val game = SkillMaster.INSTANCE.gameList.getFromUser(target)
                if (game is OnAttack)
                    game.onAttack(target)
            } catch (e: Exception) {
                return
            }
        }
    }

    /**
     * LivingEntityを攻撃します
     * 加算によってベクトルが与えられます
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     * @param damage ダメージ
     * @param vector ノックバック
     */
    private fun attackEntityAddVector(attackUser: SkillUser, target: LivingEntity, damage: Double, vector: Vector) {
        // トレーニングモード時は攻撃不可
        if (attackUser.mode == UserMode.Training)
            return

        // 標的にダメージを与える
        target.damage(damage)

        // 標的をノックバックさせる
        target.velocity.add(vector)
    }

    /**
     * LivingEntityを攻撃します
     * 代入によってベクトルが与えられます
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     * @param damage ダメージ
     * @param vector ノックバック
     */
    private fun attackEntityChangeVector(attackUser: SkillUser, target: LivingEntity, damage: Double, vector: Vector) {
        // トレーニングモード時は攻撃不可
        if (attackUser.mode == UserMode.Training)
            return

        // 標的にダメージを与える
        target.damage(damage)

        // 標的をノックバックさせる
        target.velocity = vector
    }
}