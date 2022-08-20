package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.inventory.InventoryFrame;
import net.lifecity.mc.skillmaster.inventory.UserInventory;
import net.lifecity.mc.skillmaster.skill.DefenseSkill;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.defenseskills.normaldefense.SSNormalDefense;
import net.lifecity.mc.skillmaster.skill.skills.highjump.SSHighJump;
import net.lifecity.mc.skillmaster.skill.skills.vectorattack.SSVectorAttack;
import net.lifecity.mc.skillmaster.skill.skills.movefast.SSMoveFast;
import net.lifecity.mc.skillmaster.user.skillset.SkillButton;
import net.lifecity.mc.skillmaster.user.skillset.SkillKey;
import net.lifecity.mc.skillmaster.user.skillset.SkillSet;
import net.lifecity.mc.skillmaster.utils.EntityDistanceSort;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * スキル使用者としてスキルを管理、実行するクラス
 */
public class SkillUser {

    private final int SKILL_SET_SIZE = 3;

    @Getter
    private final Player player;

    @Getter
    @Setter
    private UserMode mode = UserMode.BATTLE;

    @Getter
    private final UserInventory userInventory;

    @Getter
    @Setter
    private InventoryFrame openedInventory = null;

    @Getter
    @Setter
    private Weapon selectedWeapon = Weapon.STRAIGHT_SWORD;

    @Getter
    private SkillSet rightSkillSet;

    @Getter
    private int rightIndex = 0;

    @Getter
    private SkillSet swapSkillSet;

    @Getter
    private int swapIndex = 0;

    @Getter
    private SkillSet dropSkillSet;

    @Getter
    private int dropIndex = 0;

    public SkillUser(Player player) {
        this.player = player;
        this.rightSkillSet = new SkillSet(
                SkillButton.RIGHT,
                new SSVectorAttack(this),
                null,
                null
        );
        this.swapSkillSet = new SkillSet(
                SkillButton.SWAP,
                new SSMoveFast(this),
                null,
                null
        );
        this.dropSkillSet = new SkillSet(
                SkillButton.DROP,
                new SSNormalDefense(this),
                new SSHighJump(this),
                null
        );
        this.userInventory = new UserInventory(this);
    }

    /**
     * 左クリックを入力した時の処理
     * 発動中のスキルの解除と、自身のベクトルを0にする
     */
    public void leftClick() {
        // 左クリックでスキル解除とベクトルの大きさを0にする
        SeparatedSkill activatingSkill = getActivatingSkill();
        if (activatingSkill != null) {
            activatingSkill.deactivate();
        }

        player.setVelocity(new Vector(0, player.getVelocity().getY(), 0));
    }

    /**
     * 右クリックを入力した時の処理
     * 右クリックのスキルを発動、追加入力する
     */
    public void rightClick() {
        // Shiftが押されているか
        if (player.isSneaking()) {
            rightIndex++;
            if (rightIndex == SKILL_SET_SIZE)
                rightIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
            sendMessage("右クリックのスキルを" + rightIndex + "に変更しました。");
        }
        else
            skillInput(rightSkillSet.get(rightIndex).getSkill(), getHandWeapon());
    }

    /**
     * スワップキーを押した時の処理
     * スワップキーのスキルを発動、追加入力する
     */
    public void swap() {
        // Shiftが押されているか
        if (player.isSneaking()) {
            swapIndex++;
            if (swapIndex == SKILL_SET_SIZE)
                swapIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
            sendMessage("スワップのスキルを" + swapIndex + "に変更しました。");
        }
        else
            skillInput(swapSkillSet.get(swapIndex).getSkill(), getHandWeapon());
    }

    /**
     * ドロップキーを押した時の処理
     * ドロップキーのスキルを発動、追加入力する
     */
    public void drop(Weapon weapon) {
        // Shiftば押されているか
        if (player.isSneaking()) {
            dropIndex++;
            if (dropIndex == SKILL_SET_SIZE)
                dropIndex = 0;
            playSound(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW);
            sendMessage("ドロップのスキルを" + dropIndex + "に変更しました。");
        }
        else
            skillInput(dropSkillSet.get(dropIndex).getSkill(), weapon);
    }

    /**
     * スキルを発動、追加入力する
     * @param skill 操作するスキル
     * @param weapon 手に持っている武器
     */
    private void skillInput(Skill skill, Weapon weapon) {
        // null確認
        if (skill == null) {
            sendMessage("スキルがセットされていません");
            return;
        }

        // 持っている武器を確認
        if (!skill.usable(weapon)) {
            sendMessage("この武器ではこのスキルを使用できません");
            return;
        }

        // インターバル確認
        if (skill.isInInterval())
            return;

        // 複合スキルの場合
        if (skill instanceof SeparatedSkill separatedSkill) {

            SeparatedSkill activatingSkill = getActivatingSkill();

            if (activatingSkill != null) {
                if (activatingSkill != skill) {
                    activatingSkill.deactivate();
                }
            }

            // 発動中だったら追加入力
            if (separatedSkill.isActivated()) {
                separatedSkill.additionalInput();
                return;
            }

        }

        skill.activate();
    }

