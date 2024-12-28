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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Gives xp when the player consumes an item. See the wiki for the format.
 */
public final class ConsumeExperienceSource extends SimpleExperienceSource<ItemStack> {

    ConsumeExperienceSource(Skill skill, int xp, Predicate<ItemStack> predicate) { super(skill, xp, predicate); }

    public static ConsumeExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String itemArg;
        Predicate<ItemStack> predicate;

        if (jsonObject.has("item")) {

            itemArg = GsonHelper.getAsString(jsonObject, "item");
            if (itemArg.equals("ALL")) return new ConsumeExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ITEM_STACK);

            ResourceLocation itemKey = ResourceLocation.tryParse(itemArg);
            Item item = ForgeRegistries.ITEMS.getValue(itemKey);
            predicate = stack -> stack.is(item);

        } else {

            itemArg = GsonHelper.getAsString(jsonObject, "tag");
            if (itemArg.equals("ALL")) return new ConsumeExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_ITEM_STACK);

            ResourceLocation tagLocation = ResourceLocation.tryParse(itemArg);
            TagKey<Item> tag = TagKey.create(ForgeRegistries.Keys.ITEMS, Objects.requireNonNull(tagLocation));
            predicate = stack -> stack.is(tag);
        }
        return new ConsumeExperienceSource(skill, xp, predicate);
    }

    public static void handlePlayer(@Nullable ServerPlayer player, ItemStack stack) {

        if (player == null) return;
        for (ConsumeExperienceSource xpSource : ExperienceSource.filterBy(ConsumeExperienceSource.class)) xpSource.applyIfMeetsConditions(player, stack);
    }

}
