package github.poscard8.poscardsskills.compat.jei;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.registry.PSItems;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JEI utility class for ascensions. {@code catalyst} specifies the catalyst item,
 * {@code drops} specifies the item drops which are determined by loot tables.
 */
public record Ascension(Item catalyst, List<AscensionDrop> drops) {

    public static final List<Ascension> VALUES = new ArrayList<>();
    private static boolean INITIALIZED = false;

    /**
     * Init function for ascension recipes. The values are only initialized once.
     */
    public static List<Ascension> getValues() {

        if (INITIALIZED) return VALUES;

        List<Ascension> ascensions = new ArrayList<>();
        Map<ResourceLocation, Resource> map = PSUtils.getServer().getResourceManager().listResources("loot_tables", location -> location.getPath().endsWith(".json"));

        for (Map.Entry<Item, ResourceLocation> entry : getPairs().entrySet()) {

            Resource resource = map.get(entry.getValue());

            try {

                BufferedReader reader = resource.openAsReader();

                JsonObject mainObject = GsonHelper.fromJson(new Gson(), reader, JsonObject.class);
                reader.close();

                Ascension ascension = fromJsonObject(entry.getKey(), mainObject);
                if (ascension != null) ascensions.add(ascension);

            } catch (Exception ignored) {}
        }

        VALUES.addAll(ascensions);
        INITIALIZED = true;
        return ascensions;
    }

    /**
     * Registering catalyst and loot table pairs.
     */
    public static Map<Item, ResourceLocation> getPairs() {

        Map<Item, ResourceLocation> map = new HashMap<>();

        if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

            map.put(PSItems.BRILLIANT_CATALYST.get(), PoscardsSkills.asResource("loot_tables/gameplay/brilliant_catalyst.json"));
            map.put(PSItems.BLESSED_CATALYST.get(), PoscardsSkills.asResource("loot_tables/gameplay/blessed_catalyst.json"));
            map.put(PSItems.DIVINE_CATALYST.get(), PoscardsSkills.asResource("loot_tables/gameplay/divine_catalyst.json"));

        } else {

            map.put(PSItems.BRILLIANT_CATALYST.get(), PoscardsSkills.asResource("loot_tables/gameplay/brilliant_catalyst_no_extra_progression.json"));
        }

        return map;
    }

    /**
     * Catalysts in item stack form.
     */
    public static List<ItemStack> getCatalysts() { return getPairs().keySet().stream().map(Item::getDefaultInstance).toList(); }

    /**
     * Manually deserializes the loot table file.<p>
     * Returns {@code null} if encounters an exception. If returns {@code null},
     * the ascension recipe is ignored.
     */
    @Nullable
    public static Ascension fromJsonObject(Item item, JsonObject jsonObject) {

        JsonArray pools = GsonHelper.getAsJsonArray(jsonObject, "pools");
        List<AscensionDrop> drops = new ArrayList<>();

        try {

            for (JsonElement element : pools) {

                JsonObject pool = element.getAsJsonObject();
                JsonArray entries = GsonHelper.getAsJsonArray(pool, "entries");

                int totalWeight = 0;
                for (JsonElement entry : entries) totalWeight += AscensionDrop.getWeightOf(entry.getAsJsonObject(), true);

                for (JsonElement entry : entries) {

                    JsonObject object = entry.getAsJsonObject();
                    AscensionDrop drop = AscensionDrop.fromJsonObject(object, totalWeight);
                    if (drop != null) drops.add(drop);
                }
            }

            return new Ascension(item, drops);

        } catch (Exception exception) { return null; }
    }

    /**
     * Each one of these are deserialized by loot table entries.
     * {@code minCount} and {@code maxCount} specify the count range.
     */
    public record AscensionDrop(Item item, float chance, int minCount, int maxCount) {

        /**
         * Deserializer function.
         */
        @Nullable
        public static AscensionDrop fromJsonObject(JsonObject jsonObject, int totalWeight) {

            if (GsonHelper.getAsString(jsonObject, "type").equals("minecraft:item")) {

                ResourceLocation itemKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "name"));
                Item item = ForgeRegistries.ITEMS.getValue(itemKey);

                int weight = getWeightOf(jsonObject, false);
                float chance = (float) weight / totalWeight;

                int minCount = -1;
                int maxCount = -1;

                JsonArray functions = GsonHelper.getAsJsonArray(jsonObject, "functions");
                for (JsonElement element : functions) {

                    JsonObject function = element.getAsJsonObject();
                    if (GsonHelper.getAsString(function, "function").equals("minecraft:set_count")) {

                        JsonElement countElement = function.get("count");

                        if (countElement.isJsonPrimitive()) {

                            int count = countElement.getAsInt();
                            minCount = count;
                            maxCount = count;

                        } else if (countElement.isJsonObject()) {

                            JsonObject countMap = countElement.getAsJsonObject();
                            minCount = GsonHelper.getAsInt(countMap, "min");
                            maxCount = GsonHelper.getAsInt(countMap, "max");

                        } else return null;
                    }
                }

                minCount = Math.max(0, minCount);
                maxCount = Math.max(0, maxCount);

                return new AscensionDrop(item, chance, minCount, maxCount);
            }
            return null;
        }

        /**
         * Weight of a loot table entry.
         *
         * @param jsonObject Loot table entry object.
         * @param check Checks if the jsonObject is valid or not.
         *              Passed as {@code false} in the second calculation to save performance.
         */
        public static int getWeightOf(JsonObject jsonObject, boolean check) {

            if (check && fromJsonObject(jsonObject.deepCopy(), 1) == null) return 0;
            return jsonObject.has("weight") ? jsonObject.get("weight").getAsInt() : 1;
        }

        public ItemStack getItemDisplay(int count) {

            ItemStack stack = new ItemStack(item, count);
            stack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
            stack.hideTooltipPart(ItemStack.TooltipPart.MODIFIERS);
            PSUtils.addComponentsToItem(stack, PSComponents.chance(chance));

            return stack;
        }

        /**
         * Used for creating multi-item displays.
         */
        public List<ItemStack> getItemDisplays() {

            List<ItemStack> stacks = new ArrayList<>();
            for (int i = minCount; i <= maxCount; i++) stacks.add(getItemDisplay(i));

            return stacks;
        }

    }

}
