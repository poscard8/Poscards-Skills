package github.poscard8.poscardsskills.util.component;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.secret.Secret;
import github.poscard8.poscardsskills.secret.SecretData;
import github.poscard8.poscardsskills.secret.Secrets;
import github.poscard8.poscardsskills.skill.*;
import github.poscard8.poscardsskills.skill.misc.RequisiteHolder;
import github.poscard8.poscardsskills.skill.misc.Translation;
import github.poscard8.poscardsskills.util.PSUtils;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Utility class with almost every text used in the mod UI.
 */
@SuppressWarnings("unused")
public class PSComponents {

    static final ComponentHandler HANDLER = PoscardsSkills.getComponentHandler();
    static final ColorPalette PALETTE = HANDLER.getColorPalette();

    static final NumberFormat INTEGER_FORMAT = NumberFormat.getInstance(Minecraft.getInstance().getLocale());
    static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("##.#");
    static final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("##.##");
    static final DecimalFormat THREE_DECIMAL_PLACES = new DecimalFormat("##.###");

    public static List<Component> split(Component component) {

        List<Component> components = new ArrayList<>();
        String[] parts = component.getString().split("\n");
        Arrays.stream(parts).forEach(string -> components.add(Component.literal(string).withStyle(component.getStyle())));
        return components;
    }

    public static String format(int number) { return INTEGER_FORMAT.format(number); }

    static ChatFormatting description() { return PALETTE.colorOf(ColorPalette.Key.DESCRIPTION); }

    static ChatFormatting instructions() { return PALETTE.colorOf(ColorPalette.Key.INSTRUCTIONS); }

    static ChatFormatting modInstructions() { return PALETTE.colorOf(ColorPalette.Key.MOD_INSTRUCTIONS); }

    static ChatFormatting allow() { return PALETTE.colorOf(ColorPalette.Key.ALLOW); }

    static ChatFormatting deny() { return PALETTE.colorOf(ColorPalette.Key.DENY); }

    static ChatFormatting skillAndXP() { return PALETTE.colorOf(ColorPalette.Key.SKILL_AND_XP); }

    static ChatFormatting skillUI() { return PALETTE.colorOf(ColorPalette.Key.SKILL_UI); }

    static ChatFormatting reward() { return PALETTE.colorOf(ColorPalette.Key.REWARD); }

    static ChatFormatting recipe() { return PALETTE.colorOf(ColorPalette.Key.RECIPE); }

    static ChatFormatting item() { return PALETTE.colorOf(ColorPalette.Key.ITEM); }

    static ChatFormatting experience() { return PALETTE.colorOf(ColorPalette.Key.EXPERIENCE); }

    static ChatFormatting barEmpty() { return PALETTE.colorOf(ColorPalette.Key.BAR_EMPTY); }

    static ChatFormatting barFull() { return PALETTE.colorOf(ColorPalette.Key.BAR_FULL); }

    static ChatFormatting filler() { return PALETTE.colorOf(ColorPalette.Key.FILLER); }

    static ChatFormatting splash() { return PALETTE.colorOf(ColorPalette.Key.SPLASH); }

    static ChatFormatting misc() { return PALETTE.colorOf(ColorPalette.Key.MISC); }

    public static MutableComponent space() { return Component.literal(" "); }

    public static MutableComponent longSpace() { return Component.literal("    "); }

    public static MutableComponent longerSpace() { return Component.literal("        "); }

    public static MutableComponent newLine() { return CommonComponents.NEW_LINE.copy(); }

    public static Component fillerLine() { return Component.literal(" ".repeat(70)).withStyle(filler(), ChatFormatting.STRIKETHROUGH); }

    public static Component modName() { return Component.translatable("generic.poscardsskills.name"); }

    public static Component level() { return Component.translatable("generic.poscardsskills.level"); }

    public static Component levelUp() { return Component.translatable("popup.poscardsskills.level_up").withStyle(misc(), ChatFormatting.BOLD); }

    public static Component ascension() { return Component.translatable("popup.poscardsskills.ascension").withStyle(misc(), ChatFormatting.BOLD); }

    public static Component ascensionSuccess() { return Component.translatable("generic.poscardsskills.ascension_success").withStyle(skillUI()); }

    public static Component stats() { return Component.translatable("label.poscardsskills.stats").withStyle(description()); }

