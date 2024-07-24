package github.poscard8.poscardsskills.skill;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.skill.misc.Additional;
import github.poscard8.poscardsskills.skill.misc.ItemRequisite;
import github.poscard8.poscardsskills.skill.reward.Reward;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Explained in the wiki.
 */
public final class Skill {

    public static final int MAX_LEVEL = PoscardsSkillsCommonConfig.MAX_SKILL_LEVEL.get();
    public static final int TRUE_MAX_LEVEL = 200;
    public static final int TRUE_MIN_LEVEL = 1;

    public static final int[] XP_FOR_LEVEL = new int[]{0, 0, 25, 50, 100, 150, 200, 300, 400, 600, 800};

    public final int index;
    public final ResourceLocation key;
    public final ItemStack icon;
    public final ResourceLocation background;
    public final Attribute attribute;
    public final float attributeAmount;
    public final List<Reward> rewards;
    public final List<SkillRecipe> recipes;
    public final List<ItemRequisite> itemRequisites;
    public final List<Additional> additional;

    Skill(int index, ResourceLocation location, ItemStack icon, ResourceLocation background, Attribute attribute, float attributeAmount,
          List<Reward> rewards, List<SkillRecipe> recipes, List<ItemRequisite> itemRequisites, List<Additional> additional) {

        this.index = index;
        this.key = location;
        this.icon = icon;
        this.background = background;
        this.attribute = attribute;
        this.attributeAmount = attributeAmount;
        this.rewards = rewards;
        this.recipes = recipes;
        this.itemRequisites = itemRequisites;
        this.additional = additional;
    }

    public static Skill fromJsonObject(int index, ResourceLocation skillKey, JsonObject jsonObject) {

        ResourceLocation iconKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "icon"));
        ItemStack icon = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(iconKey)).getDefaultInstance();

        ResourceLocation backgroundArg = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "background"));

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

            JsonArray additionalArray = GsonHelper.getAsJsonArray(jsonObject, "recipes");

            for (JsonElement element : additionalArray) {

                JsonObject object = element.getAsJsonObject();
                SkillRecipe recipe = SkillRecipe.fromJsonObject(skillKey, object);
                if (recipe != null) recipeList.add(recipe);
            }
        }

        List<ItemRequisite> requisiteList = new ArrayList<>();
        if (jsonObject.has("locked_items")) {

            JsonArray additionalArray = GsonHelper.getAsJsonArray(jsonObject, "locked_items");

            for (JsonElement element : additionalArray) {

                JsonObject object = element.getAsJsonObject();
                ItemRequisite requisite = ItemRequisite.fromJsonObject(skillKey, object);
                if (requisite != null) requisiteList.add(requisite);
            }
        }

        List<Additional> additionalList = new ArrayList<>();
        if (jsonObject.has("additional")) {

            JsonArray additionalArray = GsonHelper.getAsJsonArray(jsonObject, "additional");

            for (JsonElement element : additionalArray) {

                JsonObject object = element.getAsJsonObject();
                Additional additional = Additional.fromJsonObject(skillKey, object);
                if (additional != null) additionalList.add(additional);
            }
        }

        return new Skill(index, skillKey, icon, background, attr, amount, rewardList, recipeList, requisiteList, additionalList);
    }

    @Nullable
    public static Skill byKey(ResourceLocation key) { return PoscardsSkills.getSkillHandler().byLocation(key).orElse(null); }

    public static int getNeededXP(int level) { return level <= 10 ? XP_FOR_LEVEL[level] : level <= 70 ? 1000 * (level - 10) : 3000 * (level - 50); }

    public static int getNeededXP(int oldLevel, int newLevel) {

        int XP = 0;

        for (int l = oldLevel + 1; l <= newLevel; l++) {

            XP += getNeededXP(l);
        }
        return XP;
    }

    public static int getNeededTotalXP(int level) { return getNeededXP(0, level); }

    public static boolean isValidLevel(int level) { return level >= TRUE_MIN_LEVEL && level <= TRUE_MAX_LEVEL; }

    public SkillHandler.SkillPosition position() { return PoscardsSkills.getSkillHandler().getOrCreatePosition(this); }

    public void addContents(JsonObject jsonObject) {

        List<Reward> rewardList = new ArrayList<>();
        if (jsonObject.has("rewards")) {

            JsonArray rewardArray = GsonHelper.getAsJsonArray(jsonObject, "rewards");

            for (JsonElement element : rewardArray) {

                JsonObject object = element.getAsJsonObject();
                Reward reward = Reward.fromJsonObject(key, object);
                if (reward != null) rewardList.add(reward);
            }
        }

        List<SkillRecipe> recipeList = new ArrayList<>();
        if (jsonObject.has("recipes")) {

            JsonArray additionalArray = GsonHelper.getAsJsonArray(jsonObject, "recipes");

            for (JsonElement element : additionalArray) {

                JsonObject object = element.getAsJsonObject();
                SkillRecipe recipe = SkillRecipe.fromJsonObject(key, object);
                if (recipe != null) recipeList.add(recipe);
            }
        }

        List<ItemRequisite> requisiteList = new ArrayList<>();
        if (jsonObject.has("locked_items")) {

            JsonArray additionalArray = GsonHelper.getAsJsonArray(jsonObject, "locked_items");

            for (JsonElement element : additionalArray) {

                JsonObject object = element.getAsJsonObject();
                ItemRequisite requisite = ItemRequisite.fromJsonObject(key, object);
                if (requisite != null) requisiteList.add(requisite);
            }
        }

        List<Additional> additionalList = new ArrayList<>();
        if (jsonObject.has("additional")) {

            JsonArray additionalArray = GsonHelper.getAsJsonArray(jsonObject, "additional");

            for (JsonElement element : additionalArray) {

                JsonObject object = element.getAsJsonObject();
                Additional additional = Additional.fromJsonObject(key, object);
                if (additional != null) additionalList.add(additional);
            }
        }

        rewards.addAll(rewardList);
        recipes.addAll(recipeList);
        itemRequisites.addAll(requisiteList);
        additional.addAll(additionalList);
    }

    boolean[] getDefaultRewardArray() {

        boolean[] array = new boolean[MAX_LEVEL + 1];
        int rewardCount;

        for (int i = 0; i <= MAX_LEVEL; i++) {

            rewardCount = 0;

            for (Reward reward : rewards) { if (reward.isAvailableFor(i)) rewardCount++; }
            array[i] = rewardCount == 0;
        }
        return array;
    }

    String indexAsString() {

        String[] letters = new String[]{"A", "B", "C", "D", "E"};

        return index <= 9 ? Integer.toString(index) : letters[index - 10];
    }

    @Override
    public boolean equals(Object object) { return object instanceof Skill skill && this.key.equals(skill.key); }

    @Override
    public String toString() { return key.toString(); }


}
