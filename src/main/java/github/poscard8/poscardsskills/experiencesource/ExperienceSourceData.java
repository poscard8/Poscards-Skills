package github.poscard8.poscardsskills.experiencesource;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
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
public class ExperienceSourceData<T> {

    public static Logger LOGGER = LogUtils.getLogger();

    public final File file;
    public final DataExperienceSource<T> xpSource;
    public Map<Player, T> playerMap;

    private ExperienceSourceData(Player player, DataExperienceSource<T> xpSource, Map<Player, T> playerMap) {

        this.xpSource = xpSource;
        this.playerMap = playerMap;
        this.file = getOrCreateFile();

        handleFile();
    }

    public static <I> ExperienceSourceData<I> of(Player player, DataExperienceSource<I> dataXPSource) {

        return new ExperienceSourceData<>(player, dataXPSource, dataXPSource.getPlayerMap());
    }

    public static JsonObject jsonFile(Player player) {

        try {

            assert player.getServer() != null;
            File directory = player.getServer().getWorldPath(PoscardsSkills.DIRECTORY).toFile();
            File file = new File(directory, "experience_source_data.json");

            try { Files.createDirectory(directory.toPath()); } catch (FileAlreadyExistsException ignored) {} catch (IOException e) { throw new RuntimeException(e); }
            try { boolean ignored = file.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

            String initial = FileUtils.readFileToString(file);
            String string = initial.equals("") ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            return Streams.parse(jsonReader).getAsJsonObject();

        } catch (IOException exception) { throw new RuntimeException(exception); }
    }

    public static void reset(Player player) {

        JsonObject jsonFile = jsonFile(player);
        Set<String> properties = jsonFile.keySet();
        String uuid = player.getStringUUID();

        for (String property : properties) {

            try {

                JsonObject jsonObject = jsonFile.getAsJsonObject(property);
                if (jsonObject.has(uuid)) jsonObject.remove(uuid);

            } catch (Exception ignored) {}
        }

        try {

            File file = getOrCreateFile();
            FileUtils.writeStringToFile(file, jsonFile.toString());

        } catch (IOException exception) { throw new RuntimeException(exception); }
    }

    private static File getOrCreateFile() {

        MinecraftServer server = PSUtils.getCurrentServer();
        File directory = server.getWorldPath(PoscardsSkills.DIRECTORY).toFile();
        File file = new File(directory, "experience_source_data.json");

        try { Files.createDirectory(directory.toPath()); } catch (FileAlreadyExistsException ignored) {} catch (IOException e) { throw new RuntimeException(e); }
        try { boolean ignored = file.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

        return file;
    }

    private void handleFile() {

        try {
            String initial = FileUtils.readFileToString(file);
            String string = initial.equals("") ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            Pair<String, JsonObject> pair = xpSource.serialize();

            JsonObject jsonFile = Streams.parse(jsonReader).getAsJsonObject();

            jsonFile.add(pair.getFirst(), pair.getSecond());
            FileUtils.writeStringToFile(file, jsonFile.toString());

        } catch (IOException exception) { LOGGER.error("Parsing error generating player experience source data."); }
    }

    public void update(Player player, T data) {

        playerMap.put(player, data);
        handleFile();
    }

}
