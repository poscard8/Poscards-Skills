package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public interface DataExperienceSource<T> extends ExperienceSource {

    T parseData(Player player);

    Pair<String, JsonObject> serialize();

    String serializedName();

    Map<Player, T> getPlayerMap();
}
