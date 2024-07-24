package github.poscard8.poscardsskills.util.component;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.*;
import github.poscard8.poscardsskills.skill.misc.Additional;
import github.poscard8.poscardsskills.skill.misc.RequisiteHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@SuppressWarnings("unused")
public class PSComponents {

    private static final ComponentHandler HANDLER = PoscardsSkills.getComponentHandler();
    private static final ColorPalette PALETTE = HANDLER.getColorPalette();

    private static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("##.#");
    private static final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("##.##");
    private static final DecimalFormat THREE_DECIMAL_PLACES = new DecimalFormat("##.###");

    public static List<Component> split(Component component) {

        List<Component> components = new ArrayList<>();
        String[] parts = component.getString().split("\n");
        Arrays.stream(parts).forEach(string -> components.add(Component.literal(string).withStyle(component.getStyle())));
        return components;
    }

    public static MutableComponent space() { return Component.literal(" "); }

    public static MutableComponent longSpace() { return Component.literal("    "); }

    public static MutableComponent newLine() { return CommonComponents.NEW_LINE.copy(); }

    public static Component blueLine() { return Component.literal("-".repeat(45)).withStyle(PALETTE.colorOf(ColorPalette.Key.FILLER), ChatFormatting.BOLD); }

    public static Component levelUp() { return Component.translatable("generic.poscardsskills.level_up").withStyle(PALETTE.colorOf(ColorPalette.Key.MISC), ChatFormatting.BOLD); }

