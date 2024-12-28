package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.DataExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceData;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/**
 * Gives xp when the player visits structures.
 * The xp source stores visited chunk positions so the
 * player cannot gain infinite xp by visiting the same
 * structure over and over again.
 */
public final class StructureExperienceSource implements DataExperienceSource<List<ChunkPos>> {

    public final Skill skill;
    public final int xp;
    public final Predicate<ServerPlayer> predicate;

    final String name;
    final Map<ServerPlayer, List<ChunkPos>> visitedChunksMap = new HashMap<>();

    StructureExperienceSource(Skill skill, int xp, Predicate<ServerPlayer> predicate, String name) {

        this.skill = skill;
        this.xp = xp;
        this.predicate = predicate;
        this.name = name;
    }

    public static StructureExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String structureArg;
        String name;
        Predicate<ServerPlayer> predicate;

        if (jsonObject.has("structure")) {

            structureArg = GsonHelper.getAsString(jsonObject, "structure");
            name = String.format("structure,single,%s", structureArg);
            if (structureArg.equals("ALL")) return new StructureExperienceSource(skill, xp, alwaysTruePredicate(), name);

            ResourceLocation structureLoc = ResourceLocation.tryParse(structureArg);

            assert structureLoc != null;
            ResourceKey<Structure> key = ResourceKey.create(Registry.STRUCTURE_REGISTRY, structureLoc);

            predicate = predicate(key);

        } else {

            structureArg = GsonHelper.getAsString(jsonObject, "tag");
            name = String.format("structure,tag,%s", structureArg);
            if (structureArg.equals("ALL")) return new StructureExperienceSource(skill, xp, alwaysTruePredicate(), name);

            ResourceLocation tagLocation = ResourceLocation.tryParse(structureArg);
            assert tagLocation != null;
            TagKey<Structure> tag = TagKey.create(Registry.STRUCTURE_REGISTRY, tagLocation);

            predicate = predicate(tag);
        }
        return new StructureExperienceSource(skill, xp, predicate, name);
    }

    /**
     * Handling method.
     * Since there is almost never more than one structure per chunk,
     * the process ends when it detects an unvisited structure.
     */
    public static void handlePlayer(@Nullable ServerPlayer player) {

        if (player == null) return;

        try {

            for (StructureExperienceSource xpSource : ExperienceSource.filterBy(StructureExperienceSource.class)) {

                if (xpSource.predicate.test(player) && !xpSource.visit(player) && !player.isSpectator() && !player.isCreative()) {

                    xpSource.applyTo(player);
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Generates a player predicate for a single structure.
     */
    public static Predicate<ServerPlayer> predicate(ResourceKey<Structure> resourceKey) {

        return serverPlayer -> {

            ServerLevel serverLevel = PSUtils.getServer().getLevel(serverPlayer.getLevel().dimension());
            if (serverLevel == null) return false;

            StructureManager structureManager = serverLevel.structureManager();
            StructureStart structureStart = structureManager.getStructureWithPieceAt(serverPlayer.getOnPos(), resourceKey);

            return structureStart.isValid();
        };
    }

    /**
     * Generates a player predicate for a structure tag.
     */
    public static Predicate<ServerPlayer> predicate(TagKey<Structure> tag) {

        return serverPlayer -> {

            ServerLevel serverLevel = PSUtils.getServer().getLevel(serverPlayer.getLevel().dimension());
            if (serverLevel == null) return false;

            StructureManager structureManager = serverLevel.structureManager();
            StructureStart structureStart = structureManager.getStructureWithPieceAt(serverPlayer.getOnPos(), tag);

            return structureStart.isValid();
        };
    }

    public static Predicate<ServerPlayer> alwaysTruePredicate() {

        return serverPlayer -> {

            ServerLevel serverLevel = PSUtils.getServer().getLevel(serverPlayer.getLevel().dimension());
            if (serverLevel == null) return false;

            StructureManager structureManager = serverLevel.structureManager();
            return structureManager.hasAnyStructureAt(serverPlayer.getOnPos());
        };
    }

    /**
     * Method to check if the player visited the structures or not.
     */
    boolean visit(ServerPlayer player) {

        ServerLevel serverLevel = PSUtils.getServer().getLevel(player.getLevel().dimension());
        if (serverLevel == null) return false;

        StructureManager structureManager = serverLevel.structureManager();
        Set<Structure> structures = structureManager.getAllStructuresAt(player.getOnPos()).keySet();
        ChunkPos origin = null;

        for (Structure structure : structures) {

            StructureStart structureStart = structureManager.getStructureWithPieceAt(player.getOnPos(), structure);
            if (structureStart.isValid()) {

                origin = structureStart.getChunkPos();
                if (getVisitedChunks(player).contains(origin)) return true;
            }
        }
        if (origin != null) addVisitedChunk(player, origin);
        return false;
    }

    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }

    public List<ChunkPos> getVisitedChunks(ServerPlayer player) { return visitedChunksMap.getOrDefault(player, parseData(player)); }

    /**
     * Adds the chunk position to the JSON file.
     */
    public void addVisitedChunk(ServerPlayer player, ChunkPos chunkPos) {

        List<ChunkPos> visitedChunks = getVisitedChunks(player);
        visitedChunks.add(chunkPos);
        visitedChunksMap.put(player, visitedChunks);
        ExperienceSourceData.of(player.getServer(), this).update(player, visitedChunks);
    }

    /**
     * Deserializes chunk positions from the JSON file.
     */
    @Override
    public List<ChunkPos> parseData(ServerPlayer player) {

        JsonObject jsonFile = ExperienceSourceData.jsonFile(player.getServer());
        List<ChunkPos> visitedChunks = new ArrayList<>();
        String serialized = this.serializedName();
        String uuid = player.getStringUUID();

        if (!jsonFile.has(serialized)) { return new ArrayList<>(); }

        JsonObject jsonObject = GsonHelper.getAsJsonObject(jsonFile, serialized);

        if (!jsonObject.has(uuid)) { return new ArrayList<>(); }

        JsonArray chunks = GsonHelper.getAsJsonArray(jsonObject, uuid);
        for (JsonElement jsonElement : chunks) {

            String[] stringArray = jsonElement.getAsString().split(",");
            visitedChunks.add(new ChunkPos(Integer.parseInt(stringArray[0]), Integer.parseInt(stringArray[1])));
        }
        return visitedChunks;
    }

    @Override
    public Pair<String, JsonObject> serialize() {

        JsonObject playerMap = new JsonObject();

        for (ServerPlayer player : visitedChunksMap.keySet()) {

            JsonArray chunks = new JsonArray();
            for (ChunkPos chunkPos : getVisitedChunks(player)) { chunks.add(String.format("%d,%d", chunkPos.x, chunkPos.z)); }

            playerMap.add(player.getStringUUID(), chunks);
        }
        return Pair.of(serializedName(), playerMap);
    }

    @Override
    public String serializedName() { return name; }

    @Override
    public Map<ServerPlayer, List<ChunkPos>> getPlayerMap() { return visitedChunksMap; }

}
