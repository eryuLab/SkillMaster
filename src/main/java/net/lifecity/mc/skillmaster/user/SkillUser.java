package net.lifecity.mc.skillmaster.user;

import lombok.Getter;
import lombok.Setter;
import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.game.Game;
import net.lifecity.mc.skillmaster.game.function.OnAttack;
import net.lifecity.mc.skillmaster.inventory.InventoryFrame;
import net.lifecity.mc.skillmaster.inventory.UserInventory;
import net.lifecity.mc.skillmaster.skill.DefenseSkill;
import net.lifecity.mc.skillmaster.skill.SeparatedSkill;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.skill.defenseskills.NormalDefense;
import net.lifecity.mc.skillmaster.skill.separatedskills.LeafFlow;
import net.lifecity.mc.skillmaster.skill.skills.Kick;
import net.lifecity.mc.skillmaster.skill.skills.MoveFast;
import net.lifecity.mc.skillmaster.skill.skills.VectorAttack;
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

import java.util.Arrays;
import java.util.List;

/**
 * スキル使用者としてスキルを管理、実行するクラス
 */
public class SkillUser {

    private final int SKILL_SET_SIZE = 3;

    @Getter
    private final Player player;

    @Getter
    private UserMode mode;

    @Getter
    private UserInventory userInventory;

    @Getter
    @Setter
    private InventoryFrame openedInventory = null;

    @Getter
    @Setter
    private Weapon selectedWeapon = Weapon.STRAIGHT_SWORD;

    @Getter
    private final SkillSet rightSkillSet;

    @Getter
    private int rightIndex = 0;

    @Getter
    private final SkillSet swapSkillSet;

    @Getter
    private int swapIndex = 0;

    @Getter
    private final SkillSet dropSkillSet;

    @Getter
    private int dropIndex = 0;

    public SkillUser(Player player, UserMode mode) {
        this.player = player;
        this.mode = mode;
        SkillManager sm = new SkillManager(this);
        this.rightSkillSet = new SkillSet(
                SkillButton.RIGHT,
                sm.fromClass(VectorAttack.class),
                sm.fromClass(LeafFlow.class),
                null
        );
        this.swapSkillSet = new SkillSet(
                SkillButton.SWAP,
                sm.fromClass(MoveFast.class),
                null,
                null
        );
        this.dropSkillSet = new SkillSet(
                SkillButton.DROP,
                sm.fromClass(Kick.class),
                sm.fromClass(NormalDefense.class),
                null
        );
        this.userInventory = new UserInventory(this);

        // HPを40に設定
        this.player.setMaxHealth(40);
        this.player.setHealth(40);
    }

