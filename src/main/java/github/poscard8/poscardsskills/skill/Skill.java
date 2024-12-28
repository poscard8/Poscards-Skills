package github.poscard8.poscardsskills.skill;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.extension.Extension;
import github.poscard8.poscardsskills.skill.misc.ItemLock;
import github.poscard8.poscardsskills.skill.misc.Translation;
import github.poscard8.poscardsskills.skill.reward.Reward;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basics and JSON formatting are explained in the wiki.
 */
public final class Skill implements Comparable<Skill> {

    public static final ResourceLocation

        WOODCUTTING_KEY = PoscardsSkills.asResource("woodcutting"),
        MINING_KEY = PoscardsSkills.asResource("mining"),
        FARMING_KEY = PoscardsSkills.asResource("farming"),
        COMBAT_KEY = PoscardsSkills.asResource("combat"),
        EXPLORING_KEY = PoscardsSkills.asResource("exploring"),
        ENCHANTING_KEY = PoscardsSkills.asResource("enchanting");

    public static final String

        WOODCUTTING_KEY_STRING = "github.poscard8.poscardsskills:woodcutting",
        MINING_KEY_STRING = "github.poscard8.poscardsskills:mining",
        FARMING_KEY_STRING = "github.poscard8.poscardsskills:farming",
        COMBAT_KEY_STRING = "github.poscard8.poscardsskills:combat",
        EXPLORING_KEY_STRING = "github.poscard8.poscardsskills:exploring",
        ENCHANTING_KEY_STRING = "github.poscard8.poscardsskills:enchanting";

    public static final int

            TRUE_MAX_LEVEL = 500,
            TRUE_MIN_LEVEL = 1,

            MAX_SKILL_COUNT = 27,
            MAX_ROW = 2,
            MIN_ROW = 0,
            MAX_COLUMN = 8,
            MIN_COLUMN = 0,
            ROW_SIZE = 9;

    public static final int[] XP_FOR_LEVEL = new int[]{0, 0, 25, 50, 75, 100, 150, 200, 250, 300, 400};

    public final ResourceLocation key;

    @NotNull
    public ItemStack icon;

    @NotNull
    public ResourceLocation background;

    public final Translation translation;
    public final int row;
    public final int column;
    public int maxLevel;
    public final Attribute attribute;
    public final float attributeAmount;
    public final List<Reward> rewards;
    public final List<SkillRecipe> recipes;
    public final List<ItemLock> itemLocks;

    Skill(ResourceLocation location, @NotNull ItemStack icon, @NotNull ResourceLocation background, Translation translation, int row, int column, int maxLevel, Attribute attribute,
          float attributeAmount, List<Reward> rewards, List<SkillRecipe> recipes, List<ItemLock> itemLocks) {

        this.key = location;
        this.icon = icon;
        this.background = background;
        this.translation = translation;
        this.row = row;
        this.column = column;
        this.maxLevel = maxLevel;
        this.attribute = attribute;
        this.attributeAmount = attributeAmount;
        this.rewards = rewards;
        this.recipes = recipes;
        this.itemLocks = itemLocks;
    }

