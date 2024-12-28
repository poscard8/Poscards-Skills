package github.poscard8.poscardsskills.extension;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Extension loader class. See the wiki for more info.
 */
public class ExtensionHandler extends SimpleJsonResourceReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected static final String KEY = "poscardsmods/extensions";
    protected static final Logger LOGGER = LogUtils.getLogger();

    public ExtensionHandler() { super(GSON, KEY); }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {

            ResourceLocation fileKey = entry.getKey();

            if (!entry.getValue().isJsonObject()) {

                LOGGER.error("Parsing error loading extension {}", fileKey);
                continue;
            }

            JsonObject jsonObject = entry.getValue().getAsJsonObject();

            try {

                Extension extension = Extension.fromJsonObject(fileKey, jsonObject);
                extension.apply();

            } catch (IllegalArgumentException | JsonParseException jsonParseException) {

                LOGGER.error("Parsing error loading extension {}", fileKey, jsonParseException);
            }
        }

        LOGGER.info("Loaded extensions");
    }

}