    /**
     * 発動中のスキルを返します
     * @return 発動中のスキル
     */
    public SeparatedSkill getActivatingSkill() {

        for (SkillKey skillKey : rightSkillSet) {
            Skill skill  = skillKey.getSkill();
            if (skill == null)
                continue;
            if (skill instanceof SeparatedSkill combinedSkill) {
                if (combinedSkill.isActivated())
                    return combinedSkill;
            }
        }
        for (SkillKey skillKey : swapSkillSet) {
            Skill skill = skillKey.getSkill();
            if (skill == null)
                continue;
            if (skill instanceof SeparatedSkill combinedSkill) {
                if (combinedSkill.isActivated())
                    return combinedSkill;
            }
        }
        for (SkillKey skillKey : dropSkillSet) {
            Skill skill = skillKey.getSkill();
            if (skill == null)
                continue;
            if (skill instanceof SeparatedSkill combinedSkill) {
                if (combinedSkill.isActivated())
                    return combinedSkill;
            }
        }

        return null;
    }

    /**
     * スキルがセット可能であるか確かめます
     * @param skill セットするスキル
     * @return スキルがセット可能であるか
     */
    public boolean settable(Skill skill) {
        for (SkillKey set : rightSkillSet) {
            if (set.getSkill() == null)
                continue;
            if (set.getSkill().is(skill))
                return false;
        }
        for (SkillKey set : swapSkillSet) {
            if (set.getSkill() == null)
                continue;
            if (set.getSkill().is(skill))
                return false;
        }
        for (SkillKey set : dropSkillSet) {
            if (set.getSkill() == null)
                continue;
            if (set.getSkill().is(skill))
                return false;
        }
        return true;
    }

    /**
     * 標的を攻撃します
     * todo 位置から一番近いentityが標的
     * todo 目線から一番近いentityが標的
     * todo 複数の標的に攻撃
     */
    public void attack() {
    }

    /**
     * 一番近いEntityを攻撃します
     * @param radius この半径内のEntityに攻撃します
     * @param damage このダメージを与えます
     * @param vector このベクトルを与えます
     * @param sound このSEを再生します
     * @return 攻撃できたらtrueを返します
     */
    public boolean attackNearest(double radius, double damage, Vector vector, Sound sound) {
        List<Entity> entities = getNearEntities(radius);

        if (entities.size() == 0)
            return false;

        Entity entity = entities.get(0);
        if (entity == null)
            return false;

        // プレイヤーだった時の処理
        if (entity instanceof Player player) {
            SkillUser user = SkillMaster.instance.getUserList().get(player);

            if (user == null)
                return false;

            // 攻撃処理
            user.damage(damage, vector, sound);

        } else {
            // 攻撃処理
            if (entity instanceof Damageable target) {
                // 標的にダメージを与える
                target.damage(damage);

                // 標的をノックバックさせる
                target.setVelocity(vector);

                // SE再生
                playSound(sound);
            }
        }
        return true;
    }

    /**
     * このSkillUserへの攻撃を試みます
     * ただし、防御されるかもしれません
     * @param damage 攻撃力
     * @param vector ノックバック
     * @param sound 攻撃成功時の音
     */
    private void damage(double damage, Vector vector, Sound sound) {
        // 防御スキル確認
        SeparatedSkill activatingSkill = getActivatingSkill();

        if (activatingSkill != null) {
            if (activatingSkill instanceof DefenseSkill defenseSkill) {
                defenseSkill.defense(damage, vector);
                return;
            }
        }

        // ダメージを与える
        player.damage(damage);

        // ノックバックさせる
        player.setVelocity(vector);

        // SE再生
        playSound(sound);
    }

    /**
     * このSkillUserから近いEntityを取得します
     * @param radius 検知する範囲の半径
     * @return 近い順にEntityのリスト
     */
    public List<Entity> getNearEntities(double radius) {
        // 半径radiusで近くのentityのリストを取得
        List<Entity> near = player.getNearbyEntities(radius, radius, radius);

        // Entityのリストを近い順に並べ替える
        EntityDistanceSort.quicksort(player, near, 0, near.size() - 1);

        return near;
    }

    /**
     * メインハンドのMaterialを取得します
     * @return メインハンドのMaterial
     */
    public ItemStack getHandItem() {
        return player.getInventory().getItemInMainHand();
    }

    /**
     * メインハンドの武器を取得します
     * @return メインハンドの武器
     */
    public Weapon getHandWeapon() {
        return Weapon.fromItemStack(getHandItem());
    }

    /**
     * このプレイヤーにメッセージログを送信します
     * @param msg 送信するメッセージ
     */
    public void sendMessage(String msg) {
        player.sendMessage(msg);
    }

    /**
     * アクションバーでメッセージを送信します
     * @param msg 送信するメッセージ
     */
    public void sendActionBar(String msg) {
        player.sendActionBar(msg);
    }

    /**
     * このプレイヤーの位置でSEを再生します
     * @param sound 再生するSE
     */
    public void playSound(Sound sound) {
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

    /**
     * UUIDが一致しているかを確認します
     * @param target 比較するPlayer
     * @return UUIDが一致するか
     */
    public boolean match(Player target) {
        return player.getUniqueId().toString().equals(target.getUniqueId().toString());
    }
}
