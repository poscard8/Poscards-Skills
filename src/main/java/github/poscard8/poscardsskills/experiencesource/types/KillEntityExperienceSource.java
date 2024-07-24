package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.experiencesource.SimpleExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Predicate;

public class KillEntityExperienceSource extends SimpleExperienceSource<Entity> {

    KillEntityExperienceSource(Skill skill, int xp, Predicate<Entity> predicate) { super(skill, xp, predicate); }

    public static KillEntityExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String entityArg;
        Predicate<Entity> predicate;

        if (jsonObject.has("tag")) {

            entityArg = GsonHelper.getAsString(jsonObject, "tag");
            if (entityArg.equals("ALL")) return new KillEntityExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ENTITY);

            ResourceLocation tagLocation = ResourceLocation.tryParse(entityArg);

            assert tagLocation != null;
            TagKey<EntityType<?>> tag = TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, tagLocation);
            predicate = entity -> entity.getType().is(tag);

        } else {

            entityArg = GsonHelper.getAsString(jsonObject, "entity");
            if (entityArg.equals("ALL")) return new KillEntityExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ENTITY);

            ResourceLocation entityTypeKey = ResourceLocation.tryParse(entityArg);
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityTypeKey);
            predicate = entity -> entity.getType().equals(entityType);
        }
        return new KillEntityExperienceSource(skill, xp, predicate);
    }

    public static void handlePlayer(Player player, Entity entity) {

        for (KillEntityExperienceSource xpSource : ExperienceSource.filterBy(KillEntityExperienceSource.class)) xpSource.applyIfMeetsConditions(player, entity);
    }

}
