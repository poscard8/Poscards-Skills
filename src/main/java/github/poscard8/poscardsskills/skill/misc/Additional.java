package github.poscard8.poscardsskills.skill.misc;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

/**
 * Explained in the wiki.
 */
public class Additional implements RequisiteHolder {

    public final int at;
    public final String key;

    private final ResourceLocation skillKey;

    Additional(ResourceLocation skillKey, int at, String key) {

        this.skillKey = skillKey;
        this.at = at;
        this.key = key;
    }

    public static Additional fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        int at = GsonHelper.getAsInt(jsonObject, "at");
        String key = GsonHelper.getAsString(jsonObject, "key");

        return Skill.isValidLevel(at) ? new Additional(skillKey, at, key) : null;
    }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at); }


}
