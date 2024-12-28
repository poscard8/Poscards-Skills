package github.poscard8.poscardsskills.skill;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Skill loader. {@link #skills} is a skill array with a size of 27.
 * <p>Skills are registered based on position (row and column). If multiple skills
 * are registered on the same position, one of them will override the other.</p>
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class SkillHandler extends SimpleJsonResourceReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected static final String KEY = "poscardsmods/skills";
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected Skill[] skills;

    public SkillHandler() { super(GSON, KEY); }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller filler) {

        skills = new Skill[Skill.MAX_SKILL_COUNT];

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {

            ResourceLocation skillKey = entry.getKey();

            if (!entry.getValue().isJsonObject()) {

                LOGGER.error("Parsing error loading skill {}", skillKey);
                continue;
            }

            JsonObject jsonObject = entry.getValue().getAsJsonObject();

            try {

                Skill skill = Skill.fromJsonObject(skillKey, jsonObject);
                if (skill != null && shouldLoad(skillKey)) skills[skill.getPositionIndex()] = skill;

            } catch (IllegalArgumentException | JsonParseException jsonParseException) {

                LOGGER.error("Parsing error loading skill {}", skillKey, jsonParseException);
            }
        }

        LOGGER.info("Loaded {} skills", getValues().size());
    }

    /**
     * Enables mod skills to load based on configs.
     */
    boolean shouldLoad(ResourceLocation skillKey) {

        boolean shouldLoad;

        switch (skillKey.toString()) {

            case Skill.WOODCUTTING_KEY_STRING -> shouldLoad = PoscardsSkillsCommonConfig.WOODCUTTING_SKILL.get();
            case Skill.MINING_KEY_STRING -> shouldLoad = PoscardsSkillsCommonConfig.MINING_SKILL.get();
            case Skill.FARMING_KEY_STRING -> shouldLoad = PoscardsSkillsCommonConfig.FARMING_SKILL.get();
            case Skill.COMBAT_KEY_STRING -> shouldLoad = PoscardsSkillsCommonConfig.COMBAT_SKILL.get();
            case Skill.EXPLORING_KEY_STRING -> shouldLoad = PoscardsSkillsCommonConfig.EXPLORING_SKILL.get();
            case Skill.ENCHANTING_KEY_STRING -> shouldLoad = PoscardsSkillsCommonConfig.ENCHANTING_SKILL.get();
            default -> shouldLoad = true;
        }

        return shouldLoad;
    }

    public List<ResourceLocation> getKeys() {

        List<ResourceLocation> keys = new ArrayList<>();

        for (Skill skill : skills) {

            if (skill != null) keys.add(skill.key);
        }
        return keys;
    }

    public List<Skill> getValues() {

        List<Skill> keys = new ArrayList<>();

        for (Skill skill : skills) {

            if (skill != null) keys.add(skill);
        }
        return keys;
    }

    public Optional<Skill> byPositionIndex(int index) {

        try {

            Skill skill = skills[index];
            return Optional.ofNullable(skill);

        } catch (Exception exception) { return Optional.empty(); }
    }

    public Optional<Skill> byKey(ResourceLocation key) {

        for (Skill skill : skills) {

            if (skill != null && skill.key.equals(key)) return Optional.of(skill);
        }
        return Optional.empty();
    }

    public int getMaxSkillLevel() { return getValues().stream().map(skill -> skill.maxLevel).max(Integer::compareTo).orElse(Skill.TRUE_MAX_LEVEL); }

}
