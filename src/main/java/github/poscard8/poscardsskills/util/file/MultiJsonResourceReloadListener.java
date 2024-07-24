package github.poscard8.poscardsskills.util.file;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MultiJsonResourceReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, List<JsonWrapper>>> {

    protected static final Logger LOGGER = LogUtils.getLogger();
    private static final String PATH_SUFFIX = ".json";
    private static final int PATH_SUFFIX_LENGTH = PATH_SUFFIX.length();

    protected final Gson gson;
    protected final String directory;

    public MultiJsonResourceReloadListener(Gson gson, String directory) {

        this.gson = gson;
        this.directory = directory;
    }

    @Override
    protected Map<ResourceLocation, List<JsonWrapper>> prepare(ResourceManager resourceManager, ProfilerFiller filler) {

        Map<ResourceLocation, List<JsonWrapper>> map = new HashMap<>();
        int p = directory.length() + 1;

        for (Map.Entry<ResourceLocation, List<Resource>> entry : resourceManager.listResourceStacks(directory, location -> location.getPath().endsWith(PATH_SUFFIX)).entrySet()) {

            ResourceLocation oldLocation = entry.getKey();
            ResourceLocation newLocation = new ResourceLocation(oldLocation.getNamespace(), oldLocation.getPath().substring(p, oldLocation.getPath().length() - PATH_SUFFIX_LENGTH));

            List<JsonWrapper> list = new ArrayList<>();

            for (Resource resource : entry.getValue()) {

                @Nullable JsonWrapper wrapper = JsonWrapper.byResource(gson, resource);

                if (wrapper != null) { list.add(wrapper); } else LOGGER.error("Error loading resource: {}", newLocation);
            }

            if (!list.isEmpty()) map.put(newLocation, list);
        }

        return map;
    }
}
