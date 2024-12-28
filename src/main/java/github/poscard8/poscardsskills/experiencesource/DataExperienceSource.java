package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

/**
 * Experience sources that need to store data.
 * <p>Data is stored in {@code "<world save file>/poscardsmods/experience_source_data.json"}.
 */
public interface DataExperienceSource<T> extends ExperienceSource {

    T parseData(ServerPlayer player);

    Pair<String, JsonObject> serialize();

    String serializedName();

    Map<ServerPlayer, T> getPlayerMap();
}
