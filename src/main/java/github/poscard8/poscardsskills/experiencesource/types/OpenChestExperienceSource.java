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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class OpenChestExperienceSource extends SimpleExperienceSource<ResourceLocation> {

    private static final Map<Player, ResourceLocation> WAITING_LOOT_TABLE_MAP = new HashMap<>();


    OpenChestExperienceSource(Skill skill, int xp, Predicate<ResourceLocation> predicate) { super(skill, xp, predicate); }

    public static OpenChestExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String arg;

        if (!jsonObject.has("lootTable")) {

            throw new JsonParseException("An open chest experience source entry is a loot table");

        } else {

            arg = GsonHelper.getAsString(jsonObject, "lootTable");
            if (arg.equals("ALL")) return new OpenChestExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_RESOURCE_LOCATION);

            ResourceLocation lootTableKey = ResourceLocation.tryParse(arg);
            Predicate<ResourceLocation> predicate = resourceLocation -> resourceLocation != null && resourceLocation.equals(lootTableKey);
            return new OpenChestExperienceSource(skill, xp, predicate);
        }
    }

    public static void handlePlayer(Player player) {

        if (player == null) return;
        if (getWaitingLootTable(player) == null) return;

        for (OpenChestExperienceSource xpSource : ExperienceSource.filterBy(OpenChestExperienceSource.class)) {

            if (xpSource.meetsConditions(player, getWaitingLootTable(player))) {

                xpSource.applyTo(player);
                removeWaitingLootTable(player);
            }
        }
    }

    @Nullable
    public static ResourceLocation getWaitingLootTable(Player player) { return WAITING_LOOT_TABLE_MAP.getOrDefault(player, null); }

    public static void setWaitingLootTable(Player player, ResourceLocation lootTable) { WAITING_LOOT_TABLE_MAP.put(player, lootTable); }

    public static void removeWaitingLootTable(Player player) { WAITING_LOOT_TABLE_MAP.remove(player); }


}
