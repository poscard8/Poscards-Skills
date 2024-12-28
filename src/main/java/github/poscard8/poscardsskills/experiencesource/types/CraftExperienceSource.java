package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.experiencesource.SimpleExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Gives xp when the player crafts items.
 * Uses arbitrary containers to give the xp <b>after</b> the player
 * closes the crafting table.
 * <p>See the wiki for the format.</p>
 */
public final class CraftExperienceSource extends SimpleExperienceSource<ItemStack> {

    static final Map<ServerPlayer, SimpleContainer> WAITING_ITEMS_MAP = new HashMap<>();
    static final int CONTAINER_SIZE = 27;

    CraftExperienceSource(Skill skill, int xp, Predicate<ItemStack> predicate) { super(skill, xp, predicate); }

    public static CraftExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String itemArg;
        Predicate<ItemStack> predicate;

        if (jsonObject.has("item")) {

            itemArg = GsonHelper.getAsString(jsonObject, "item");
            if (itemArg.equals("ALL")) return new CraftExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ITEM_STACK);

            ResourceLocation itemKey = ResourceLocation.tryParse(itemArg);
            Item item = ForgeRegistries.ITEMS.getValue(itemKey);
            predicate = stack -> stack.is(item);

        } else {

            itemArg = GsonHelper.getAsString(jsonObject, "tag");
            if (itemArg.equals("ALL")) return new CraftExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ITEM_STACK);

            ResourceLocation tagLocation = ResourceLocation.tryParse(itemArg);
            TagKey<Item> tag = TagKey.create(ForgeRegistries.Keys.ITEMS, Objects.requireNonNull(tagLocation));
            predicate = stack -> stack.is(tag);
        }
        return new CraftExperienceSource(skill, xp, predicate);
    }

    public static void handlePlayer(@Nullable ServerPlayer player) {

        if (player == null) return;

        for (int i = 0; i < CONTAINER_SIZE; i++) {

            ItemStack stack = getWaitingItems(player).getItem(i);
            if (stack.isEmpty()) continue;

            for (CraftExperienceSource xpSource : ExperienceSource.filterBy(CraftExperienceSource.class)) {

                xpSource.applyIfMeetsConditions(player, stack, getMultiplier(stack));
            }
        }
        removeWaitingItems(player);
    }

    public static SimpleContainer getWaitingItems(ServerPlayer player) { return WAITING_ITEMS_MAP.getOrDefault(player, new SimpleContainer(CONTAINER_SIZE)); }

    public static void addWaitingItem(ServerPlayer player, ItemStack stack) { getWaitingItems(player).addItem(stack); }

    public static void removeWaitingItems(ServerPlayer player) { WAITING_ITEMS_MAP.put(player, new SimpleContainer(CONTAINER_SIZE)); }

    static int getMultiplier(ItemStack stack) { return stack.getCount() - 1; }

}
