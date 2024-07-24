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
import github.poscard8.poscardsskills.skill.misc.Additional;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class SkillData {

    public static Logger LOGGER = LogUtils.getLogger();

    public final File file;
    public final Player player;
    public Map<Skill, SkillInstance> skillMap;
    private final List<String> additionalData;

    private SkillData(@NotNull Player player, Map<Skill, SkillInstance> skillMap, List<String> additionalData) {

        this.player = player;
        this.skillMap = skillMap;
        this.file = getOrCreateFile();
        this.additionalData = additionalData;

        update();
    }

    @NotNull
    public static SkillData of(Player player) {

        try {

            assert player.getServer() != null;
            File file = getOrCreateFile();

            String initial = FileUtils.readFileToString(file);
            String string = initial.equals("") ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            JsonObject jsonObject = Streams.parse(jsonReader).getAsJsonObject();
            String uuid = player.getUUID().toString();

            return jsonObject.has(uuid) ? deserialize(player, GsonHelper.getAsJsonArray(jsonObject, uuid)) : new SkillData(player, generateEmptySkillMap(), List.of());

        } catch (IOException exception) { throw new RuntimeException("Parsing error loading player skill data."); }
    }

    public static List<SkillRecipe> getAllRecipes() {

        List<SkillRecipe> recipes = new ArrayList<>();
        Map<Skill, SkillInstance> map = generateEmptySkillMap();

        for (int i = 0; i <= Skill.MAX_LEVEL; i++) {

            for (Skill skill : PoscardsSkills.getSkillHandler().getSortedValues()) {

                SkillMilestone milestone = map.get(skill).milestone(i);
                recipes.addAll(milestone.recipes);
            }
        }

        return recipes;
    }

    public SkillData copy() {

        Map<Skill, SkillInstance> newMap = Map.copyOf(skillMap);
        List<String> newData = List.copyOf(additionalData);

        return new SkillData(player, newMap, newData);
    }

    public SkillInstance getSkill(Skill skill) {

        if (skillMap.containsKey(skill)) { return skillMap.get(skill); } else {

            SkillInstance instance = new SkillInstance(skill);
            skillMap.put(skill, instance);
            return instance;
        }
    }

    public boolean hasAdditional(String additionalKey) {

        for (Skill skill : skillMap.keySet()) {

            for (Additional additional : skill.additional) {

                if (additional.key.equals(additionalKey) && getSkill(skill).level >= additional.at) return true;
            }
        }

        return false;
    }

    public void updateAttributeModifiers() {

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();

        for (SkillInstance instance : skillMap.values()) builder.put(instance.getAttributeModifier());

        ImmutableMultimap<Attribute, AttributeModifier> multimap = builder.build();

        try {

            player.getAttributes().removeAttributeModifiers(multimap);
            player.getAttributes().addTransientAttributeModifiers(multimap);

        } catch (Exception ignored) {}
    }

    public void update(SkillInstance instance) {

        skillMap.put(instance.skill, instance);
        update();
    }

    public void update() {

        updateFile();
        updateAttributeModifiers();
    }

    private static SkillData deserialize(Player player, JsonArray array) {

        Map<Skill, SkillInstance> skillMap = new HashMap<>();
        List<String> additionalData = new ArrayList<>();

        for (JsonElement element : array) {

            SkillInstance instance = SkillInstance.deserialize(element.getAsString());
            if (instance != null) { skillMap.put(instance.skill, instance); } else additionalData.add(element.getAsString());
        }

        return new SkillData(player, skillMap, additionalData);
    }

    private static File getOrCreateFile() {

        MinecraftServer server = PSUtils.getCurrentServer();
        File directory = server.getWorldPath(PoscardsSkills.DIRECTORY).toFile();
        File file = new File(directory, "skill_data.json");

        try { Files.createDirectory(directory.toPath()); } catch (FileAlreadyExistsException ignored) {} catch (IOException e) { throw new RuntimeException(e); }
        try { boolean ignored = file.createNewFile(); } catch (IOException e) { throw new RuntimeException(e); }

        return file;
    }

    private static Map<Skill, SkillInstance> generateEmptySkillMap() {

        Map<Skill, SkillInstance> map = new HashMap<>();

        for (Skill skill : PoscardsSkills.getSkillHandler().getValues()) { map.put(skill, new SkillInstance(skill)); }
        return map;
    }

    private Pair<String, JsonArray> serialize() {

        String uuid = player.getUUID().toString();
        JsonArray array = new JsonArray();

        for (SkillInstance instance : skillMap.values()) array.add(instance.serialize());
        for (String string : additionalData) array.add(string);

        return Pair.of(uuid, array);
    }

    private void updateFile() {

        try {

            String initial = FileUtils.readFileToString(file);
            String string = initial.equals("") ? "{}" : initial;

            JsonReader jsonReader = new JsonReader(new StringReader(string));
            jsonReader.setLenient(false);

            Pair<String, JsonArray> pair = serialize();

            JsonObject jsonObject = Streams.parse(jsonReader).getAsJsonObject();
            jsonObject.add(pair.getFirst(), pair.getSecond());

            FileUtils.writeStringToFile(file, jsonObject.toString());

        } catch (IOException exception) { LOGGER.error("Parsing error generating player skill data."); }
    }

}