    @Nullable
    public static Skill fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        try {

            ResourceLocation iconKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "icon"));
            ItemStack icon = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(iconKey)).getDefaultInstance();

            ResourceLocation backgroundArg = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "background"));

            Translation translation;
            if (jsonObject.has("translation")) {

                JsonObject translationObject = GsonHelper.getAsJsonObject(jsonObject, "translation");
                translation = Translation.fromJsonObject(translationObject);

            } else translation = Translation.empty();

            int row = Mth.clamp(GsonHelper.getAsInt(jsonObject, "row"), MIN_ROW, MAX_ROW);
            int column = Mth.clamp(GsonHelper.getAsInt(jsonObject, "column"), MIN_COLUMN, MAX_COLUMN);
            int maxLevel = Mth.clamp(GsonHelper.getAsInt(jsonObject, "max_level"), TRUE_MIN_LEVEL, TRUE_MAX_LEVEL);

            assert backgroundArg != null;
            ResourceLocation background = new ResourceLocation(backgroundArg.getNamespace(), String.format("textures/%s.png", backgroundArg.getPath()));

            ResourceLocation attributeKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "attribute"));
            Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(attributeKey);
            float amount = GsonHelper.getAsFloat(jsonObject, "amount");

            List<Reward> rewardList = new ArrayList<>();
            if (jsonObject.has("rewards")) {

                JsonArray rewardArray = GsonHelper.getAsJsonArray(jsonObject, "rewards");

                for (JsonElement element : rewardArray) {

                    JsonObject object = element.getAsJsonObject();
                    Reward reward = Reward.fromJsonObject(skillKey, object);
                    if (reward != null) rewardList.add(reward);
                }
            }

            List<SkillRecipe> recipeList = new ArrayList<>();
            if (jsonObject.has("recipes")) {

                JsonArray recipeArray = GsonHelper.getAsJsonArray(jsonObject, "recipes");

                for (JsonElement element : recipeArray) {

                    JsonObject object = element.getAsJsonObject();
                    SkillRecipe recipe = SkillRecipe.fromJsonObject(skillKey, object);
                    if (recipe != null) recipeList.add(recipe);
                }
            }

            List<ItemLock> requisiteList = new ArrayList<>();
            if (jsonObject.has("locked_items")) {

                JsonArray requisiteArray = GsonHelper.getAsJsonArray(jsonObject, "locked_items");

                for (JsonElement element : requisiteArray) {

                    JsonObject object = element.getAsJsonObject();
                    ItemLock requisite = ItemLock.fromJsonObject(skillKey, object);
                    if (requisite != null) requisiteList.add(requisite);
                }
            }
            return new Skill(skillKey, icon, background, translation, row, column, maxLevel, attr, amount, rewardList, recipeList, requisiteList);

        } catch (Exception exception) { return null; }
    }

    @Nullable
    public static Skill byKey(ResourceLocation key) { return PoscardsSkills.getSkillHandler().byKey(key).orElse(null); }

    /**
     * XP needed to get to level <i>n</i> from <i>n-1</i>
     */
    public static int getNeededXP(int level) { return level <= 10 ? XP_FOR_LEVEL[level] : 500 * (level - 10); }

    /**
     * XP needed to get to level <i>n</i> from <i>m</i>
     */
    public static int getNeededXP(int oldLevel, int newLevel) {

        int XP = 0;

        for (int l = oldLevel + 1; l <= newLevel; l++) {

            XP += getNeededXP(l);
        }
        return XP;
    }

    /**
     * XP needed to get to level <i>x</i> from <i>0</i>
     */
    public static int getNeededTotalXP(int level) { return getNeededXP(0, level); }

    public static boolean isValidLevel(int level) { return level >= TRUE_MIN_LEVEL && level <= TRUE_MAX_LEVEL; }

    /**
     * Reward arrays are boolean arrays that determine if the player
     * collected the rewards of a certain level or not.
     */
    public boolean[] getDefaultRewardArray() {

        boolean[] array = new boolean[maxLevel + 1];
        int rewardCount;

        for (int i = 0; i <= maxLevel; i++) {

            rewardCount = 0;

            for (Reward reward : rewards) { if (reward.isAvailableFor(i)) rewardCount++; }
            array[i] = rewardCount == 0;
        }
        return array;
    }

    public int getPositionIndex() { return row * ROW_SIZE + column; }

    public String indexAsString() {

        String hex = Integer.toHexString(getPositionIndex()).toUpperCase();
        return getPositionIndex() >= 16 ? hex : "0" + hex;
    }

    public int getXPForCompletion() { return getNeededTotalXP(maxLevel); }

    public int getXPForMaxOut() { return Math.max(2000000, getXPForCompletion()); }

    public void addExtension(Extension extension) {

        if (extension.icon != null) icon = extension.icon;
        if (extension.background != null) background = extension.background;
        if (extension.maxLevel != null) maxLevel = extension.maxLevel;

        translation.add(extension.translation);
        rewards.addAll(extension.rewards);
        recipes.addAll(extension.recipes);
        itemLocks.addAll(extension.itemRequisites);

        PoscardsSkills.getXPSourceHandler().values().addAll(extension.xpSources);
    }

    @Override
    public int compareTo(@NotNull Skill other) { return getPositionIndex() - other.getPositionIndex(); }

    @Override
    public boolean equals(Object object) { return object instanceof Skill skill && this.key.equals(skill.key); }

    @Override
    public String toString() { return key.toString(); }


}
