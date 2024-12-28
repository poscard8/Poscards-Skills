package github.poscard8.poscardsskills.skill;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.registry.PSParticleTypes;
import github.poscard8.poscardsskills.registry.PSSoundEvents;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.*;
import java.util.function.Supplier;

/**
 * The skill instances players have.
 * <p>{@link #skill}, {@link #level}: Self explanatory.</p>
 * <p>{@link #xp}: <b>Leftover</b> xp, not total xp.</p>
 * <p>{@link #claimedRewards}: Reward claim data. If there are no rewards for level <i>n</i>,
 * {@code claimedRewards[n+1]} is automatically passed as {@code true}.</p>
 * <p>{@link #milestones}: See {@link SkillMilestone}.</p>
 */
@SuppressWarnings("unused")
public class SkillInstance {

    public final Skill skill;
    public int level;
    public int xp;
    public boolean[] claimedRewards;
    public List<SkillMilestone> milestones;

    protected SkillInstance(Skill skill) { this(skill, 1, 0, skill.getDefaultRewardArray()); }

    protected SkillInstance(Skill skill, int level, int xp, boolean[] claimedRewards) {

        this.skill = skill;
        this.level = Math.min(level, skill.maxLevel); // this line ensures the level is set properly
        this.xp = xp;
        this.claimedRewards = claimedRewards;
        this.milestones = generateMilestones();

        updateLevel();
    }

    public static SkillInstance of(ServerPlayer player, Skill skill) { return SkillData.of(player).getSkill(skill); }

    public static SkillInstance deserialize(String serialized) {

        String[] stringArray = serialized.split(",");

        ResourceLocation location = ResourceLocation.tryParse(stringArray[0]);
        if (location == null) return null;

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int level = Integer.parseInt(stringArray[1]);
        int leftoverXP = Integer.parseInt(stringArray[2]);

        boolean[] booleanArray = new boolean[skill.maxLevel + 1];
        boolean[] fullData = skill.getDefaultRewardArray();

        for (int i = 0; i <= skill.maxLevel; i++) {

            try {

                char c = stringArray[3].charAt(i);
                booleanArray[i] = Character.getNumericValue(c) == 1;

            } catch (Exception exception) { booleanArray[i] = fullData[i]; }
        }

        return new SkillInstance(skill, level, leftoverXP, booleanArray);
    }

    /**
     * Disables the skill from playing level up sound at low levels since
     * the player can level up quickly at early game and hearing the same sound
     * can be irritating.
     */
    protected static boolean shouldPlayLevelUpSound(int oldLevel, int newLevel) {

        for (int i = oldLevel + 1; i <= newLevel; i++) {

            if (i >= 20 || i % 5 == 0) return true;
        }
        return false;
    }

    public String serialize() {

        StringBuilder builder = new StringBuilder();

        for (boolean bool : claimedRewards) {

            char dataChar = bool ? '1' : '0';
            builder.append(dataChar);
        }

        String rewardData = builder.toString();
        return String.format("%s,%d,%d,%s", skill, level, xp, rewardData);
    }

