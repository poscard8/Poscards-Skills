package github.poscard8.poscardsskills.skill;

import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.secret.SecretData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static github.poscard8.poscardsskills.PoscardsSkills.getSkillHandler;

/**
 * Skill, secret, and ascension data of the player.
 * Only updated on change or when the player enters the world.
 *
 * <p>Note: {@link #ascend()} only resets the skills while
 * {@link #reset()} resets everything.</p>
 */
@SuppressWarnings("deprecation")
public final class SkillData {

    public static final Logger LOGGER = LogUtils.getLogger();

    public final ServerPlayer player;
    public Map<Skill, SkillInstance> skillMap;
    public Map<Skill, Integer> highScores;
    public SecretData secretData;
    public int ascensions;

    final MinecraftServer server;
    final File file;
    final String uuid;

    boolean updating;

    SkillData(@NotNull ServerPlayer player, Map<Skill, SkillInstance> skillMap, Map<Skill, Integer> highScores, SecretData secretData, int ascensions) { this(player, skillMap, highScores, secretData, ascensions, true); }

    SkillData(@NotNull ServerPlayer player, Map<Skill, SkillInstance> skillMap, Map<Skill, Integer> highScores, SecretData secretData, int ascensions, boolean update) {

        this.player = player;
        this.skillMap = skillMap;
        this.highScores = highScores;
        this.secretData = secretData;
        this.ascensions = ascensions;

        MinecraftServer server = player.getServer();
        if (server == null) throw new RuntimeException("Tried to generate skill data without a server.");

        this.server = server;
        this.file = getFile(server);
        this.uuid = player.getStringUUID();
        this.updating = false;

        if (update) update();
    }

    /**
     * Loads without updating to save performance. However, if the player does not have data saved,
     * the method still updates the skill data.
     */
    @NotNull
    public static SkillData of(ServerPlayer player) {

        try {

            MinecraftServer server = player.getServer();
            assert server != null;
            File file = getFile(server);

            String initial = FileUtils.readFileToString(file);
            String string = initial.isEmpty() ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            JsonObject jsonObject = Streams.parse(jsonReader).getAsJsonObject();
            String uuid = player.getStringUUID();

            return jsonObject.has(uuid) ? deserialize(player, GsonHelper.getAsJsonObject(jsonObject, uuid)) : new SkillData(player, emptySkillMap(), emptyHighScoreMap(), new SecretData(), 0);

        } catch (IOException exception) { throw new RuntimeException("Parsing error loading player skill data."); }
    }

