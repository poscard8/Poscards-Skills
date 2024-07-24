package github.poscard8.poscardsskills.skill;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public final class SkillInstance {

    public final Skill skill;
    public int level;
    public int leftoverXP;
    public int totalXP;
    public boolean[] claimedRewards;
    public List<SkillMilestone> milestones;

    SkillInstance(Skill skill) { this(skill, 1, 0, skill.getDefaultRewardArray()); }

    SkillInstance(Skill skill, int level, int leftoverXP, boolean[] claimedRewards) {

        this.skill = skill;
        this.level = Math.min(level, Skill.MAX_LEVEL);
        this.leftoverXP = leftoverXP;
        this.totalXP = calculateTotalXP();
        this.claimedRewards = claimedRewards;
        this.milestones = generateMilestones();

        int newLevel = level;

        for (int lvl = nextLevel(); lvl <= Skill.MAX_LEVEL; lvl++) {

            if (leftoverXP >= Skill.getNeededXP(this.level, lvl)) { newLevel = lvl; } else break;
        }

        if (newLevel > level) {

            this.leftoverXP -= Skill.getNeededXP(this.level, newLevel);
            this.level = newLevel;
        }
    }

    public static SkillInstance of(Player player, Skill skill) { return SkillData.of(player).getSkill(skill); }

    public static SkillInstance deserialize(String serialized) {

        String[] stringArray = serialized.split(",");
        boolean[] byteArray = new boolean[Skill.MAX_LEVEL + 1];

        ResourceLocation location = ResourceLocation.tryParse(stringArray[0]);
        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int level = Integer.parseInt(stringArray[1]);
        int leftoverXP = Integer.parseInt(stringArray[2]);

        boolean[] fullData = skill.getDefaultRewardArray();

        for (int i = 0; i <= Skill.MAX_LEVEL; i++) {

            try {

                char c = stringArray[3].charAt(i);
                byteArray[i] = Character.getNumericValue(c) == 1;

            } catch (Exception exception) { byteArray[i] = fullData[i]; }
        }

        return new SkillInstance(skill, level, leftoverXP, byteArray);
    }

    public String serialize() {

        StringBuilder builder = new StringBuilder();

        for (boolean bool : claimedRewards) { if (bool) { builder.append('1'); } else builder.append('0'); }
        String rewardData = builder.toString();

        return String.format("%s,%d,%d,%s", skill, level, leftoverXP, rewardData);
    }

    public Map.Entry<Attribute, AttributeModifier> getAttributeModifier() { return getAttributeModifier(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5C" + skill.indexAsString())); }

    public Map.Entry<Attribute, AttributeModifier> getAttributeModifier(UUID uuid) {

        return Map.entry(skill.attribute, new AttributeModifier(uuid, () -> "Poscard's Skills: Skill modifier", level * skill.attributeAmount, AttributeModifier.Operation.ADDITION));
    }

    public boolean addXP(Player player, int amount, @Nullable ResourceLocation typeKey) { return addXP(player, amount, typeKey, true, true); }

    public boolean addXP(Player player, int amount, @Nullable ResourceLocation typeKey, boolean displayMessage, boolean playSounds) {

        boolean manually = typeKey != null;
        int newAmount = manually ? Math.round(((float) player.getAttributeValue(BaseModule.Attributes.WISDOM.get()) / 100 + 1) * amount) : amount;

        leftoverXP += newAmount;
        totalXP += newAmount;

        if (player instanceof ServerPlayer serverPlayer) PSCriteriaTriggers.GAIN_XP.trigger(serverPlayer, totalXP);

        int newLevel = level;

        for (int n = level + 1; n <= Skill.MAX_LEVEL; n++) {

            if (leftoverXP >= Skill.getNeededXP(level, n)) { newLevel = n; } else break;
        }

        boolean levelUp = newLevel > level;

        if (levelUp) {

            levelUp(player, level, newLevel, displayMessage, playSounds);

        } else if (manually && playSounds && PoscardsSkillsClientConfig.XP_GAIN_SOUND.get()) playXPSound(player);


        if (manually && amount >= PoscardsSkillsClientConfig.MINIMUM_XP_FOR_PROGRESS_MESSAGE.get()) {

            player.displayClientMessage(PSComponents.xpGain(newAmount, this), true);
        }

        updatePlayerData(player);
        handleRareDrops(player, newAmount, typeKey);

        return levelUp;
    }

    public boolean setXP(Player player, int amount, boolean playSounds) {

        boolean displayMessage = amount > totalXP;

        reset(player, false);
        return addXP(player, amount, null, displayMessage, playSounds);
    }

    // passing typeKey as null since this is only used in commands
    public boolean addLevel(Player player, int level, boolean playSounds) {

        int newLevel = level < 1 ? this.level : Math.min(this.level + level, Skill.MAX_LEVEL);
        int xpAmount = Skill.getNeededXP(this.level, newLevel);

        return addXP(player, xpAmount, null, true, playSounds);
    }

    public boolean setLevel(Player player, int level, boolean playSounds) {

        int newLevel = level < 1 ? 1 : Math.min(level, Skill.MAX_LEVEL);
        int xpAmount = Skill.getNeededTotalXP(newLevel);

        return setXP(player, xpAmount, playSounds);
    }

    public void reset(Player player) { reset(player, true); }

    public void reset(Player player, boolean resetRewards) {

         level = 1;
         leftoverXP = 0;
         totalXP = 0;

         if (resetRewards) {

             claimedRewards = skill.getDefaultRewardArray();
             milestones = generateMilestones();
         }

         updatePlayerData(player);
    }

    void handleRareDrops(Player player, int amount, @Nullable ResourceLocation typeKey) {

        boolean manually = typeKey != null;

        double rareDropChance = Math.min(PoscardsSkillsCommonConfig.RARE_DROP_MULTIPLIER.get() * amount, 1.0D / 3.0D);

        if (new Random().nextDouble() < rareDropChance && manually && SkillData.of(player).hasAdditional("rare_drops")) {

            LootTable lootTable = Objects.requireNonNull(player.level.getServer()).getLootTables().get(PoscardsSkills.asResource("gameplay/rare_drop"));
            LootContext lootContext = new LootContext.Builder((ServerLevel) player.level)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .create(LootContextParamSets.GIFT);

            SimpleContainer container = new SimpleContainer(1);
            lootTable.fill(container, lootContext);

            ItemStack stack = container.getItem(0);
            player.getInventory().placeItemBackInInventory(stack.copy());
            player.displayClientMessage(PSComponents.rareDrop(stack, typeKey), false);
            playXPSound(player);

            if (player instanceof ServerPlayer serverPlayer) PSCriteriaTriggers.RARE_DROP.trigger(serverPlayer, typeKey);
        }
    }

    void levelUp(Player player, int oldLevel, int newLevel, boolean displayMessage, boolean playSounds) {

        if (newLevel > Skill.MAX_LEVEL) return;

        level = newLevel;
        leftoverXP -= Skill.getNeededXP(oldLevel, newLevel);

        if (player instanceof ServerPlayer serverPlayer) PSCriteriaTriggers.LEVEL_UP.trigger(serverPlayer, newLevel);

        if (playSounds && PoscardsSkillsClientConfig.LEVEL_UP_SOUND.get()) playLevelUpSound(player);
        if (displayMessage) PSComponents.levelUpComponents(this, oldLevel, newLevel).forEach(component -> player.displayClientMessage(component, false));
    }

    public void claimRewards(Player player, int level) {

        claimedRewards[level] = true;
        updatePlayerData(player);
    }

    public boolean hasUnclaimedRewards() {

        for (SkillMilestone milestone : milestones) { if (milestone.canClaimRewards) return true; }
        return false;
    }

    public boolean isMaxLevel() { return level == Skill.MAX_LEVEL; }

    public int nextLevel() { return level + 1; }

    public SkillMilestone milestone(int level) { return milestones.get(level); }

    private void updatePlayerData(Player player) { SkillData.of(player).update(this); }

    private List<SkillMilestone> generateMilestones() {

        List<SkillMilestone> list = new ArrayList<>();

        for (int i = 0; i <= Skill.MAX_LEVEL; i++) { list.add(new SkillMilestone(this, i)); }
        return list;
    }

    private int calculateTotalXP() { return Skill.getNeededTotalXP(level) + leftoverXP; }

    private void playXPSound(Player player) {

        LocalPlayer localPlayer = PSUtils.getLocalPlayer(player);
        playSound(localPlayer, BaseModule.SoundEvents.XP_GAIN);
    }

    private void playLevelUpSound(Player player) {

        LocalPlayer localPlayer = PSUtils.getLocalPlayer(player);
        playSound(localPlayer, BaseModule.SoundEvents.LEVEL_UP);
    }

    private void playSound(LocalPlayer localPlayer, Supplier<SoundEvent> supplier) {

        Random random = new Random();

        float volume = random.nextFloat(0.5F, 0.75F);
        float pitch = random.nextFloat(1.0F, 1.25F);

        if (localPlayer != null) PSUtils.playLocalSound(localPlayer, supplier.get(), volume, pitch);
    }

}
