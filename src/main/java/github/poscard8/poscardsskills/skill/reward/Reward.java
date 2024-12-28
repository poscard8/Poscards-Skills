package github.poscard8.poscardsskills.skill.reward;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.misc.RequisiteHolder;
import github.poscard8.poscardsskills.skill.misc.SkillRequisite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple reward object. Reward types other than items and minecraft xp are currently not supported.
 * <p>{@link #skillKey}: Key of the skill. Rewards are loaded before skills,
 * so we can't pass a skill here.</p>
 * <p>{@link #at}: Levels that this reward can be claimed at.
 * Can be a range of integers or an arbitrary set of integers.</p>
 * See the wiki for the format.
 */
public abstract class Reward implements RequisiteHolder {

    protected final ResourceLocation skillKey;
    protected final Set<Integer> at;

    protected Reward(ResourceLocation skillKey, Set<Integer> at) {

        this.skillKey = skillKey;
        this.at = at;
    }

    public static Reward fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        Set<Integer> at = new HashSet<>();

        if (jsonObject.has("at")) {

            JsonElement jsonElement = jsonObject.get("at");

            if (jsonElement.isJsonPrimitive()) {

                int level = jsonElement.getAsInt();
                if (Skill.isValidLevel(level)) at.add(level);

            } else {

                JsonArray array = GsonHelper.getAsJsonArray(jsonObject, "at");
                for (JsonElement element : array) {

                    int level = element.getAsInt();
                    if (Skill.isValidLevel(level)) at.add(level);
                }
            }

        } else if (jsonObject.has("from") && jsonObject.has("to")) {

            int from = GsonHelper.getAsInt(jsonObject, "from");
            int to = GsonHelper.getAsInt(jsonObject, "to");

            for (int i = from; i <= to; i++) {

                if (Skill.isValidLevel(i)) at.add(i);
            }
        }
        if (at.isEmpty()) return null;

        if (jsonObject.has("item_stack")) {

            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "item_stack"));
            return new ItemReward(skillKey, at, stack);

        } else if (jsonObject.has("xp")) {

            int xp = GsonHelper.getAsInt(jsonObject, "xp");
            return new XpReward(skillKey, at, xp);

        } else return null;
    }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at.stream().sorted().toList().get(0), false); }

    public boolean isAvailableFor(int level) { return at.contains(level); }

}