    /**
     * 左クリックを入力した時の処理
     * 発動中のスキルの解除と、自身のベクトルを0にする
     */
    public void leftClick() {
        // 左クリックでスキル解除とベクトルの大きさを0にする
        SeparatedSkill activatingSkill = getActivatedSkill();
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
            // 右クリック[1]を「LeafFlow」に変更しました。
            Skill skill = rightSkillSet.get(rightIndex).getSkill();
            if (skill == null)
                sendMessage("右クリック[" + rightIndex + "]:" + "スキルがセットされていません");
            else
                sendMessage("右クリック[" + rightIndex + "]を「" + skill.getName() + "」に変更しました。");
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
            Skill skill = swapSkillSet.get(swapIndex).getSkill();
            if (skill == null)
                sendMessage("右クリック[" + swapIndex + "]:" + "スキルがセットされていません");
            else
                sendMessage("右クリック[" + swapIndex + "]を「" + skill.getName() + "」に変更しました。");
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
            Skill skill = dropSkillSet.get(dropIndex).getSkill();
            if (skill == null)
                sendMessage("右クリック[" + dropIndex + "]:" + "スキルがセットされていません");
            else
                sendMessage("右クリック[" + dropIndex + "]を「" + skill.getName() + "」に変更しました。");
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

            SeparatedSkill activatedSkill = getActivatedSkill();

            if (activatedSkill != null) {
                if (activatedSkill != skill) {
                    activatedSkill.deactivate();
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
    public SeparatedSkill getActivatedSkill() {

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
     * 使用する武器を変更します
     * @param weapon 変更後の武器
     */
    public void changeWeapon(Weapon weapon) {
        // スキルセットをリセット
        rightSkillSet.clear();
        for (int i = 0; i < 3; i++)
            rightSkillSet.add(null);

        swapSkillSet.clear();
        for (int i = 0; i < 3; i++)
            swapSkillSet.add(null);

        dropSkillSet.clear();
        for (int i = 0; i < 3; i++)
            dropSkillSet.add(null);

        // 武器を変更
        selectedWeapon = weapon;
    }

    /**
     * モードを変更します
     * @param mode 変更するモード
     */
    public void changeMode(UserMode mode) {
        // バトルからトレーニング
        if (this.mode == UserMode.BATTLE && mode == UserMode.TRAINING) {
            // 稼働中のスキルの初期化
            initSkills();
        }
        // トレーニングからバトル
        else if (this.mode == UserMode.TRAINING && mode == UserMode.BATTLE) {
            // 稼働中のスキルの初期化
            initSkills();
        }
        // 武装解除からバトル、トレーニング
        else if (this.mode == UserMode.UNARMED && (mode == UserMode.BATTLE || mode == UserMode.TRAINING)) {
            // インベントリ初期化
            userInventory = new UserInventory(this);
            // HPの初期化
            player.setMaxHealth(40);
            player.setHealth(40);
        }
        this.mode = mode;
    }

    /**
     * 登録されているスキルを初期化
     */
    public void initSkills() {
        List<SkillSet> setList = Arrays.asList(rightSkillSet, swapSkillSet, dropSkillSet);

        for (SkillSet set : setList) {
            for (SkillKey key : set) {
                Skill skill = key.getSkill();
                if (skill != null)
                    skill.init();
            }
        }
    }

    /**
     * 標的を攻撃します
     * todo 位置から一番近いentityが標的
     * todo 目線から一番近いentityが標的
     * todo 複数の標的に攻撃
     */

    /**
     * 指定したユーザーを攻撃します
     * @param user このユーザーを攻撃します
     * @param damage このダメージを与えます
     * @param vector このノックバックを与えます
     * @param sound このSEを再生します
     */
    public void attackUser(SkillUser user, double damage, Vector vector, Sound sound) {
        // SE再生
        playSound(sound);

        // トレーニングモード時は攻撃不可
        if (mode == UserMode.TRAINING)
            user.damage(0, new Vector(0, 0, 0));
        else {
            user.damage(damage, vector);
            // ゲーム中のときonAttack()を呼び出す
            Game game = SkillMaster.instance.getGameList().getFromUser(this);
            if (game instanceof OnAttack onAttack)
                onAttack.onAttack(this);
        }
    }

    /**
     * 指定したEntityを攻撃します
     * @param entity このEntityを攻撃します
     * @param damage このダメージを与えます
     * @param vector このノックバックを与えます
     * @param sound このSEを再生します
     */
    public void attackEntity(Entity entity, double damage, Vector vector, Sound sound) {
        // SE再生
        playSound(sound);

        // トレーニングモード時は攻撃不可
        if (mode == UserMode.TRAINING)
            return;

        if (entity instanceof Damageable target) {
            // 標的にダメージを与える
            target.damage(damage);

            // 標的をノックバックさせる
            target.setVelocity(vector);
        }
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
            attackUser(user, damage, vector, sound);

        }
        // プレイヤー以外の時の処理
        else {
            // 攻撃処理
            attackEntity(entity, damage, vector, sound);
        }
        return true;
    }

    /**
     * このSkillUserへの攻撃を試みます
     * ただし、防御されるかもしれません
     * @param damage 攻撃力
     * @param vector ノックバック
     */
    private void damage(double damage, Vector vector) {
        // 防御スキル確認
        SeparatedSkill activatingSkill = getActivatedSkill();

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
     * プレイヤーにタイトルを送信します
     * @param title 送信するタイトル
     * @param sub 送信するサブタイトル
     */
    public void sendTitle(String title, String sub) {
        player.sendTitle(title, sub);
    }

    /**
     * このプレイヤーの位置でSEを再生します
     * @param sound 再生するSE
     */
    public void playSound(Sound sound) {
        player.getWorld().playSound(player.getLocation(), sound, 1f, 1f);
    }

    /**
     * UUIDが一致しているかを確認します
     * @param other 比較するPlayer
     * @return UUIDが一致するか
     */
    public boolean match(Player other) {
        return player.getUniqueId().toString().equals(other.getUniqueId().toString());
    }

    /**
     * UUIDが一致しているかを確認します
     * @param other 比較するSkillUser
     * @return UUIDが一致するか
     */
    public boolean match(SkillUser other) {
        return match(other.player);
    }
}
