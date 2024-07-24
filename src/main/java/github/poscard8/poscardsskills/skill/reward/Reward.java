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

public abstract class Reward implements RequisiteHolder {

    protected final Set<Integer> at;
    protected final ResourceLocation skillKey;

    protected Reward(ResourceLocation skillKey, Set<Integer> at) {

        this.at = at;
        this.skillKey = skillKey;
    }

    public static Reward fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        Set<Integer> at = new HashSet<>();

        if (jsonObject.has("at")) {

            JsonArray array = GsonHelper.getAsJsonArray(jsonObject, "at");
            for (JsonElement jsonElement : array) {

                if (Skill.isValidLevel(jsonElement.getAsInt())) at.add(jsonElement.getAsInt());
            }

        } else if (jsonObject.has("from") && jsonObject.has("to")) {

            int from = GsonHelper.getAsInt(jsonObject, "from");
            int to = GsonHelper.getAsInt(jsonObject, "to");

            for (int i = from; i <= to; i++) {

                if (Skill.isValidLevel(i)) at.add(i);
            }
        }

        if (at.size() == 0) return null;


        if (jsonObject.has("itemStack")) {

            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "itemStack"));
            return new ItemReward(skillKey, at, stack);

        } else if (jsonObject.has("xp")) {

            int xp = GsonHelper.getAsInt(jsonObject, "xp");
            return new XpReward(skillKey, at, xp);

        } else return null;
    }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at.stream().sorted().toList().get(0)); }

    public boolean isAvailableFor(int level) { return at.contains(level); }

}
