package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.experiencesource.SimpleExperienceSource;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

public class CraftItemExperienceSource extends SimpleExperienceSource<ItemStack> {

    private static final Map<Player, List<ItemStack>> WAITING_ITEMS_MAP = new HashMap<>();


    CraftItemExperienceSource(Skill skill, int xp, Predicate<ItemStack> predicate) { super(skill, xp, predicate); }

    public static CraftItemExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String itemArg;
        Predicate<ItemStack> predicate;

        if (jsonObject.has("item")) {

            itemArg = GsonHelper.getAsString(jsonObject, "item");
            if (itemArg.equals("ALL")) return new CraftItemExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ITEM_STACK);

            ResourceLocation itemKey = ResourceLocation.tryParse(itemArg);
            Item item = ForgeRegistries.ITEMS.getValue(itemKey);
            predicate = stack -> stack.is(item);

        } else {

            itemArg = GsonHelper.getAsString(jsonObject, "tag");
            if (itemArg.equals("ALL")) return new CraftItemExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ITEM_STACK);

            ResourceLocation tagLocation = ResourceLocation.tryParse(itemArg);
            TagKey<Item> tag = TagKey.create(ForgeRegistries.Keys.ITEMS, Objects.requireNonNull(tagLocation));
            predicate = stack -> stack.is(tag);
        }
        return new CraftItemExperienceSource(skill, xp, predicate);
    }

    public static void handlePlayer(Player player) {

        if (player == null) return;
        if (getWaitingItems(player).size() == 0) return;

        for (ItemStack stack : getWaitingItems(player)) {

            for (CraftItemExperienceSource xpSource : ExperienceSource.filterBy(CraftItemExperienceSource.class)) xpSource.applyIfMeetsConditions(player, stack, stack.getCount() - 1);
        }

        removeWaitingItems(player);
    }

    public static List<ItemStack> getWaitingItems(Player player) { return WAITING_ITEMS_MAP.getOrDefault(player, new ArrayList<>()); }

    public static void addWaitingItem(Player player, ItemStack stack) {

        if (stack.isEmpty()) return;

        List<ItemStack> existing = getWaitingItems(player);
        boolean combined = false;

        for (ItemStack existingStack : existing) {

            if (ItemStack.isSame(existingStack, stack)) {

                if (existingStack.getCount() + stack.getCount() <= stack.getItem().getMaxStackSize(stack)) {

                    existingStack.grow(stack.getCount());
                    combined = true;
                    break;
                }
            }
        }
        if (!combined) existing.add(stack.copy());
        WAITING_ITEMS_MAP.put(player, existing);
    }

    public static void removeWaitingItems(Player player) { WAITING_ITEMS_MAP.remove(player); }


}
