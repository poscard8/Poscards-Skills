package github.poscard8.poscardsskills.skill;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.skill.misc.RequisiteHolder;
import github.poscard8.poscardsskills.skill.misc.SkillRequisite;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Recipes unlocked by skill progression.
 * <p>{@link #skillKey}: Key of the skill. Skill recipes are loaded before skills,
 * so we can't pass a skill here.</p>
 * <p>{@link #at}: The level the recipe is unlocked at.</p>
 * <p>{@link #group}: Group of the recipe. Utilized by mod configs, redundant for custom skills.</p>
 * <p>{@link #customText}: Names of the recipes are shown on unlocking and in the skill UI.
 * If undefined, name will be {@code "<item name> Recipe"}. See the wiki for more info.</p>
 * <p>{@link #input1}: First input. Cannot be empty.</p>
 * <p>{@link #input2}: Second input. Can be empty.</p>
 * <p>{@link #output}: Output item. Cannot be empty.</p>
 * <p>See the wiki for formatting.</p>
 */
public class SkillRecipe implements RequisiteHolder {


    public final @Nullable String group;
    public final @Nullable String customText;

    public final ItemStack input1;
    public final @Nullable ItemStack input2;
    public final ItemStack output;

    public final int at;

    final ResourceLocation skillKey;

    SkillRecipe(ResourceLocation skillKey, int at, @Nullable String group, @Nullable String customText, ItemStack input1, @Nullable ItemStack input2, ItemStack output) {

        this.skillKey = skillKey;
        this.at = at;
        this.group = group;
        this.customText = customText;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    /**
     * Values are sorted based on level, then skill position.
     */
    public static List<SkillRecipe> getValues() {

        List<SkillRecipe> recipes = new ArrayList<>();
        Map<Skill, SkillInstance> map = SkillData.emptySkillMap();
        int maxSkillLevel = PoscardsSkills.getSkillHandler().getMaxSkillLevel();

        for (int i = 0; i <= maxSkillLevel; i++) {

            for (Skill skill : PoscardsSkills.getSkillHandler().getValues()) {

                if (i <= skill.maxLevel) {

                    SkillMilestone milestone = map.get(skill).milestone(i);
                    recipes.addAll(milestone.recipes);
                }
            }
        }
        return recipes;
    }

    public static SkillRecipe fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        try {

            int at = GsonHelper.getAsInt(jsonObject, "at");

            String group = jsonObject.has("group") ? GsonHelper.getAsString(jsonObject, "group") : null;
            String customText = jsonObject.has("customText") ? GsonHelper.getAsString(jsonObject, "customText") : null;

            if (!shouldLoad(group)) return null;

            ItemStack input1 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "input1"));
            ItemStack input2 = jsonObject.has("input2") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "input2")) : null;
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

            return Skill.isValidLevel(at) ? new SkillRecipe(skillKey, at, group, customText, input1, input2, output) : null;

        } catch (Exception exception) { return null; }
    }

    /**
     * Enables the recipes to load based on config.
     */
    public static boolean shouldLoad(@Nullable String group) {

        if (group == null) return true;
        boolean shouldLoad = true;

        switch (group) {

            case "survival_utilities" -> shouldLoad = PoscardsSkillsCommonConfig.SURVIVAL_UTILITIES.get();
            case "decorative_blocks" -> shouldLoad = PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get();
            case "mod_enchantments" -> shouldLoad = PoscardsSkillsCommonConfig.MOD_ENCHANTMENTS.get();
            case "extra_progression" -> shouldLoad = PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get();
            case "mod_enchantments_and_extra_progression" -> shouldLoad = PoscardsSkillsCommonConfig.MOD_ENCHANTMENTS.get() && PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get();
            case "mod_enchantments_and_no_extra_progression" -> shouldLoad = PoscardsSkillsCommonConfig.MOD_ENCHANTMENTS.get() && !PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get();
            default -> {}
        }
        return shouldLoad;
    }

    /**
     * Checks if the player has the inputs or not.
     */
    public boolean canCraft(ServerPlayer player) {

        boolean hasInput1 = false;
        boolean hasInput2 = input2 == null;

        NonNullList<ItemStack> stacks = player.getInventory().items;

        for (ItemStack stack : stacks) {

            if (ItemStack.isSameItem(stack, input1) && stack.getCount() >= input1.getCount()) hasInput1 = true;
            if (input2 != null && ItemStack.isSameItem(stack, input2) && stack.getCount() >= input2.getCount()) hasInput2 = true;
        }

        return hasInput1 && hasInput2 && isUnlockedFor(player);
    }

    /**
     * Method for crafting.
     */
    public void craftSingle(ServerPlayer player) {

        if (player == null) return;

        if (canCraft(player)) {

            Inventory inventory = player.getInventory();

            Optional<ItemStack> optional1 = findInput1(player);
            Optional<ItemStack> optional2 = findInput2(player);

            boolean changedInput1 = optional1.isPresent();
            boolean changedInput2 = optional2.isPresent() || input2 == null;

            if (changedInput1 && changedInput2) {

                ItemStack newInput1 = optional1.get();
                newInput1.shrink(input1.getCount());

                if (input2 != null) {

                    ItemStack newInput2 = optional2.get();
                    newInput2.shrink(input2.getCount());
                }

                inventory.placeItemBackInInventory(assemble());
                PSCriteriaTriggers.SKILL_CRAFTING.trigger(player, assemble());
            }
        }
    }

    public void craftStack(ServerPlayer player) {

        int count = output.getMaxStackSize();
        boolean canCraft = canCraft(player);

        while (canCraft && count > 0) {

            craftSingle(player);
            count--;
            canCraft = canCraft(player);
        }
    }

    public ItemStack assemble() { return output.copy(); }

    public boolean isUnlockedFor(@Nullable ServerPlayer player) { return player != null && getRequisite().test(player); }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at, PoscardsSkillsCommonConfig.KEEP_SKILL_RECIPES.get()); }

    /**
     * Method to check if the player has the input. Also supports enchanted inputs.
     */
    protected boolean checkInput(ItemStack stack, @Nullable ItemStack input) {

        if (input == null) return true;
        boolean enchantmentCheck = true;

        if (input.isEnchanted() || input.getItem() instanceof EnchantedBookItem) {

            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {

                int inputLevel = PSUtils.getEnchantmentLevel(input, enchantment);
                int itemLevel = PSUtils.getEnchantmentLevel(stack, enchantment);

                if (inputLevel != 0 && inputLevel != itemLevel) {

                    enchantmentCheck = false;
                    break;
                }
            }
        }

        return ItemStack.isSameItem(input, stack) && stack.getCount() >= input.getCount() && enchantmentCheck;
    }

    /**
     * Finds the first input on the player's inventory.
     */
    protected Optional<ItemStack> findInput1(ServerPlayer player) {

        NonNullList<ItemStack> stacks = player.getInventory().items;

        for (ItemStack stack : stacks) {

            if (checkInput(stack, input1)) return Optional.of(stack);
        }
        return Optional.empty();
    }

    /**
     * Finds the second input on the player's inventory.
     */
    protected Optional<ItemStack> findInput2(ServerPlayer player) {

        if (input2 == null) return Optional.empty();
        NonNullList<ItemStack> stacks = player.getInventory().items;

        for (ItemStack stack : stacks) {

            if (checkInput(stack, input2)) return Optional.of(stack);
        }
        return Optional.empty();
    }

}