    public static void getOrCreateFile(MinecraftServer server) {

        File directory = server.getWorldPath(PoscardsSkills.DIRECTORY).toFile();
        File file = new File(directory, "skill_data.json");

        try { Files.createDirectory(directory.toPath()); } catch (FileAlreadyExistsException ignored) {} catch (IOException e) { throw new RuntimeException(e); }
        try { boolean ignored = file.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

    }

    /**
     * Gets the existing file. Fired after server load.
     */
    public static File getFile(MinecraftServer server) {

        File directory = server.getWorldPath(PoscardsSkills.DIRECTORY).toFile();
        return new File(directory, "skill_data.json");
    }

    public static Map<Skill, SkillInstance> emptySkillMap() {

        Map<Skill, SkillInstance> map = new HashMap<>();

        for (Skill skill : getSkillHandler().getValues()) map.put(skill, new SkillInstance(skill));
        return map;
    }

    public static Map<Skill, SkillInstance> maxSkillMap() {

        Map<Skill, SkillInstance> map = new HashMap<>();

        for (Skill skill : getSkillHandler().getValues()) map.put(skill, new SkillInstance(skill, 1, skill.getXPForMaxOut(), skill.getDefaultRewardArray()));
        return map;
    }

    public static Map<Skill, Integer> emptyHighScoreMap() {

        Map<Skill, Integer> map = new HashMap<>();

        for (Skill skill : getSkillHandler().getValues()) map.put(skill, 1);
        return map;
    }

    public static Map<Skill, Integer> maxHighScoreMap() {

        Map<Skill, Integer> map = new HashMap<>();

        for (Skill skill : getSkillHandler().getValues()) map.put(skill, skill.maxLevel);
        return map;
    }

    /**
     * Deserializing from player-json pair.
     */
    static SkillData deserialize(ServerPlayer player, JsonObject jsonObject) {

        Map<Skill, SkillInstance> skillMap = new HashMap<>();
        Map<Skill, Integer> highScores = new HashMap<>();

        if (jsonObject.has("skills")) {

            JsonArray array = GsonHelper.getAsJsonArray(jsonObject, "skills");

            for (JsonElement element : array) {

                SkillInstance instance = SkillInstance.deserialize(element.getAsString());
                if (instance != null) skillMap.put(instance.skill, instance);
            }

        } else skillMap = emptySkillMap();

        if (jsonObject.has("high_scores")) {

            JsonObject map = GsonHelper.getAsJsonObject(jsonObject, "high_scores");

            for (Skill skill : skillMap.keySet()) {

                String string = skill.toString();
                int highScore = map.has(string) ? GsonHelper.getAsInt(map, string) : skillMap.get(skill).level;

                highScores.put(skill, highScore);
            }

        } else {

            for (Skill skill : skillMap.keySet()) {

                int highScore = skillMap.get(skill).level;
                highScores.put(skill, highScore);
            }
        }

        SecretData secretData = jsonObject.has("secret_data") ? SecretData.deserialize(GsonHelper.getAsJsonArray(jsonObject, "secret_data")) : new SecretData();
        int ascensions = jsonObject.has("ascensions") ? GsonHelper.getAsInt(jsonObject, "ascensions") : 0;

        return new SkillData(player, skillMap, highScores, secretData, ascensions, false);
    }

    public void reset() {

        skillMap = emptySkillMap();
        highScores = emptyHighScoreMap();
        secretData = secretData.reset(player);
        ascensions = 0;

        update();
    }

    public void maxOut() {

        skillMap = maxSkillMap();
        highScores = maxHighScoreMap();
        secretData = secretData.maxOut(player);
        ascensions = Math.max(ascensions, 10);

        int highestLevel = maxHighScoreMap().values().stream().max(Integer::compareTo).orElse(1);
        int highestXP = maxSkillMap().values().stream().map(SkillInstance::totalXP).max(Integer::compareTo).orElse(0);

        PSCriteriaTriggers.LEVEL_UP.trigger(player, highestLevel);
        PSCriteriaTriggers.GAIN_XP.trigger(player, highestXP);

        update();
    }

    public void ascend() {

        skillMap.replaceAll((skill, instance) -> new SkillInstance(skill, getLegacy() + 1, 0, instance.getRewardArrayForAscension()));
        ascensions++;
        if (!PoscardsSkillsCommonConfig.KEEP_SECRETS.get()) secretData = new SecretData();

        update();
    }

    public void setAscensions(int count) {

        ascensions = count;
        update();
    }

    public SkillInstance getSkill(Skill skill) {

        if (skillMap.containsKey(skill)) { return skillMap.get(skill); } else {

            SkillInstance instance = new SkillInstance(skill);
            skillMap.put(skill, instance);
            return instance;
        }
    }

    public int getLevel(Skill skill) { return getSkill(skill).level; }

    public int getHighScore(Skill skill) {

        if (highScores.containsKey(skill)) { return highScores.get(skill); } else {

            highScores.put(skill, 1);
            return 1;
        }
    }

    public int getTotalXP() {

        int total = 0;
        for (SkillInstance instance : skillMap.values()) total += instance.totalXP();
        return total;
    }

    public float getMaxAvgLevel() {

        int levels = 0;
        int skills = 0;

        for (Skill skill : skillMap.keySet()) {

            levels += skill.maxLevel;
            skills += 1;
        }
        return (float) levels / skills;
    }

    public int getLegacy() { return Math.round((float) secretData.getAttributeModifier().getValue().getAmount()); }

    public void updateSkill(SkillInstance instance) {

        Skill skill = instance.skill;

        skillMap.put(skill, instance);
        highScores.put(skill, Math.max(getLevel(skill), getHighScore(skill)));
        update();
    }

    public void updateSecrets(SecretData secretData) {

        this.secretData = secretData;
        update();
    }

    public void update() {

        if (updating) return;

        updating = true;

        updateFile();
        updateAttributeModifiers();

        updating = false;
    }

    Pair<String, JsonObject> serialize() {

        JsonObject jsonObject = new JsonObject();
        JsonArray skills = new JsonArray();
        JsonObject highScores = new JsonObject();

        for (SkillInstance instance : skillMap.values()) skills.add(instance.serialize());
        for (Skill skill : getSkillHandler().getValues()) highScores.addProperty(skill.toString(), getHighScore(skill));

        jsonObject.add("skills", skills);
        jsonObject.add("high_scores", highScores);
        jsonObject.add("secret_data", secretData.serialize());
        jsonObject.addProperty("ascensions", ascensions);

        return Pair.of(uuid, jsonObject);
    }

    void updateAttributeModifiers() {

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();

        builder.put(secretData.getAttributeModifier());
        for (SkillInstance instance : skillMap.values()) builder.put(instance.getAttributeModifier());

        ImmutableMultimap<Attribute, AttributeModifier> multimap = builder.build();

        try {

            player.getAttributes().removeAttributeModifiers(multimap);
            player.getAttributes().addTransientAttributeModifiers(multimap);

        } catch (Exception exception) { LOGGER.error("Error adding skill attribute modifiers."); }
    }

    /**
     * Writes data to file.
     */
    void updateFile() {

        try {

            String initial = FileUtils.readFileToString(file);
            String string = initial.isEmpty() ? "{}" : initial;

            StringReader stringReader = new StringReader(string);
            JsonReader jsonReader = new JsonReader(stringReader);
            jsonReader.setLenient(false);

            Pair<String, JsonObject> pair = serialize();
            
            JsonObject jsonObject = Streams.parse(jsonReader).getAsJsonObject();
            jsonObject.add(pair.getFirst(), pair.getSecond());

            String serialized = jsonObject.toString();
            FileUtils.writeStringToFile(file, serialized);

            stringReader.close();
            jsonReader.close();

        } catch (IOException exception) { LOGGER.error("Parsing error writing player skill data."); }
    }

}