    /**
     * Last two characters are set by the skill's position. This ensures that each skill has a different UUID.
     */
    public Map.Entry<Attribute, AttributeModifier> getAttributeModifier() { return getAttributeModifier(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5" + skill.indexAsString())); }

    public Map.Entry<Attribute, AttributeModifier> getAttributeModifier(UUID uuid) {

        return Map.entry(skill.attribute, new AttributeModifier(uuid, () -> "Poscard's Skills: Skill modifier", level * skill.attributeAmount, AttributeModifier.Operation.ADDITION));
    }

    @SuppressWarnings("ALL")
    public final boolean addXP(ServerPlayer player, int amount) { return addXP(player, amount, true, true); }

    /**
     * Adds <i>n</i> xp.
     * @param manually True if by an experience source, false if by a command. Determines if the wisdom will apply
     *                 and if a pop-up message will be displayed.
     * @param soundAndParticles Prevents the skill from playing more sounds and displaying more particles than needed.
     * @return Level up.
     */
    public final boolean addXP(ServerPlayer player, int amount, boolean manually, boolean soundAndParticles) {

        double universalMultiplier = PoscardsSkillsCommonConfig.UNIVERSAL_XP_MULTIPLIER.get();

        // new amount based on the player's wisdom
        int newAmount = manually ? (int) Math.round((player.getAttributeValue(PSAttributes.WISDOM.get()) / 100 + 1) * universalMultiplier * amount) : amount;
        int oldLevel = level;

        xp += newAmount;
        boolean levelUp = updateLevel();

        if (levelUp) onLevelUp(player, oldLevel, level, manually, soundAndParticles);
        onXPAdd(player, newAmount, oldLevel, level, manually, soundAndParticles);

        updateAdvancements(player);
        updateSkillData(player);
        return levelUp;
    }

    /**
     * Sets the leftover xp to <i>n</i>. Only possible with commands.
     * @return Level up.
     */
    public final boolean setXP(ServerPlayer player, int amount, boolean soundAndParticles) {

        int oldLevel = level;

        xp = amount;
        boolean levelUp = updateLevel();

        if (levelUp) onLevelUp(player, oldLevel, level, false, soundAndParticles);
        onXPSet(player, xp, oldLevel, level, soundAndParticles);

        updateAdvancements(player);
        updateSkillData(player);
        return levelUp;
    }

    /**
     * Adds <i>n</i> levels while keeping the xp same. Only possible with commands.
     * @return Level up.
     */
    public final boolean addLevel(ServerPlayer player, int amount, boolean soundAndParticles) {

        int oldLevel = level;
        level = Mth.clamp(level + amount, level, maxLevel());

        boolean levelUp = level > oldLevel;
        if (levelUp) onLevelUp(player, oldLevel, level, false, soundAndParticles);
        onLevelAdd(player, oldLevel, level, soundAndParticles);

        updateAdvancements(player);
        updateSkillData(player);
        return levelUp;
    }

    /**
     * Sets the level to <i>n</i> and xp to <i>0</i>. Only possible with commands.
     * @return Level up.
     */
    public final boolean setLevel(ServerPlayer player, int amount, boolean soundAndParticles) {

        int oldLevel = level;
        level = Mth.clamp(amount, Skill.TRUE_MIN_LEVEL, maxLevel());
        xp = 0;

        boolean levelUp = level > oldLevel;
        if (levelUp) onLevelUp(player, oldLevel, level, false, soundAndParticles);
        onLevelSet(player, oldLevel, level, soundAndParticles);

        updateAdvancements(player);
        updateSkillData(player);
        return levelUp;
    }

    public final void reset(ServerPlayer player) {

        int oldLevel = level;
        int oldXp = xp;

        level = 1;
        xp = 0;
        claimedRewards = skill.getDefaultRewardArray();
        milestones = generateMilestones();

        onReset(player, oldLevel, oldXp);

        updateAdvancements(player);
        updateSkillData(player);
    }

    public final void claimRewards(ServerPlayer player, int level) {

        claimedRewards[level] = true;
        updateSkillData(player);
    }

    public final void updateSkillData(ServerPlayer player) { SkillData.of(player).updateSkill(this); }

    public void updateAdvancements(ServerPlayer player) {

        PSCriteriaTriggers.GAIN_XP.trigger(player, totalXP());
        PSCriteriaTriggers.LEVEL_UP.trigger(player, level);
    }

    /**
     * Displays xp and message.
     */
    public void onXPAdd(ServerPlayer player, int amount, int oldLevel, int newLevel, boolean manually, boolean soundAndParticles) {

        boolean levelUp = newLevel > oldLevel;

        if (!levelUp && manually && soundAndParticles && PoscardsSkillsClientConfig.XP_GAIN_SOUND.get()) PSUtils.playLocalSound(player, PSSoundEvents.XP_GAIN);
        if (manually && amount >= PoscardsSkillsClientConfig.MINIMUM_XP_FOR_PROGRESS_MESSAGE.get()) player.displayClientMessage(PSComponents.xpGain(amount, this), true);
    }

    public void onXPSet(ServerPlayer player, int amount, int oldLevel, int newLevel, boolean soundAndParticles) {
    }

    public void onLevelAdd(ServerPlayer player, int oldLevel, int newLevel, boolean soundAndParticles) {
    }

    public void onLevelSet(ServerPlayer player, int oldLevel, int newLevel, boolean soundAndParticles) {
    }

    public void onReset(ServerPlayer player, int oldLevel, int oldXp) {
    }

    /**
     * Level ups are made from <i>n</i> to <i>m</i> instead of <i>n</i> to <i>n+1</i>. This is to prevent
     * the skill from displaying more messages than needed when large amounts of xp is gained instantly (commands).
     */
    public void onLevelUp(ServerPlayer player, int oldLevel, int newLevel, boolean manually, boolean soundAndParticles) {

        if (newLevel > skill.maxLevel) return;

        Supplier<SoundEvent> soundGetter = shouldPlayLevelUpSound(oldLevel, newLevel) ? PSSoundEvents.LEVEL_UP : PSSoundEvents.XP_GAIN;

        if (soundAndParticles && PoscardsSkillsClientConfig.LEVEL_UP_PARTICLES.get()) PSUtils.addParticlesAroundPlayer(player, PSParticleTypes.LEVEL_UP);
        if (soundAndParticles && PoscardsSkillsClientConfig.LEVEL_UP_SOUND.get()) PSUtils.playLocalSound(player, soundGetter);

        PSComponents.levelUpComponents(this, oldLevel, newLevel, manually).forEach(component -> player.displayClientMessage(component, false));
    }

    public final boolean hasUnclaimedRewards() {

        for (SkillMilestone milestone : milestones) { if (milestone.canClaimRewards()) return true; }
        return false;
    }

    public final boolean isMaxLevel() { return level == skill.maxLevel; }

    public final int maxLevel() { return skill.maxLevel; }

    public final int nextLevel() { return Math.min(level + 1, maxLevel()); }

    public final int totalXP() { return Skill.getNeededTotalXP(level) + xp; }

    public final SkillMilestone milestone(int level) { return milestones.get(level); }

    public final boolean[] getRewardArrayForAscension() { return PoscardsSkillsCommonConfig.KEEP_CLAIMED_REWARDS.get() ? claimedRewards : skill.getDefaultRewardArray(); }

    /**
     * Updates the level if there is overflow xp.
     * @return Level up.
     */
    protected final boolean updateLevel() {

        int oldLevel = level;
        int newLevel = level;

        for (int n = level + 1; n <= skill.maxLevel; n++) {

            if (xp >= Skill.getNeededXP(level, n)) { newLevel = n; } else break;
        }

        boolean levelUp = newLevel > level;

        if (levelUp) {

            level = newLevel;
            xp -= Skill.getNeededXP(oldLevel, newLevel);
        }
        return levelUp;
    }

    protected List<SkillMilestone> generateMilestones() {

        List<SkillMilestone> list = new ArrayList<>();

        for (int i = 0; i <= skill.maxLevel; i++) { list.add(new SkillMilestone(this, i)); }
        return list;
    }

    @Override
    public String toString() { return "Skill instance: " + serialize(); }

}
