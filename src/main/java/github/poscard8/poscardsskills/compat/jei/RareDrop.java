package github.poscard8.poscardsskills.compat.jei;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import github.poscard8.poscardsskills.util.item.PSItemUtils;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record RareDrop(Item item, int minCount, int maxCount) {

    public static final RecipeType<RareDrop> TYPE = RecipeType.create("poscardsskills", "rare_drop", RareDrop.class);
    public static final List<RareDrop> VALUES = new ArrayList<>();
    private static boolean initialized = false;

    public static List<RareDrop> getAll() {

        if (initialized) return VALUES;

        List<RareDrop> drops = new ArrayList<>();

        LootTable lootTable = PSUtils.getCurrentServer().getLootTables().get(PoscardsSkills.asResource("gameplay/rare_drop"));
        JsonObject mainObject = LootTables.serialize(lootTable).getAsJsonObject();
        JsonArray pools = GsonHelper.getAsJsonArray(mainObject, "pools");
        JsonArray entries = GsonHelper.getAsJsonArray(pools.get(0).getAsJsonObject(), "entries");

        for (JsonElement element : entries) {

            JsonObject object = element.getAsJsonObject();
            RareDrop drop = fromJsonObject(object);
            if (drop != null) drops.add(drop);
        }

        VALUES.addAll(drops);
        initialized = true;
        return drops;
    }

    @Nullable
    public static RareDrop fromJsonObject(JsonObject jsonObject) {

        if (GsonHelper.getAsString(jsonObject, "type").equals("minecraft:item")) {

            ResourceLocation itemKey = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "name"));
            Item item = ForgeRegistries.ITEMS.getValue(itemKey);

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

            return new RareDrop(item, minCount, maxCount);
        }
        return null;
    }

    public ItemStack getItemDisplay() {

        ItemStack stack = item.getDefaultInstance();
        stack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        stack.hideTooltipPart(ItemStack.TooltipPart.MODIFIERS);
        PSItemUtils.addText(stack, PSComponents.range(minCount, maxCount));

        return stack;
    }

}
