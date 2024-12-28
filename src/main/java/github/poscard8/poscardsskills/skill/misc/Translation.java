package github.poscard8.poscardsskills.skill.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Enables adding custom names to skills without changing language files.
 * See the wiki for the format.
 */
public class Translation {

    protected final Map<String, TranslationInstance> languageMap;
    public final boolean overwrite;

    public Translation(Map<String, TranslationInstance> languageMap, boolean overwrite) {

        this.languageMap = languageMap;
        this.overwrite = overwrite;
    }

    public static Translation empty() { return new Translation(new HashMap<>(), false); }

    public static Translation fromJsonObject(JsonObject jsonObject) {

        Map<String, TranslationInstance> languageMap = new HashMap<>();
        boolean overwrite = jsonObject.has("overwrite") && GsonHelper.getAsBoolean(jsonObject, "overwrite");

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

            String string = entry.getKey();

            if (!string.equals("overwrite") && entry.getValue().isJsonObject()) {

                TranslationInstance instance = TranslationInstance.fromJsonObject(entry.getValue().getAsJsonObject());
                languageMap.put(string, instance);
            }
        }

        return new Translation(languageMap, overwrite);
    }

    public void add(Translation other) {

        if (other.overwrite) {

            for (String string : other.languageMap.keySet()) languageMap.put(string, other.languageMap.get(string));

        } else {

            for (String string : other.languageMap.keySet()) languageMap.putIfAbsent(string, other.languageMap.get(string));
        }
    }

    /**
     * Gets skill name.
     */
    @Nullable
    public String getName() {

        try {

            String languageCode = Minecraft.getInstance().options.languageCode;
            return languageMap.containsKey(languageCode) ? languageMap.get(languageCode).name() : null;

        } catch (Exception exception) { return null; }
    }

    /**
     * Gets skill description.
     */
    @Nullable
    public String getDescription() {

        try {

            String languageCode = Minecraft.getInstance().options.languageCode;
            return languageMap.containsKey(languageCode) ? languageMap.get(languageCode).description() : null;

        } catch (Exception exception) { return null; }
    }

    /**
     * Translation for a single language.
     */
    public record TranslationInstance(@Nullable String name, @Nullable String description) {

        public static TranslationInstance fromJsonObject(JsonObject jsonObject) {

            @Nullable String name = jsonObject.has("name") ? GsonHelper.getAsString(jsonObject, "name") : null;
            @Nullable String description = jsonObject.has("description") ? GsonHelper.getAsString(jsonObject, "description") : null;
            return new TranslationInstance(name, description);
        }

    }

}