    public static Component tripleQuestionMark() { return Component.translatable("???").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC); }

    public static Component singleUnclaimedMark() { return Component.literal("!").withStyle(reward(), ChatFormatting.BOLD); }

    public static Component doubleUnclaimedMark() { return Component.literal(" !!").withStyle(reward(), ChatFormatting.BOLD); }

    public static Component playerName(Player player) { return player.getDisplayName().copy().withStyle(misc()); }

    public static Component uiTitle() { return Component.translatable("generic.poscardsskills.skills"); }

    public static Component skillCrafting() { return Component.translatable("generic.poscardsskills.skill_crafting"); }

    public static Component attributeBuffs() { return Component.translatable("label.poscardsskills.attribute_buffs").withStyle(description()); }

    public static Component rewards() { return Component.translatable("label.poscardsskills.rewards").withStyle(description()); }

    public static Component claimRewards() { return Component.translatable("generic.poscardsskills.claim_rewards").withStyle(modInstructions()); }

    public static Component locked() { return Component.translatable("generic.poscardsskills.locked").withStyle(deny()); }

    public static Component unlocked() { return Component.translatable("generic.poscardsskills.unlocked").withStyle(allow()); }

    public static Component back() { return Component.translatable("generic.poscardsskills.back"); }

    public static Component secretTitle() { return Component.translatable("generic.poscardsskills.secrets"); }

    public static Component ascensionTitle() { return Component.translatable("generic.poscardsskills.ascension"); }

    public static Component skillTitle() { return Component.translatable("generic.poscardsskills.skill").withStyle(ChatFormatting.DARK_GRAY); }

    public static Component progress() { return Component.translatable("generic.poscardsskills.progress").withStyle(ChatFormatting.DARK_GRAY); }

    public static Component journey() { return Component.translatable("generic.poscardsskills.journey").withStyle(misc()); }

    public static Component comingSoon() { return Component.translatable("generic.poscardsskills.coming_soon").withStyle(instructions()); }

    public static Component moreComingSoon() { return Component.translatable("generic.poscardsskills.more_coming_soon").withStyle(instructions()); }

    public static Component craftSingle() { return Component.translatable("generic.poscardsskills.craft_single"); }

    public static Component craftStack() { return Component.translatable("generic.poscardsskills.craft_stack"); }

    public static Component skill(SkillInstance instance) { return skill(instance.skill); }

    public static Component skill(Skill skill) {

        Translation translation = skill.translation;
        String translated = translation.getName();
        if (translated != null) return Component.literal(translated).withStyle(skillAndXP());

        return Component.translatable(String.format("skill.%s.%s", skill.key.getNamespace(), skill.key.getPath())).withStyle(skillAndXP());
    }

    public static Component skillDescription(SkillInstance instance) { return skillDescription(instance.skill); }

    public static Component skillDescription(Skill skill) {

        Translation translation = skill.translation;
        String translated = translation.getDescription();
        if (translated != null) return Component.literal(translated).withStyle(description());

        return Component.translatable(String.format("skill.%s.%s.desc", skill.key.getNamespace(), skill.key.getPath())).withStyle(description());
    }

    public static Component level(int level) { return level(level, skillAndXP()); }

    public static Component level(int level, ChatFormatting... modifiers) {

        return HANDLER.getNumeralType() == NumeralType.ROMAN ? RomanNumeral.of(level).component().withStyle(modifiers) : Component.literal(Integer.toString(level)).withStyle(modifiers);
    }

    public static Component skillWithLevel(SkillInstance instance) { return skillWithLevel(instance.skill, instance.level); }

    public static Component skillWithLevel(SkillInstance instance, ChatFormatting... modifiers) { return skill(instance).copy().withStyle(modifiers).append(space()).append(level(instance.level, modifiers)); }

    public static Component skillWithLevel(Skill skill, int level) { return skill(skill).copy().append(space()).append(level(level)); }

    public static Component xpGain(int amount, SkillInstance instance) {

        MutableComponent prefix = Component.literal(String.format("+%d ", amount)).withStyle(skillAndXP());
        Component skillName = skill(instance);
        Component suffix;

        if (HANDLER.getProgressMessageType() == ProgressMessageType.PERCENTILE) {

            float percentage = (float) 100 * instance.xp / Skill.getNeededXP(instance.nextLevel());
            String formattedPercentage = TWO_DECIMAL_PLACES.format(percentage);
            Component percentageComponent = Component.translatable("generic.poscardsskills.percentage", formattedPercentage);
            suffix = Component.literal(String.format(" (%s)", percentageComponent.getString()));

        } else {

            String xpString = format(instance.xp);
            String neededXPString = format(Skill.getNeededXP(instance.nextLevel()));
            suffix = Component.literal(String.format(" (%s/%s)", xpString, neededXPString));
        }

        return instance.isMaxLevel() ? prefix.append(skillName) : prefix.append(skillName).append(suffix);
    }

    public static Component levelUpIndicator(Component prefix, int oldLevel, int newLevel) { return levelUpIndicator(prefix, Style.EMPTY.withBold(true).withColor(skillUI()), oldLevel, newLevel); }

    public static Component levelUpIndicator(Component prefix, Style newLevelStyle, int oldLevel, int newLevel) {

        MutableComponent oldLevelComponent = Component.literal(String.format("%s %s -> ", prefix.getString(), level(oldLevel).getString())).withStyle(description());
        Component newLevelComponent = level(newLevel).copy().withStyle(newLevelStyle);

        return oldLevelComponent.append(newLevelComponent);
    }

    public static Component attribute(Skill skill) { return attribute(skill.attribute); }

    public static Component attribute(Attribute attribute) { return HANDLER.getAttributeComponent(attribute); }

    public static Component attributeWithAmount(Skill skill) { return attributeWithAmount(skill, 1); }

    public static Component attributeWithAmount(Skill skill, int multiplier) {

        float amount = skill.attributeAmount * multiplier;
        return attributeWithAmount(skill.attribute, amount);
    }

    public static Component attributeWithAmount(Attribute attribute, double amount) {

        boolean percentage = HANDLER.getAttributeStyle(attribute).percentage;

        Style style = attribute(attribute).getStyle();
        MutableComponent sign = amount >= 0 ? Component.literal("+").withStyle(style) : Component.empty();

        Component amountComponent = percentage ?
                Component.translatable("generic.poscardsskills.percentage", THREE_DECIMAL_PLACES.format(amount)).withStyle(style) :
                Component.literal(THREE_DECIMAL_PLACES.format(amount)).withStyle(style);

        return sign.append(amountComponent).append(space()).append(attribute(attribute));
    }

    public static Component item(Item item, int count) { return item(item, count, item()); }

    public static Component item(Item item, int count, ChatFormatting color) {

        return Component.translatable("generic.poscardsskills.item_stack", count, item.getDescription().getString()).withStyle(color);
    }

    public static Component itemStack(ItemStack stack, boolean claimed, boolean unlocked) {

        boolean keepClaimedRewards = PoscardsSkillsCommonConfig.KEEP_CLAIMED_REWARDS.get();
        ChatFormatting color = claimed && keepClaimedRewards ? description() : item();
        return !claimed && unlocked ? itemStack(stack, color).copy().append(doubleUnclaimedMark()) : itemStack(stack, color);
    }

    public static Component itemStack(ItemStack stack) { return itemStack(stack, item()); }

    public static Component itemStack(ItemStack stack, ChatFormatting... colors) {

        Component name = stack.getHoverName().copy().withStyle(colors);

        if (stack.getItem() instanceof EnchantedBookItem) {

            Map<Enchantment, Integer> enchantmentMap = PSUtils.getEnchantmentLevel(stack);

            if (enchantmentMap.size() == 1) {

                Enchantment enchantment = new ArrayList<>(enchantmentMap.keySet()).get(0);
                int level = enchantmentMap.getOrDefault(enchantment, 1);

                name = Component.translatable("generic.poscardsskills.enchanted_book", enchantment.getFullname(level).copy().withStyle(colors)).withStyle(colors);
            }
        }

        return Component.translatable("generic.poscardsskills.item_stack", stack.getCount(), name.getString()).withStyle(colors);
    }

    public static Component experience(int amount, boolean claimed, boolean unlocked) {

        boolean keepClaimedRewards = PoscardsSkillsCommonConfig.KEEP_CLAIMED_REWARDS.get();
        ChatFormatting color = claimed && keepClaimedRewards ? description() : experience();
        return !claimed && unlocked ? experience(amount, color).copy().append(doubleUnclaimedMark()) : experience(amount, color);
    }

    public static Component experience(int amount) { return experience(amount, experience()); }

    public static Component experience(int amount, ChatFormatting... colors) {

        String string = format(amount) + " ";
        return Component.literal(string).withStyle(colors).append(Component.translatable("generic.poscardsskills.experience").withStyle(colors));
    }

    public static Component skillRecipe(SkillRecipe skillRecipe) {

        if (skillRecipe.customText != null) {

            if (skillRecipe.customText.equals("empty")) return Component.empty();
            return Component.translatable(skillRecipe.customText).withStyle(recipe());

        } else {

            String itemName = skillRecipe.output.getHoverName().getString();
            return Component.translatable("generic.poscardsskills.recipe", itemName).withStyle(recipe());
        }
    }

    public static Component legacySuccess(int legacy) { return Component.translatable("generic.poscardsskills.legacy_success", format(legacy)).withStyle(skillUI()); }

    public static Component rarity(Rarity rarity) {

        ResourceLocation location = ResourceLocation.tryParse(rarity.name());
        assert location != null;
        return Component.translatable(String.format("rarity.%s.%s", location.getNamespace(), location.getPath())).withStyle(rarity.getStyleModifier());
    }

    public static Component totalXP(SkillData skillData) {

        String xp = format(skillData.getTotalXP());
        return Component.translatable("label.poscardsskills.total_xp").withStyle(description())
                .append(Component.literal(xp).withStyle(skillAndXP()));
    }

    public static Component totalProgress(SkillData skillData) {

        int totalXP = 0;
        int neededTotalXP = 0;

        for (SkillInstance instance : skillData.skillMap.values()) {

            totalXP += Math.min(instance.totalXP(), Skill.getNeededTotalXP(instance.maxLevel()));
            neededTotalXP += Skill.getNeededTotalXP(instance.maxLevel());
        }

        float ratio = (float) (totalXP) / neededTotalXP * 100;
        ChatFormatting color = ratio >= 100 ? allow() : skillAndXP();

        Component numberComponent = Component.translatable("generic.poscardsskills.percentage", THREE_DECIMAL_PLACES.format(ratio)).withStyle(color);
        return Component.translatable("label.poscardsskills.total_progress").withStyle(description()).append(numberComponent);
    }

    public static Component avgSkillLevel(SkillData skillData) {

        int total = 0;
        for (SkillInstance instance : skillData.skillMap.values()) total += instance.level;

        float average = (float) total / skillData.skillMap.size();
        ChatFormatting color = average >= skillData.getMaxAvgLevel() ? allow() : skillAndXP();

        Component numberComponent = Component.literal(TWO_DECIMAL_PLACES.format(average)).withStyle(color);
        return Component.translatable("label.poscardsskills.average_skill_level").withStyle(description()).append(numberComponent);
    }

    public static Component ascensions(SkillData skillData) {

        int count = skillData.ascensions;
        String xp = format(count);
        ChatFormatting color = count > 0 ? allow() : skillAndXP();

        return Component.translatable("label.poscardsskills.ascensions").withStyle(description())
                .append(Component.literal(xp).withStyle(color));
    }

    public static Component progressBar(SkillInstance instance) {

        float ratio = (float) instance.xp / Skill.getNeededXP(instance.nextLevel());

        int completeLines = Math.round(25 * ratio);
        int incompleteLines = 25 - completeLines;

        MutableComponent bar = Component.literal(" ".repeat(completeLines)).withStyle(barFull(), ChatFormatting.STRIKETHROUGH)
                .append(Component.literal(" ".repeat(incompleteLines)).withStyle(barEmpty(), ChatFormatting.STRIKETHROUGH));

        Component percentage = Component.translatable("generic.poscardsskills.percentage", ONE_DECIMAL_PLACE.format(ratio * 100))
                .withStyle(Style.EMPTY.withStrikethrough(false).withColor(skillAndXP()));

        return bar.append(space().withStyle(Style.EMPTY.withStrikethrough(false))).append(percentage);
    }

    public static Component requisite(RequisiteHolder holder) { return requisite(holder, deny()); }

    public static Component requisite(RequisiteHolder holder, ChatFormatting... colors) {

        String skillName = skillWithLevel(holder.getRequisite().skill(), holder.getRequisite().level()).getString();
        return Component.translatable("generic.poscardsskills.requisite", skillName).withStyle(colors);
    }

    public static Component itemRequisite(RequisiteHolder holder) { return itemRequisite(holder, deny()); }

    public static Component itemRequisite(RequisiteHolder holder, ChatFormatting... colors) {

        String skillName = skillWithLevel(holder.getRequisite().skill(), holder.getRequisite().level()).getString();
        return Component.translatable("generic.poscardsskills.item_requisite", skillName).withStyle(colors);
    }

    public static Component secretCounter(SecretData secretData, boolean brackets) {

        int count = secretData.getSecretCount();
        int total = Secrets.getTotalCount();

        ChatFormatting primaryColor = count >= total ? allow() : skillAndXP();
        ChatFormatting secondaryColor = brackets ? barFull() : description();

        MutableComponent leftBracket = Component.literal("[").withStyle(secondaryColor).withStyle(style -> style.withBold(false));
        MutableComponent countComponent = Component.literal(format(count)).withStyle(primaryColor).withStyle(style -> style.withBold(false));
        Component slash = Component.literal("/").withStyle(secondaryColor).withStyle(style -> style.withBold(false));
        Component totalComponent = Component.literal(format(total)).withStyle(primaryColor).withStyle(style -> style.withBold(false));
        Component rightBracket = Component.literal("]").withStyle(secondaryColor).withStyle(style -> style.withBold(false));

        return brackets ?
                leftBracket.append(countComponent).append(slash).append(totalComponent).append(rightBracket) :
                countComponent.append(slash).append(totalComponent);
    }

    public static Component secretName(Secret secret) {

        int index = secret.getIndex() + 1;
        MutableComponent name = Component.translatable("generic.poscardsskills.secret").withStyle(style -> style.withItalic(false).withColor(reward()));

        if (index <= 0) return name;

        Component level = level(index, reward()).copy().withStyle(style -> style.withItalic(false));
        return name.append(space()).append(level);
    }

    public static Component secrets(SkillData skillData) {

        MutableComponent secrets = Component.translatable("label.poscardsskills.secrets").withStyle(description());
        return secrets.append(secretCounter(skillData.secretData, false));
    }

    public static Component chance(float chance) {

        float percentageChance = chance * 100;
        String string = TWO_DECIMAL_PLACES.format(percentageChance);
        return Component.translatable("generic.poscardsskills.percentage", string).withStyle(Style.EMPTY.withItalic(false).withColor(misc()));
    }

    public static Component openSkillMenu() {

        MutableComponent first = Component.translatable("prefix.poscardsskills.open_skill_menu").withStyle(instructions());
        Component second = PoscardsSkills.KEY_POSCARDS_SKILLS_MENU.getTranslatedKeyMessage().plainCopy().withStyle(description());
        Component third = Component.translatable("suffix.poscardsskills.open_skill_menu").withStyle(instructions());

        return first.append(second).append(third);
    }

    public static Component holdShift() {

        MutableComponent first = Component.translatable("prefix.poscardsskills.hold_shift").withStyle(instructions());
        Component second = Component.translatable("generic.poscardsskills.shift").withStyle(description());
        Component third = Component.translatable("suffix.poscardsskills.hold_shift").withStyle(instructions());

        return first.append(second).append(third);
    }

    public static Component chestLuck(int rolls) {

        MutableComponent popup = Component.translatable("popup.poscardsskills.chest_luck").withStyle(reward(), ChatFormatting.BOLD);
        Component description;

        switch (rolls) {

            case 2 -> description = Component.translatable("generic.poscardsskills.chest_luck_double").withStyle(Style.EMPTY.withBold(false).withColor(filler()));
            case 3 -> description = Component.translatable("generic.poscardsskills.chest_luck_triple").withStyle(Style.EMPTY.withBold(false).withColor(filler()));
            default -> description = Component.translatable("generic.poscardsskills.chest_luck", format(rolls)).withStyle(Style.EMPTY.withBold(false).withColor(filler()));
        }

        return popup.append(description);
    }

    public static Component splashComponent() {

        int index = new Random().nextInt(1, 26);
        String key = "splash.poscardsskills." + index;

        return Component.translatable(key).withStyle(splash());
    }

    public static List<Component> secretComponents(SecretData secretData, Secret secret, boolean manually) {

        List<Component> components = new ArrayList<>();

        MutableComponent popup = Component.translatable("popup.poscardsskills.secret").withStyle(reward(), ChatFormatting.BOLD);
        Component description = Component.translatable("generic.poscardsskills.secret_found").withStyle(Style.EMPTY.withBold(false).withColor(filler()));
        Component counter = secretCounter(secretData, true);

        Component firstLine = popup.append(description).append(counter);
        components.add(firstLine);
        if (!manually) return components;

        MutableComponent rewardComponent1 = Component.translatable("prefix.poscardsskills.reward").withStyle(filler());
        Component rewardComponent2 = itemStack(secret.getRewardItem());
        Component rewardComponent3 = Component.translatable("suffix.poscardsskills.reward").withStyle(filler());

        Component secondLine = rewardComponent1.append(rewardComponent2).append(rewardComponent3);
        components.add(secondLine);

        return components;
    }

    public static List<Component> ascensionComponents(SkillData skillData, Container container) {

        List<Component> components = new ArrayList<>();
        List<Component> itemComponents = new ArrayList<>();
        Object2IntMap<Item> itemMap = new Object2IntArrayMap<>();
        int itemIndex = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {

            ItemStack stack = container.getItem(i);
            Item item = stack.getItem();
            if (stack.isEmpty()) continue;

            int count = itemMap.getOrDefault(item, 0);
            itemMap.put(item, count + stack.getCount());
        }

        for (Item item : itemMap.keySet()) {

            int count = itemMap.getInt(item);
            if (count > 0) {

                ChatFormatting color = itemIndex % 2 == 0 ? item() : skillAndXP();
                itemComponents.add(longerSpace().append(item(item, count, color)));
                itemIndex++;
            }
        }

        components.add(fillerLine());

        components.add(longSpace().append(ascension()));
        components.add(longSpace().append(ascensionSuccess()));
        components.add(longSpace().append(space()));
        components.add(longSpace().append(stats()));
        components.add(longerSpace().append(totalXP(skillData)));
        components.add(longerSpace().append(totalProgress(skillData)));
        components.add(longerSpace().append(avgSkillLevel(skillData)));
        components.add(space());
        components.add(longSpace().append(rewards()));
        components.addAll(itemComponents);

        if (skillData.getLegacy() > 0) {

            components.add(space());
            components.add(longSpace().append(legacySuccess(skillData.getLegacy())));
            components.add(longSpace().append(levelUpIndicator(level(), Style.EMPTY.withBold(true).withColor(reward()), 1, skillData.getLegacy() + 1)));
        }

        components.add(fillerLine());

        return components;
    }

    public static List<Component> statComponents(SkillData skillData) {

        if (skillData == null) return new ArrayList<>();

        List<Component> components = new ArrayList<>();
        Map<Attribute, Float> map = new HashMap<>();

        components.add(playerName(skillData.player));
        components.add(space());
        components.add(totalProgress(skillData));
        components.add(avgSkillLevel(skillData));
        components.add(ascensions(skillData));
        components.add(space());
        components.add(attributeBuffs());

        for (Skill skill : PoscardsSkills.getSkillHandler().getValues()) {

            Attribute attribute = skill.attribute;
            Float existingValue = map.getOrDefault(attribute, 0.0F);

            map.put(attribute, existingValue + skillData.getSkill(skill).level * skill.attributeAmount);
        }

        for (Attribute attribute : map.keySet()) {

            Component component = attributeWithAmount(attribute, map.get(attribute));
            components.add(component);
        }
        return components;
    }

    public static List<Component> journeyComponents(SkillData skillData) {

        List<Component> components = new ArrayList<>();

        components.add(journey());
        components.add(space());
        components.add(secrets(skillData));
        components.add(moreComingSoon());
        components.add(space());
        components.add(attributeBuffs());

        int legacy = (int) Math.round(skillData.secretData.getAttributeModifier().getValue().getAmount());
        components.add(attributeWithAmount(PSAttributes.LEGACY.get(), legacy));

        return components;
    }

    public static List<Component> skillInstanceComponents(SkillInstance instance) {

        List<Component> components = new ArrayList<>();

        components.add(skillWithLevel(instance, skillUI()));

        ResourceLocation location = instance.skill.key;
        Component description = skillDescription(instance);
        if (HANDLER.hasSkillDescriptions()) components.addAll(split(description));

        components.add(space());

        if (instance.isMaxLevel()) {

            String xp = format(instance.totalXP());
            Component totalXPComponent = Component.translatable("label.poscardsskills.total_xp").withStyle(description())
                    .append(Component.literal(xp).withStyle(skillAndXP()));

            components.add(totalXPComponent);

        } else {

            String level = level(instance.nextLevel()).getString();
            Component textComponent = Component.translatable("label.poscardsskills.progress_to_level", level).withStyle(description());

            components.add(textComponent);
            components.add(progressBar(instance));
        }

        Component clickComponent = Component.translatable("generic.poscardsskills.click_to_view").withStyle(modInstructions());

        components.add(space());
        components.add(clickComponent);
        return components;
    }

    public static List<Component> milestoneComponents(SkillMilestone milestone) {

        List<Component> components = new ArrayList<>();
        components.add(skillWithLevel(milestone.skill, milestone.level));
        components.add(rewards());

        for (SkillRecipe recipe : milestone.recipes) {

            Component skillRecipeComponent = skillRecipe(recipe);
            if (!skillRecipeComponent.equals(Component.empty())) components.add(space().append(skillRecipe(recipe)));
        }

        if (milestone.hasRecipes()) components.add(space());

        components.add(space().append(attributeWithAmount(milestone.skill)));

        for (ItemStack stack : milestone.itemRewards) components.add(space().append(itemStack(stack, milestone.claimed, milestone.isUnlocked())));
        if (milestone.hasXPReward()) components.add(space().append(experience(milestone.xpReward, milestone.claimed, milestone.isUnlocked())));

        components.add(space());

        if (milestone.canClaimRewards()) components.add(claimRewards());
        components.add(milestone.state.getComponent(milestone.instance));
        return components;
    }

    public static List<Component> miscComponents(SkillInstance instance, int oldLevel, int newLevel) {

        List<Component> components = new ArrayList<>();

        List<SkillRecipe> recipes = new ArrayList<>();

        for (int i = oldLevel + 1; i <= newLevel; i++) {

            recipes.addAll(instance.milestone(i).recipes);
        }

        recipes.forEach(recipe -> {

            Component skillRecipeComponent = skillRecipe(recipe);
            if (!skillRecipeComponent.equals(Component.empty())) components.add(longSpace().append(skillRecipe(recipe)));
        });

        if (!recipes.isEmpty()) components.add(space());

        return components;
    }

    @SuppressWarnings("ALL")
    public static List<Component> rewardComponents(SkillInstance instance, int oldLevel, int newLevel) {

        List<SkillRecipe> recipes = new ArrayList<>();
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
        }

        if (lastItemStack != null) {

            Component moreComponent = Component.translatable("suffix.poscardsskills.more");
            itemStackComponent = stackCount > 1 ? Optional.of(itemStack(lastItemStack).copy().append(moreComponent)) : Optional.of(itemStack(lastItemStack));
        }

        if (xpCount != 0) experienceComponent = Optional.of(experience(xpCount));

        List<Component> components = new ArrayList<>();

        if (itemStackComponent.isPresent()) components.add(longSpace().append(itemStackComponent.get()));
        if (experienceComponent.isPresent()) components.add(longSpace().append(experienceComponent.get()));
        return components;
    }

    public static List<Component> levelUpComponents(SkillInstance instance, int oldLevel, int newLevel, boolean manually) {

        if (HANDLER.getLevelUpMessageType() == LevelUpMessageType.NONE) { return List.of(); }

        List<Component> components = new ArrayList<>();

        components.add(fillerLine());
        components.add(longSpace().append(levelUp()));
        components.add(longSpace().append(levelUpIndicator(skill(instance), oldLevel, newLevel)));

        if (HANDLER.getLevelUpMessageType() == LevelUpMessageType.LONG) {

            components.add(space());
            components.addAll(miscComponents(instance, oldLevel, newLevel));
            components.add(longSpace().append(attributeWithAmount(instance.skill, newLevel - oldLevel)));
            components.addAll(rewardComponents(instance, oldLevel, newLevel));
        }

        components.add(fillerLine());

        if (oldLevel == 1) {

            components.add(openSkillMenu());

        } else {

            if (manually && HANDLER.hasSplashTexts()) {

                if (new Random().nextInt(2) == 0) components.add(splashComponent());
            }
        }
        return components;
    }

}
