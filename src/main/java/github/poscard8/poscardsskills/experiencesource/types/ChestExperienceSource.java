package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.experiencesource.SimpleExperienceSource;
import github.poscard8.poscardsskills.mixin.RandomizableContainerBlockEntityMixin;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Gives xp when the player opens a loot chest.
 * XP is given after the player closes the chest.
 * If the loot table is already generated, no xp will be given.
 * <p>See the wiki and {@link RandomizableContainerBlockEntityMixin} for more info.</p>
 */
public final class ChestExperienceSource extends SimpleExperienceSource<ResourceLocation> {

    static final Map<ServerPlayer, ResourceLocation> WAITING_LOOT_TABLE_MAP = new HashMap<>();

    ChestExperienceSource(Skill skill, int xp, Predicate<ResourceLocation> predicate) { super(skill, xp, predicate); }

    public static ChestExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String arg;

        if (!jsonObject.has("lootTable")) {

            throw new JsonParseException("An open chest experience source entry is a loot table");

        } else {

            arg = GsonHelper.getAsString(jsonObject, "lootTable");
            if (arg.equals("ALL")) return new ChestExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_RESOURCE_LOCATION);

            ResourceLocation lootTableKey = ResourceLocation.tryParse(arg);
            Predicate<ResourceLocation> predicate = resourceLocation -> resourceLocation != null && resourceLocation.equals(lootTableKey);
            return new ChestExperienceSource(skill, xp, predicate);
        }
    }

    public static void handlePlayer(@Nullable ServerPlayer player) {

        if (player == null) return;
        if (getWaitingLootTable(player) == null) return;

        for (ChestExperienceSource xpSource : ExperienceSource.filterBy(ChestExperienceSource.class)) {

            if (xpSource.meetsConditions(player, getWaitingLootTable(player))) {

                xpSource.applyTo(player);
                removeWaitingLootTable(player);
            }
        }
    }

    /**
     * These methods store the loot table ID. This enables the player to gain the xp after they close the chest.
     */
    @Nullable
    public static ResourceLocation getWaitingLootTable(ServerPlayer player) { return WAITING_LOOT_TABLE_MAP.getOrDefault(player, null); }

    public static void setWaitingLootTable(ServerPlayer player, ResourceLocation lootTable) { WAITING_LOOT_TABLE_MAP.put(player, lootTable); }

    public static void removeWaitingLootTable(ServerPlayer player) { WAITING_LOOT_TABLE_MAP.remove(player); }


}
