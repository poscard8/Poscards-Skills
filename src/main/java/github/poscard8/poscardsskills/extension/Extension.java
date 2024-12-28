package github.poscard8.poscardsskills.extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import github.poscard8.poscardsskills.skill.misc.ItemLock;
import github.poscard8.poscardsskills.skill.misc.Translation;
import github.poscard8.poscardsskills.skill.reward.Reward;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Class to modify existing skills.
 * <p>Extensions are loaded <b>after</b> skills and xp sources.</p>
 * <p>{@code fileKey} can be anything.</p>
 * <p>{@code icon}, {@code background}, {@code maxLevel} are nullable.
 * If they are null, they won't change the icon, background or the max level of the skill.</p>
 * <p>{@code translation}, {@code rewards}, {@code recipes}, {@code itemRequisites}, {@code xpSources}
 * can be left as empty.</p>
 * <p>Removing the skills' existing rewards, recipes, etc. is currently <b>not supported</b>.</p>
 * <p>See the wiki for the format.</p>
 */
public class Extension {

    public final ResourceLocation fileKey;

    @Nullable
    public final Skill target;

    @Nullable
    public final ItemStack icon;

    @Nullable
    public final ResourceLocation background;

    @Nullable
    public final Integer maxLevel;

    public final Translation translation;
    public final List<Reward> rewards;
    public final List<SkillRecipe> recipes;
    public final List<ItemLock> itemRequisites;
    public final List<ExperienceSource> xpSources;

    Extension(ResourceLocation fileKey) { this(fileKey, null, null, null, null, Translation.empty(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()); }

    Extension(ResourceLocation fileKey, @Nullable Skill target, @Nullable ItemStack icon, @Nullable ResourceLocation background, @Nullable Integer maxLevel,
              Translation translation, List<Reward> rewards, List<SkillRecipe> recipes, List<ItemLock> itemLocks, List<ExperienceSource> xpSources) {

        this.fileKey = fileKey;
        this.target = target;
        this.icon = icon;
        this.background = background;
        this.maxLevel = maxLevel;
        this.translation = translation;
        this.rewards = rewards;
        this.recipes = recipes;
        this.itemRequisites = itemLocks;
        this.xpSources = xpSources;
    }

    public static Extension fromJsonObject(ResourceLocation fileKey, JsonObject jsonObject) {

        ResourceLocation skillKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "target"));
        Skill skill = Skill.byKey(skillKey);

        if (skill == null) return new Extension(fileKey);

        ResourceLocation iconKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "icon"));
        ItemStack icon = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(iconKey)).getDefaultInstance();

        ResourceLocation backgroundArg = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "background"));
        ResourceLocation background = backgroundArg != null ? new ResourceLocation(backgroundArg.getNamespace(), String.format("textures/%s.png", backgroundArg.getPath())) : null;

        int maxLevel = Mth.clamp(GsonHelper.getAsInt(jsonObject, "max_level"), Skill.TRUE_MIN_LEVEL, Skill.TRUE_MAX_LEVEL);

        Translation translation;
        if (jsonObject.has("translation")) {

            JsonObject translationObject = GsonHelper.getAsJsonObject(jsonObject, "translation");
            translation = Translation.fromJsonObject(translationObject);

        } else translation = Translation.empty();

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

        List<ExperienceSource> xpSourceList = new ArrayList<>();
        if (jsonObject.has("xp_sources")) {

            JsonArray xpSourceArray = GsonHelper.getAsJsonArray(jsonObject, "xp_sources");

            for (JsonElement element : xpSourceArray) {

                Optional<ExperienceSource> optional = PoscardsSkills.getXPSourceHandler().create(skillKey, element);
                optional.ifPresent(xpSourceList::add);
            }
        }

        return new Extension(fileKey, skill, icon, background, maxLevel, translation, rewardList, recipeList, requisiteList, xpSourceList);
    }

    public void apply() { if (target != null) target.addExtension(this); }

}
