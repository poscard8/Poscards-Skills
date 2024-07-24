package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface ExperienceSourceFactory<E extends ExperienceSource> {

    E create(ResourceLocation skillKey, JsonObject object);

}
