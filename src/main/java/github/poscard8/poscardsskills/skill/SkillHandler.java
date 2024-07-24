package github.poscard8.poscardsskills.skill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.util.file.JsonWrapper;
import github.poscard8.poscardsskills.util.file.MultiJsonResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unused")
public class SkillHandler extends MultiJsonResourceReloadListener {

    protected static final int MAX_SKILL_COUNT = 15;

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected static final String KEY = "poscardsmods/skills";

    protected final Map<ResourceLocation, Skill> byLocation = new HashMap<>();
    protected final Map<Skill, SkillPosition> positions = new HashMap<>();
    protected final Map<Integer, ResourceLocation> defaultPositions = new HashMap<>();

    public SkillHandler() { super(GSON, KEY); }

    @SuppressWarnings("ALL")
    @Override
    protected void apply(Map<ResourceLocation, List<JsonWrapper>> wrapperMap, ResourceManager resourceManager, ProfilerFiller filler) {

        byLocation.clear();
        positions.clear();

        for (Map.Entry<ResourceLocation, List<JsonWrapper>> entry : wrapperMap.entrySet()) {

            ResourceLocation skillKey = entry.getKey();
            int index;

            for (JsonWrapper wrapper : entry.getValue()) {

                try {

                    JsonObject jsonFile = wrapper.object;

                    if (wrapper.doesReplace) {

                        if (byLocation.containsKey(skillKey)) {

                            positions.remove(skillKey);
                            byLocation.remove(skillKey);
                        }

                        index = byLocation.size();

                        Skill skill = Skill.fromJsonObject(index, skillKey, jsonFile);
                        boolean skillCapReached = byLocation.values().size() >= MAX_SKILL_COUNT;

                        if (skillCapReached) LOGGER.error("Could not load skill {} as the mod {} only allows 15 skills", skillKey, PoscardsSkills.NAME);

                        if (skill != null && !skillCapReached && isPresent(skillKey)) {

                            byLocation.put(skillKey, skill);
                            getOrCreatePosition(skill);
                        }

                    } else {

                        if (byLocation.containsKey(skillKey)) {

                            Skill skill = byLocation.get(skillKey);
                            skill.addContents(jsonFile);

                        } else {

                            index = byLocation.size();

                            Skill skill = Skill.fromJsonObject(index, skillKey, jsonFile);
                            boolean skillCapReached = byLocation.values().size() >= MAX_SKILL_COUNT;

                            if (skillCapReached) LOGGER.error("Could not load skill {} as the mod {} only allows 15 skills", skillKey, PoscardsSkills.NAME);

                            if (skill != null && !skillCapReached && isPresent(skillKey)) {

                                byLocation.put(skillKey, skill);
                                getOrCreatePosition(skill);
                            }
                        }
                    }

                } catch (IllegalArgumentException | JsonParseException jsonParseException) {

                    LOGGER.error("Parsing error loading skill {}", skillKey, jsonParseException);
                }
            }
        }

        LOGGER.info("Loaded {} skills", byLocation.size());
    }

    private boolean isPresent(ResourceLocation skillKey) {

        Map<ResourceLocation, ForgeConfigSpec.BooleanValue> map = Map.of(

                PoscardsSkills.asResource("mining"), PoscardsSkillsCommonConfig.MINING_SKILL,
                PoscardsSkills.asResource("farming"), PoscardsSkillsCommonConfig.FARMING_SKILL,
                PoscardsSkills.asResource("combat"), PoscardsSkillsCommonConfig.COMBAT_SKILL,
                PoscardsSkills.asResource("magic"), PoscardsSkillsCommonConfig.MAGIC_SKILL,
                PoscardsSkills.asResource("exploring"), PoscardsSkillsCommonConfig.EXPLORING_SKILL
        );

        return map.containsKey(skillKey) ? map.get(skillKey).get() : true;
    }

    public Collection<ResourceLocation> getKeys() { return byLocation.keySet(); }

    public Collection<Skill> getValues() { return byLocation.values(); }

    public List<Skill> getSortedValues() { return getValues().stream().sorted(Comparator.comparingInt(skill -> skill.position().value)).toList(); }

    public Optional<Skill> byLocation(ResourceLocation location) { return Optional.ofNullable(byLocation.get(location)); }

    public SkillPosition getOrCreatePosition(Skill skill) {

        if (positions.containsKey(skill)) return positions.get(skill);

        int value = getOrCreatePositionValue(skill);
        if (value == -1) return null;

        SkillPosition position = new SkillPosition(value);

        positions.put(skill, position);
        return position;
    }

    /**
     * Methods to set positions of skills in UI.
     * If no position is set for a skill, the skill will be placed randomly in the UI.
     * The row value should be between 0 and 2, the column value should be between 0 and 4.
     * If the skillKey argument is null, the default skill for the position is removed.
    */
    public SkillHandler setDefaultPosition(int row, int column, @Nullable ResourceLocation skillKey) {

        return setDefaultPosition(5 * row + column, skillKey);
    }

    public SkillHandler setDefaultPosition(int value, @Nullable ResourceLocation skillKey) {

        if (skillKey != null) { defaultPositions.put(value, skillKey); } else defaultPositions.remove(value);
        return this;
    }

    private int getOrCreatePositionValue(Skill skill) {

        if (skill == null) return -1;

        for (int i = 0; i < MAX_SKILL_COUNT; i++) {

            if (skill.key.equals(defaultPositions.getOrDefault(i, null))) return i;
        }

        if (positions.containsKey(skill)) return positions.get(skill).value;

        List<Integer> fullSpots = new ArrayList<>(positions.values().stream().map(skillPosition -> skillPosition.value).toList());
        fullSpots.addAll(defaultPositions.keySet());

        for (int i = 0; i < MAX_SKILL_COUNT; i++) { if (!fullSpots.contains(i)) return i; }
        return -1;
    }


    public static class SkillPosition {

        public final int row;
        public final int column;
        public final int value;

        public SkillPosition(int value) {

            this.row = value / 5;
            this.column = value % 5;
            this.value = value;
        }
    }

}
