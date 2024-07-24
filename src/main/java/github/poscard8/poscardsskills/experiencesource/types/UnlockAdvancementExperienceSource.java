package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.experiencesource.SimpleExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.Predicate;

public class UnlockAdvancementExperienceSource extends SimpleExperienceSource<ResourceLocation> {

    UnlockAdvancementExperienceSource(Skill skill, int xp, Predicate<ResourceLocation> predicate) { super(skill, xp, predicate); }

    public static UnlockAdvancementExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String arg;

        if (!jsonObject.has("advancement")) {

            throw new JsonParseException("An unlock advancement experience source entry has to have an advancement");

        } else {

            arg = GsonHelper.getAsString(jsonObject, "advancement");
            if (arg.equals("ALL")) return new UnlockAdvancementExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_RESOURCE_LOCATION);

            ResourceLocation advancementKey = ResourceLocation.tryParse(arg);
            Predicate<ResourceLocation> predicate = resourceLocation -> resourceLocation.equals(advancementKey);

            return new UnlockAdvancementExperienceSource(skill, xp, predicate);
        }
    }

    public static void handlePlayer(Player player, ResourceLocation advancement) {

        for (UnlockAdvancementExperienceSource xpSource : ExperienceSource.filterBy(UnlockAdvancementExperienceSource.class)) xpSource.applyIfMeetsConditions(player, advancement);
    }

}
