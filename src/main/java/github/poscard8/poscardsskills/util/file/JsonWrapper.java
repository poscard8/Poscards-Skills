package github.poscard8.poscardsskills.util.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;

public class JsonWrapper {

    public final JsonObject object;
    public final boolean doesReplace;

    public JsonWrapper(JsonObject object) {

        this.object = object;
        this.doesReplace = object.has("replace") && object.get("replace").getAsBoolean();
    }

    @Nullable
    public static JsonWrapper byResource(Gson gson, Resource resource) {

        try {

            BufferedReader reader = resource.openAsReader();

            JsonObject object = GsonHelper.fromJson(gson, reader, JsonObject.class);
            reader.close();

            return object != null ? new JsonWrapper(object) : null;

        } catch (Exception exception) { return null; }
    }

}
