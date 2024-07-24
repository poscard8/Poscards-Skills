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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.*;
import java.util.function.Predicate;

public class VisitStructureExperienceSource implements DataExperienceSource<List<ChunkPos>> {

    public final Skill skill;
    public final int xp;
    public final Predicate<ServerPlayer> predicate;

    private final String name;
    private final Map<Player, List<ChunkPos>> visitedChunksMap = new HashMap<>();

    VisitStructureExperienceSource(Skill skill, int xp, Predicate<ServerPlayer> predicate, String name) {

        this.skill = skill;
        this.xp = xp;
        this.predicate = predicate;
        this.name = name;
    }

    public static VisitStructureExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String structureArg;
        String name;
        Predicate<ServerPlayer> predicate;

        if (jsonObject.has("tag")) {

            structureArg = GsonHelper.getAsString(jsonObject, "tag");
            name = String.format("vs,t,%s", structureArg);
            if (structureArg.equals("ALL")) return new VisitStructureExperienceSource(skill, xp, alwaysTruePredicate(), name);

            ResourceLocation tagLocation = ResourceLocation.tryParse(structureArg);
            assert tagLocation != null;
            TagKey<Structure> tag = TagKey.create(Registry.STRUCTURE_REGISTRY, tagLocation);

            predicate = predicate(tag);

        } else {

            structureArg = GsonHelper.getAsString(jsonObject, "structure");
            name = String.format("vs,k,%s", structureArg);
            if (structureArg.equals("ALL")) return new VisitStructureExperienceSource(skill, xp, alwaysTruePredicate(), name);

            ResourceLocation structureLoc = ResourceLocation.tryParse(structureArg);

            assert structureLoc != null;
            ResourceKey<Structure> key = ResourceKey.create(Registry.STRUCTURE_REGISTRY, structureLoc);

            predicate = predicate(key);
        }
        return new VisitStructureExperienceSource(skill, xp, predicate, name);
    }

    public static void handlePlayer(ServerPlayer player) {

        try {

            for (VisitStructureExperienceSource xpSource : ExperienceSource.filterBy(VisitStructureExperienceSource.class)) {

                if (xpSource.predicate.test(player) && !xpSource.visit(player) && !player.isSpectator() && !player.isCreative()) {

                    xpSource.applyTo(player);
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    protected static Predicate<ServerPlayer> predicate(ResourceKey<Structure> resourceKey) {

        return serverPlayer -> {

            ServerLevel serverLevel = PSUtils.getCurrentServer().getLevel(serverPlayer.level.dimension());
            if (serverLevel == null) return false;

            StructureManager structureManager = serverLevel.structureManager();
            StructureStart structureStart = structureManager.getStructureWithPieceAt(serverPlayer.getOnPos(), resourceKey);

            return structureStart.isValid();
        };
    }

    protected static Predicate<ServerPlayer> predicate(TagKey<Structure> tag) {

        return serverPlayer -> {

            ServerLevel serverLevel = PSUtils.getCurrentServer().getLevel(serverPlayer.level.dimension());
            if (serverLevel == null) return false;

            StructureManager structureManager = serverLevel.structureManager();
            StructureStart structureStart = structureManager.getStructureWithPieceAt(serverPlayer.getOnPos(), tag);

            return structureStart.isValid();
        };
    }

    protected static Predicate<ServerPlayer> alwaysTruePredicate() {

        return serverPlayer -> {

            ServerLevel serverLevel = PSUtils.getCurrentServer().getLevel(serverPlayer.level.dimension());
            if (serverLevel == null) return false;

            StructureManager structureManager = serverLevel.structureManager();
            return structureManager.hasAnyStructureAt(serverPlayer.getOnPos());
        };
    }

    protected boolean visit(ServerPlayer player) {

        ServerLevel serverLevel = PSUtils.getCurrentServer().getLevel(player.level.dimension());
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

    public List<ChunkPos> getVisitedChunks(Player player) { return visitedChunksMap.getOrDefault(player, parseData(player)); }

    public void addVisitedChunk(Player player, ChunkPos chunkPos) {

        List<ChunkPos> visitedChunks = getVisitedChunks(player);
        visitedChunks.add(chunkPos);
        visitedChunksMap.put(player, visitedChunks);
        ExperienceSourceData.of(player, this).update(player, visitedChunks);
    }

    @Override
    public List<ChunkPos> parseData(Player player) {

        JsonObject jsonFile = ExperienceSourceData.jsonFile(player);
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

        for (Player player : visitedChunksMap.keySet()) {

            JsonArray chunks = new JsonArray();
            for (ChunkPos chunkPos : getVisitedChunks(player)) { chunks.add(String.format("%d,%d", chunkPos.x, chunkPos.z)); }

            playerMap.add(player.getStringUUID(), chunks);
        }
        return Pair.of(serializedName(), playerMap);
    }

    @Override
    public String serializedName() { return name; }

    @Override
    public Map<Player, List<ChunkPos>> getPlayerMap() { return visitedChunksMap; }

}
