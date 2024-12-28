package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.experiencesource.SimpleExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Gives xp when the player kills an entity. See the wiki for the format.
 */
public final class EntityExperienceSource extends SimpleExperienceSource<Entity> {

    EntityExperienceSource(Skill skill, int xp, Predicate<Entity> predicate) { super(skill, xp, predicate); }

    public static EntityExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String entityArg;
        Predicate<Entity> predicate;

        if (jsonObject.has("entity")) {

            entityArg = GsonHelper.getAsString(jsonObject, "entity");
            if (entityArg.equals("ALL")) return new EntityExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ENTITY);

            ResourceLocation entityTypeKey = ResourceLocation.tryParse(entityArg);
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityTypeKey);
            predicate = entity -> entity.getType().equals(entityType);

        } else {

            entityArg = GsonHelper.getAsString(jsonObject, "tag");
            if (entityArg.equals("ALL")) return new EntityExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ENTITY);

            ResourceLocation tagLocation = ResourceLocation.tryParse(entityArg);

            assert tagLocation != null;
            TagKey<EntityType<?>> tag = TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, tagLocation);
            predicate = entity -> entity.getType().is(tag);
        }
        return new EntityExperienceSource(skill, xp, predicate);
    }

    public static void handlePlayer(@Nullable ServerPlayer player, Entity entity) {

        if (player == null) return;
        for (EntityExperienceSource xpSource : ExperienceSource.filterBy(EntityExperienceSource.class)) xpSource.applyIfMeetsConditions(player, entity);
    }

}
