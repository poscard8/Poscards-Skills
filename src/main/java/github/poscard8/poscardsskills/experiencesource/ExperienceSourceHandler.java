package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.*;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.util.file.JsonWrapper;
import github.poscard8.poscardsskills.util.file.MultiJsonResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class ExperienceSourceHandler extends MultiJsonResourceReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected static final String KEY = "poscardsmods/xp_sources";

    protected final Map<ResourceLocation, ExperienceSourceFactory<? extends ExperienceSource>> factoryMap = new HashMap<>();
    protected final Map<Class<? extends ExperienceSource>, ResourceLocation> typeMap = new HashMap<>();
    protected final Collection<ExperienceSource> xpSources = new HashSet<>();

    public ExperienceSourceHandler() { super(GSON, KEY); }

    @Override
    protected void apply(Map<ResourceLocation, List<JsonWrapper>> wrapperMap, ResourceManager resourceManager, ProfilerFiller filler) {

        xpSources.clear();

        for (Map.Entry<ResourceLocation, List<JsonWrapper>> entry : wrapperMap.entrySet()) {

            ResourceLocation skillKey = entry.getKey();

            for (JsonWrapper wrapper : entry.getValue()) {

                try {

                    JsonObject jsonFile = wrapper.object;
                    JsonArray values = GsonHelper.getAsJsonArray(jsonFile, "values");

                    if (wrapper.doesReplace) {

                        Skill skill = Skill.byKey(skillKey);
                        if (skill != null) xpSources.removeAll(ExperienceSource.filterBy(skill));
                    }

                    for (JsonElement element : values) {

                        JsonObject object = element.getAsJsonObject();
                        ResourceLocation typeKey = ResourceLocation.tryParse(GsonHelper.getAsString(object, "type"));

                        if (!factoryMap.containsKey(typeKey)) continue;

                        try {

                            ExperienceSource source = factoryMap.get(typeKey).create(skillKey, object);
                            xpSources.add(source);

                        } catch (Exception ignored) {}
                    }

                } catch (IllegalArgumentException | JsonParseException jsonParseException) {

                    LOGGER.error("Parsing error loading experience sources.", jsonParseException);
                }
            }
        }

        LOGGER.info("Loaded {} experience sources", xpSources.size());
    }

    public Stream<ExperienceSource> values() { return xpSources.stream(); }

    public <E extends ExperienceSource> ExperienceSourceHandler registerType(ResourceLocation typeKey, ExperienceSourceFactory<E> factory, Class<E> clazz) {

        factoryMap.put(typeKey, factory);
        typeMap.put(clazz, typeKey);
        return this;
    }

    @Nullable
    public ResourceLocation typeKeyOf(ExperienceSource xpSource) { return typeMap.getOrDefault(xpSource.getClass(), null); }

}
