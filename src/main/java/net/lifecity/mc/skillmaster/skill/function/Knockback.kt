package net.lifecity.mc.skillmaster.skill.function

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.game.function.OnAttack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.lang.Exception

interface Knockback {

    /**
     * 対象をノックバックさせます
     * 加算によってベクトルを与えます
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     * @param vector ノックバック
     */
    fun addVector(attackUser: SkillUser, target: LivingEntity, vector: Vector) {
        // トレーニング時はノックバック不可
        if (attackUser.mode == UserMode.TRAINING)
            return

        // ゲームアクションを呼び出す
        callGameAction(attackUser, target)

        // 対象をノックバックさせる
        target.velocity.add(vector)
        target.damage(0.0)
        target.noDamageTicks = 0
    }

    /**
     * 対象をノックバックさせます
     * 代入によってベクトルを与えます
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     * @param vector ノックバック
     */
    fun changeVector(attackUser: SkillUser, target: LivingEntity, vector: Vector) {
        // トレーニング時はノックバック不可
        if (attackUser.mode == UserMode.TRAINING)
            return

        // ゲームアクションを呼び出す
        callGameAction(attackUser, target)

        // 対象をノックバックさせる
        target.velocity = vector
        target.damage(0.0)
        target.noDamageTicks = 0
    }

    /**
     * ゲームアクションを呼び出します
     * @param attackUser 攻撃者
     * @param target 攻撃対象
     */
    private fun callGameAction(attackUser: SkillUser, target: LivingEntity) {
        // 対象がプレイヤーだった時の処理
        if (target is Player) {
            // ゲーム中のときGameのonAttack()を呼び出す
            try {
                val game = SkillMaster.INSTANCE.gameList.getFromUser(attackUser)
                if (game is OnAttack)
                    game.onAttack(attackUser)
            } catch (e: Exception) {
                return
            }

        }
    }
}