    public static Component tripleQuestionMark() { return Component.translatable("???").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC); }

    public static Component singleUnclaimedMark() { return Component.literal("!").withStyle(PALETTE.colorOf(ColorPalette.Key.REWARD), ChatFormatting.BOLD); }

    public static Component doubleUnclaimedMark() { return Component.literal(" !!").withStyle(PALETTE.colorOf(ColorPalette.Key.REWARD), ChatFormatting.BOLD); }

    public static Component skill(SkillInstance instance) { return skill(instance.skill); }

    public static Component skill(Skill skill) {

        return Component.translatable(String.format("skill.%s.%s", skill.key.getNamespace(), skill.key.getPath())).withStyle(PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP));
    }

    public static Component level(int level) { return level(level, PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP)); }

    public static Component level(int level, ChatFormatting... modifiers) {

        return HANDLER.getNumeralType() == NumeralType.ROMAN ? RomanNumeral.of(level).component().withStyle(modifiers) : Component.literal(String.format("%d", level)).withStyle(modifiers);
    }

    public static Component skillWithLevel(SkillInstance instance) { return skillWithLevel(instance.skill, instance.level); }

    public static Component skillWithLevel(SkillInstance instance, ChatFormatting... modifiers) { return skill(instance).copy().withStyle(modifiers).append(space()).append(level(instance.level, modifiers)); }

    public static Component skillWithLevel(Skill skill, int level) { return skill(skill).copy().append(space()).append(level(level)); }

    public static Component xpGain(int amount, SkillInstance instance) {

        MutableComponent prefix = Component.literal(String.format("+%d ", amount)).withStyle(PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP));
        Component skillName = skill(instance);
        Component suffix;

        if (HANDLER.getProgressMessageType() == ProgressMessageType.PERCENTILE) {

            float percentage = (float) 100 * instance.leftoverXP / Skill.getNeededXP(instance.nextLevel());
            String formattedPercentage = ONE_DECIMAL_PLACE.format(percentage);
            suffix = Component.literal(String.format(" (%s%s)", formattedPercentage, "%"));

        } else suffix = Component.literal(String.format(" (%d/%d)", instance.leftoverXP, Skill.getNeededXP(instance.nextLevel())));

        return instance.level == Skill.MAX_LEVEL ? prefix.append(skillName) : prefix.append(skillName).append(suffix);
    }

    public static Component attribute(Skill skill) { return attribute(skill.attribute); }

    public static Component attribute(Attribute attribute) {

        ChatFormatting color = HANDLER.getAttributeColor(attribute);
        String icon = HANDLER.getAttributeIcon(attribute);

        Component iconComponent = Component.literal(" " + icon).withStyle(color);
        return Component.translatable(attribute.getDescriptionId()).withStyle(color).append(iconComponent);
    }

    public static Component attributeWithAmount(Skill skill) { return attributeWithAmount(skill, 1); }

    public static Component attributeWithAmount(Skill skill, int multiplier) {

        float amount = skill.attributeAmount * multiplier;
        Style style = attribute(skill).getStyle();
        return Component.literal(String.format("+%s ", TWO_DECIMAL_PLACES.format(amount))).withStyle(style).append(attribute(skill));
    }

    public static Component itemStack(ItemStack stack) {

        return Component.literal(String.format("%dx ", stack.getCount())).withStyle(PALETTE.colorOf(ColorPalette.Key.ITEM)).append(Component.translatable(stack.getDescriptionId())).withStyle(PALETTE.colorOf(ColorPalette.Key.ITEM));
    }

    public static Component itemStack(ItemStack stack, boolean canClaim) { return canClaim ? itemStack(stack).copy().append(doubleUnclaimedMark()) : itemStack(stack); }

    public static Component experience(int amount) {

        return Component.literal(String.format("%d ", amount)).withStyle(PALETTE.colorOf(ColorPalette.Key.EXPERIENCE)).append(Component.translatable("generic.poscardsskills.experience").withStyle(PALETTE.colorOf(ColorPalette.Key.EXPERIENCE)));
    }

    public static Component experience(int amount, boolean canClaim) { return canClaim ? experience(amount).copy().append(doubleUnclaimedMark()) : experience(amount); }

    public static Component skillRecipe(SkillRecipe skillRecipe) {

        String itemName = skillRecipe.output.getItem().getDescription().getString();
        return Component.translatable("generic.poscardsskills.recipe", itemName).withStyle(PALETTE.colorOf(ColorPalette.Key.RECIPE));
    }

    public static Component additional(Additional additional) { return Component.translatable(String.format("additional.%s", additional.key)).withStyle(PALETTE.colorOf(ColorPalette.Key.REWARD)); }

    public static Component rarity(Rarity rarity) {

        ResourceLocation location = ResourceLocation.tryParse(rarity.name());
        assert location != null;
        return Component.translatable(String.format("rarity.%s.%s", location.getNamespace(), location.getPath())).withStyle(rarity.getStyleModifier());
    }

    public static Component totalProgress(SkillData skillData) {

        int totalXP = 0;
        int neededTotalXP = 0;

        for (SkillInstance instance : skillData.skillMap.values()) {

            totalXP += Math.min(instance.totalXP, Skill.getNeededTotalXP(Skill.MAX_LEVEL));
            neededTotalXP += Skill.getNeededTotalXP(Skill.MAX_LEVEL);
        }

        float ratio = (float) (totalXP) / neededTotalXP * 100;
        ChatFormatting color = ratio >= 100 ? PALETTE.colorOf(ColorPalette.Key.ALLOW) : PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP);

        Component numberComponent = Component.literal(String.format("%s%s", THREE_DECIMAL_PLACES.format(ratio), "%")).withStyle(color);
        return Component.translatable("generic.poscardsskills.total_progress").withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION)).append(numberComponent);
    }

    public static Component avgSkillLevel(SkillData skillData) {

        int total = 0;
        for (SkillInstance instance : skillData.skillMap.values()) total += instance.level;

        float average = (float) total / skillData.skillMap.size();
        ChatFormatting color = average >= Skill.MAX_LEVEL ? PALETTE.colorOf(ColorPalette.Key.ALLOW) : PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP);

        Component numberComponent = Component.literal(TWO_DECIMAL_PLACES.format(average)).withStyle(color);
        return Component.translatable("generic.poscardsskills.average_skill_level").withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION)).append(numberComponent);
    }

    public static Component progressBar(SkillInstance instance) {

        float ratio = (float) instance.leftoverXP / Skill.getNeededXP(instance.nextLevel());

        int completeLines = Math.round(20 * ratio);
        int incompleteLines = 20 - completeLines;

        MutableComponent bar = Component.literal("-".repeat(completeLines)).withStyle(PALETTE.colorOf(ColorPalette.Key.BAR_FULL)).append(Component.literal("-".repeat(incompleteLines)).withStyle(PALETTE.colorOf(ColorPalette.Key.BAR_EMPTY)));
        Component percentage = Component.literal(String.format(" %s%s", ONE_DECIMAL_PLACE.format(ratio * 100), "%")).withStyle(PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP));
        return bar.append(percentage);
    }

    public static Component requisite(RequisiteHolder holder) { return requisite(holder, PALETTE.colorOf(ColorPalette.Key.DENY)); }

    public static Component requisite(RequisiteHolder holder, ChatFormatting... colors) {

        String skillName = skillWithLevel(holder.getRequisite().skill(), holder.getRequisite().level()).getString();
        return Component.translatable("generic.poscardsskills.requisite", skillName).withStyle(colors);
    }

    public static Component itemRequisite(RequisiteHolder holder) { return itemRequisite(holder, PALETTE.colorOf(ColorPalette.Key.DENY)); }

    public static Component itemRequisite(RequisiteHolder holder, ChatFormatting... colors) {

        String skillName = skillWithLevel(holder.getRequisite().skill(), holder.getRequisite().level()).getString();
        return Component.translatable("generic.poscardsskills.item_requisite", skillName).withStyle(colors);
    }

    public static Component rareDrop(ItemStack stack, ResourceLocation typeKey) {

        MutableComponent first = Component.translatable("generic.poscardsskills.rare_drop").withStyle(PALETTE.colorOf(ColorPalette.Key.REWARD), ChatFormatting.BOLD);
        Component second = Component.translatable("generic.poscardsskills.you_found").withStyle(Style.EMPTY.withBold(false).withColor(PALETTE.colorOf(ColorPalette.Key.FILLER)));
        Component third = itemStack(stack).copy().withStyle(style -> style.withBold(false)).append(space());
        Component fourth = Component.translatable(String.format("rare_drop.%s.%s", typeKey.getNamespace(), typeKey.getPath())).withStyle(Style.EMPTY.withBold(false).withColor(PALETTE.colorOf(ColorPalette.Key.FILLER)));

        return first.append(second).append(third).append(fourth);
    }

    public static Component range(int min, int max) {

        if (min > max) return range(max, min);
        else if (min == max) return Component.literal(Integer.toString(min)).withStyle(Style.EMPTY.withItalic(false).withColor(PALETTE.colorOf(ColorPalette.Key.MISC)));
        else return Component.literal(String.format("%d - %d", min, max)).withStyle(Style.EMPTY.withItalic(false).withColor(PALETTE.colorOf(ColorPalette.Key.MISC)));
    }

    public static Component openSkillMenu() {

        MutableComponent first = Component.translatable("generic.poscardsskills.open_skill_menu_prefix").withStyle(PALETTE.colorOf(ColorPalette.Key.INSTRUCTIONS));
        Component second = PoscardsSkills.KEY_SKILL_MENU.getTranslatedKeyMessage().plainCopy().withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION));
        Component third = Component.translatable("generic.poscardsskills.open_skill_menu_suffix").withStyle(PALETTE.colorOf(ColorPalette.Key.INSTRUCTIONS));

        return first.append(second).append(third);
    }

    public static Component holdShift() {

        MutableComponent first = Component.translatable("tooltip.poscardsskills.hold_shift_prefix").withStyle(PALETTE.colorOf(ColorPalette.Key.INSTRUCTIONS));
        Component second = Component.translatable("tooltip.poscardsskills.hold_shift").withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION));
        Component third = Component.translatable("tooltip.poscardsskills.hold_shift_suffix").withStyle(PALETTE.colorOf(ColorPalette.Key.INSTRUCTIONS));

        return first.append(second).append(third);
    }

    public static Component playerName(Player player) { return player.getDisplayName().copy().withStyle(PALETTE.colorOf(ColorPalette.Key.MISC)); }

    public static Component uiTitle() { return Component.translatable("ui.poscardsskills.title"); }

    public static Component skillCrafting() { return Component.translatable("ui.poscardsskills.skill_crafting"); }

    public static Component attributeBuffs() { return Component.translatable("generic.poscardsskills.attribute_buffs").withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION)); }

    public static Component rewards() { return Component.translatable("generic.poscardsskills.rewards").withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION)); }

    public static Component claimRewards() { return Component.translatable("generic.poscardsskills.claim_rewards").withStyle(PALETTE.colorOf(ColorPalette.Key.MOD_INSTRUCTIONS)); }

    public static Component locked() { return Component.translatable("generic.poscardsskills.locked").withStyle(PALETTE.colorOf(ColorPalette.Key.DENY)); }

    public static Component unlocked() { return Component.translatable("generic.poscardsskills.unlocked").withStyle(PALETTE.colorOf(ColorPalette.Key.ALLOW)); }

    public static Component back() { return Component.translatable("ui.poscardsskills.back"); }

    public static Component rareDrops() { return Component.translatable("ui.poscardsskills.rare_drops"); }

    public static Component craftSingle() { return Component.translatable("ui.poscardsskills.craft_single"); }

    public static Component craftStack() { return Component.translatable("ui.poscardsskills.craft_stack"); }

    public static List<Component> rareDropsDescriptionComponents() {

        Component description = Component.translatable("ui.poscardsskills.rare_drops_desc");
        return split(description);
    }

    public static List<Component> statComponents(SkillData skillData) {

        if (skillData == null) return new ArrayList<>();

        List<Component> components = new ArrayList<>();
        Map<Attribute, Float> map = new HashMap<>();

        components.add(playerName(skillData.player));
        components.add(space());
        components.add(totalProgress(skillData));
        components.add(avgSkillLevel(skillData));
        components.add(space());
        components.add(attributeBuffs());

        for (Skill skill : PoscardsSkills.getSkillHandler().getSortedValues()) {

            Attribute attribute = skill.attribute;
            Float existingValue = map.getOrDefault(attribute, 0.0F);

            map.put(attribute, existingValue + skillData.getSkill(skill).level * skill.attributeAmount);
        }

        for (Attribute attribute : map.keySet()) {

            Style style = attribute(attribute).getStyle();
            Component component = Component.literal(String.format("+%s ", TWO_DECIMAL_PLACES.format(map.get(attribute)))).withStyle(style).append(attribute(attribute));
            components.add(component);
        }
        return components;
    }

    public static List<Component> skillInstanceComponents(SkillInstance instance) {

        List<Component> components = new ArrayList<>();

        components.add(skillWithLevel(instance, PALETTE.colorOf(ColorPalette.Key.SKILL_UI)));

        ResourceLocation location = instance.skill.key;
        Component description = Component.translatable(String.format("skill.%s.%s.desc", location.getNamespace(), location.getPath())).withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION));
        if (HANDLER.hasSkillDescriptions()) components.addAll(split(description));

        components.add(space());

        if (instance.level >= Skill.MAX_LEVEL) {

            String xp = NumberFormat.getInstance().format(instance.totalXP);
            Component totalXPComponent = Component.translatable("generic.poscardsskills.total_xp").withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION))
                    .append(Component.literal(xp).withStyle(PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP)));

            components.add(totalXPComponent);

        } else {

            String level = level(instance.nextLevel()).getString();
            Component textComponent = Component.translatable("generic.poscardsskills.progress_to_level", level).withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION));

            components.add(textComponent);
            components.add(progressBar(instance));
        }

        Component clickComponent = Component.translatable("ui.poscardsskills.click_to_view").withStyle(PALETTE.colorOf(ColorPalette.Key.MOD_INSTRUCTIONS));

        components.add(space());
        components.add(clickComponent);
        return components;
    }

    public static List<Component> milestoneComponents(SkillMilestone milestone) {

        List<Component> components = new ArrayList<>();
        components.add(skillWithLevel(milestone.skill, milestone.level));
        components.add(rewards());

        for (SkillRecipe recipe : milestone.recipes) components.add(space().append(skillRecipe(recipe)));
        for (Additional additional : milestone.additional) components.add(space().append(additional(additional)));
        if (milestone.hasRecipesOrAdditional()) components.add(space());

        components.add(space().append(attributeWithAmount(milestone.skill)));

        for (ItemStack stack : milestone.itemRewards) components.add(space().append(itemStack(stack, milestone.canClaimRewards)));
        if (milestone.hasXPReward()) components.add(space().append(experience(milestone.xpReward, milestone.canClaimRewards)));

        components.add(space());

        if (milestone.canClaimRewards) components.add(claimRewards());
        components.add(milestone.state.getComponent(milestone.instance));
        return components;
    }

    public static List<Component> miscComponents(SkillInstance instance, int oldLevel, int newLevel) {

        List<Component> components = new ArrayList<>();

        List<SkillRecipe> recipes = new ArrayList<>();
        List<Additional> additional = new ArrayList<>();

        for (int i = oldLevel + 1; i <= newLevel; i++) {

            recipes.addAll(instance.milestone(i).recipes);
            additional.addAll(instance.milestone(i).additional);
        }

        recipes.forEach(recipe -> components.add(longSpace().append(skillRecipe(recipe))));
        additional.forEach(additional1 -> components.add(longSpace().append(additional(additional1))));
        if (recipes.size() + additional.size() > 0) components.add(space());

        return components;
    }

    @SuppressWarnings("ALL")
    public static List<Component> rewardComponents(SkillInstance instance, int oldLevel, int newLevel) {

        List<SkillRecipe> recipes = new ArrayList<>();
        List<Additional> additional = new ArrayList<>();
        ItemStack lastItemStack = null;

        int stackCount = 0;
        int xpCount = 0;

        Optional<Component> itemStackComponent = Optional.empty();
        Optional<Component> experienceComponent = Optional.empty();

        for (int i = oldLevel + 1; i <= newLevel; i++) {

            for (ItemStack stack : instance.milestone(i).itemRewards) {

                lastItemStack = stack;
                ++stackCount;
            }
            xpCount += instance.milestone(i).xpReward;

            instance.milestone(i).recipes.forEach(recipes::add);
            instance.milestone(i).additional.forEach(additional::add);
        }

        if (lastItemStack != null) {

            Component moreComponent = Component.translatable("generic.poscardsskills.more_rewards");
            itemStackComponent = stackCount > 1 ? Optional.of(itemStack(lastItemStack).copy().append(moreComponent)) : Optional.of(itemStack(lastItemStack));
        }

        if (xpCount != 0) experienceComponent = Optional.of(experience(xpCount));

        List<Component> components = new ArrayList<>();

        if (itemStackComponent.isPresent()) components.add(longSpace().append(itemStackComponent.get()));
        if (experienceComponent.isPresent()) components.add(longSpace().append(experienceComponent.get()));
        return components;
    }

    public static List<Component> levelUpComponents(SkillInstance instance, int oldLevel, int newLevel) {

        if (HANDLER.getLevelUpMessageType() == LevelUpMessageType.NONE) { return List.of(); }

        List<Component> components = new ArrayList<>();

        MutableComponent oldLevelDisplay = Component.literal(String.format("%s %s -> ", skill(instance).getString(), level(oldLevel).getString())).withStyle(PALETTE.colorOf(ColorPalette.Key.DESCRIPTION));
        Component newLevelDisplay = level(newLevel).copy().withStyle(PALETTE.colorOf(ColorPalette.Key.SKILL_UI), ChatFormatting.BOLD);
        Component levelDisplay = oldLevelDisplay.append(newLevelDisplay);

        components.add(blueLine());
        components.add(longSpace().append(levelUp()));
        components.add(longSpace().append(levelDisplay));

        if (HANDLER.getLevelUpMessageType() == LevelUpMessageType.LONG) {

            components.add(space());
            components.addAll(miscComponents(instance, oldLevel, newLevel));
            components.add(longSpace().append(attributeWithAmount(instance.skill, newLevel - oldLevel)));
            components.addAll(rewardComponents(instance, oldLevel, newLevel));
        }

        components.add(blueLine());
        if (oldLevel == 1) components.add(openSkillMenu());
        return components;
    }

}
