package net.lifecity.mc.skillmaster.skill.function

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.game.function.OnAttack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

interface Attack {

    /**
     * 対象を攻撃します
     * @param entity 対象となるエンティティ
     * @param damage 対象に与えるダメージ
     * @param vector 対象に与えるベクトル
     * @param isAdd ベクトルの与え方 true->add, false->set
     */
    fun attack(attackUser: SkillUser?, entity: LivingEntity, damage: Double, vector: Vector, isAdd: Boolean) {
        // プレイヤーだった時の処理
        if (entity is Player) {
            val target = SkillMaster.INSTANCE.userList.get(entity) ?: return

            // 攻撃
            attackUser(attackUser, target, damage, vector, isAdd)
        }
        // プレイヤー以外の時の処理
        else {
            attackEntity(attackUser, entity, damage, vector, isAdd)
        }
    }

    /**
     * 指定したユーザーを攻撃します
     * @param user 指定したユーザー
     * @param damage ダメージ
     * @param vector ノックバック
     */
    private fun attackUser(attackUser: SkillUser?, user: SkillUser, damage: Double, vector: Vector, isAdd: Boolean) {
        if (attackUser == null) return

        // トレーニングモード時は攻撃不可
        if (attackUser.mode == UserMode.TRAINING)
            user.damage(0.0, Vector(0.0, 0.0, 0.0), true)
        else {
            // ダメージを与える
            user.damage(damage, vector, isAdd)

            // ゲーム中のときGameのonAttack()を呼び出す
            val game = SkillMaster.INSTANCE.gameList.getFromUser(user)
            if (game is OnAttack)
                game.onAttack(user)
        }
    }

    /**
     * 指定したエンティティを攻撃します
     * @param target 指定したエンティティ
     * @param damage ダメージ
     * @param vector ノックバック
     */
    private fun attackEntity(attackUser: SkillUser?, target: LivingEntity, damage: Double, vector: Vector, isAdd: Boolean) {
        if (attackUser == null) return

        // トレーニングモード時は攻撃不可
        if (attackUser.mode == UserMode.TRAINING)
            return

        // 標的にダメージを与える
        target.damage(damage)

        // 標的をノックバックさせる
        if (isAdd)
            target.velocity.add(vector)
        else
            target.velocity = vector
    }
}