package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public final class ExperienceSourceData<T> {

    public static Logger LOGGER = LogUtils.getLogger();

    public final DataExperienceSource<T> xpSource;
    public Map<ServerPlayer, T> playerMap;

    final MinecraftServer server;
    final File file;

    ExperienceSourceData(MinecraftServer server, DataExperienceSource<T> xpSource, Map<ServerPlayer, T> playerMap) {

        this.xpSource = xpSource;
        this.playerMap = playerMap;

        this.server = server;
        this.file = getFile(server);

        handleFile();
    }

    public static <I> ExperienceSourceData<I> of(MinecraftServer server, DataExperienceSource<I> dataXPSource) { return new ExperienceSourceData<>(server, dataXPSource, dataXPSource.getPlayerMap()); }

    public static void getOrCreateFile(MinecraftServer server) {

        File directory = server.getWorldPath(PoscardsSkills.DIRECTORY).toFile();
        File file = new File(directory, "experience_source_data.json");

        try { Files.createDirectory(directory.toPath()); } catch (FileAlreadyExistsException ignored) {} catch (IOException e) { throw new RuntimeException(e); }
        try { boolean ignored = file.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }
    }

    /**
     * Gets the existing file. Fired after server load.
     */
    public static File getFile(MinecraftServer server) {

        File directory = server.getWorldPath(PoscardsSkills.DIRECTORY).toFile();
        return new File(directory, "experience_source_data.json");
    }

    /**
     * Gets the file as a {@link JsonObject}.
     */
    public static JsonObject jsonFile(MinecraftServer server) {

        try {

            File file = getFile(server);
            String initial = FileUtils.readFileToString(file);
            String string = initial.isEmpty() ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            return Streams.parse(jsonReader).getAsJsonObject();

        } catch (IOException exception) { throw new RuntimeException(exception); }
    }

    /**
     * Resets the data for the player.
     */
    public static void reset(ServerPlayer player) {

        MinecraftServer server = player.getServer();
        if (server == null) return;

        JsonObject jsonFile = jsonFile(server);
        Set<String> properties = jsonFile.keySet();
        String uuid = player.getStringUUID();

        for (String property : properties) {

            try {

                JsonObject jsonObject = jsonFile.getAsJsonObject(property);
                if (jsonObject.has(uuid)) jsonObject.remove(uuid);

            } catch (Exception ignored) {}
        }

        try {

            File file = getFile(server);
            FileUtils.writeStringToFile(file, jsonFile.toString());

        } catch (IOException exception) { throw new RuntimeException(exception); }
    }

    public void update(ServerPlayer player, T data) {

        playerMap.put(player, data);
        handleFile();
    }

    /**
     * Writes data to file.
     */
    void handleFile() {

        try {
            String initial = FileUtils.readFileToString(file);
            String string = initial.isEmpty() ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            Pair<String, JsonObject> pair = xpSource.serialize();

            JsonObject jsonFile = Streams.parse(jsonReader).getAsJsonObject();

            jsonFile.add(pair.getFirst(), pair.getSecond());
            FileUtils.writeStringToFile(file, jsonFile.toString());

        } catch (IOException exception) { LOGGER.error("Parsing error generating player experience source data."); }
    }

}
