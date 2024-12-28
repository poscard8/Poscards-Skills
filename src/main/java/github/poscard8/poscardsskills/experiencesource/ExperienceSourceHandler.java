package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Stream;

/**
 * Loads experience sources.
 * New types need to be registered using {@link #registerType(ResourceLocation typeKey, ExperienceSourceFactory factory)}.
 * <p>See the wiki for JSON documentation of experience sources.
 */
public class ExperienceSourceHandler extends SimpleJsonResourceReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected static final String KEY = "poscardsmods/xp_sources";
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final Map<ResourceLocation, ExperienceSourceFactory<? extends ExperienceSource>> factoryMap = new HashMap<>();
    protected final Set<ExperienceSource> xpSources = new HashSet<>();

    public ExperienceSourceHandler() { super(GSON, KEY); }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller filler) {

        xpSources.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {

            ResourceLocation skillKey = entry.getKey();

            if (!entry.getValue().isJsonObject()) {

                LOGGER.error("Parsing error loading experience sources for {}", skillKey);
                continue;
            }

            JsonObject jsonObject = entry.getValue().getAsJsonObject();

            try {

                JsonArray values = GsonHelper.getAsJsonArray(jsonObject, "values");

                Skill skill = Skill.byKey(skillKey);
                if (skill == null) continue;

                xpSources.removeAll(ExperienceSource.filterBy(skill));

                for (JsonElement element : values) {

                    Optional<ExperienceSource> optional = create(skillKey, element);
                    optional.ifPresent(xpSources::add);
                }

            } catch (IllegalArgumentException | JsonParseException jsonParseException) {

                LOGGER.error("Parsing error loading experience sources for {}", skillKey, jsonParseException);
            }
        }

        LOGGER.info("Loaded {} experience sources", xpSources.size());
    }

    public Optional<ExperienceSource> create(ResourceLocation skillKey, JsonElement element) {

        if (!element.isJsonObject()) return Optional.empty();

        JsonObject object = element.getAsJsonObject();
        ResourceLocation typeKey = ResourceLocation.tryParse(GsonHelper.getAsString(object, "type"));

        if (!factoryMap.containsKey(typeKey)) return Optional.empty();

        try {

            return Optional.of(factoryMap.get(typeKey).create(skillKey, object));

        } catch (Exception exception) { return Optional.empty(); }
    }

    public Set<ExperienceSource> values() { return xpSources; }

    public Stream<ExperienceSource> stream() { return values().stream(); }

    public <E extends ExperienceSource> ExperienceSourceHandler registerType(ResourceLocation typeKey, ExperienceSourceFactory<E> factory) {

        factoryMap.put(typeKey, factory);
        return this;
    }

}
