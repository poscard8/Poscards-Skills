package github.poscard8.poscardsskills.skill.misc;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Requisite to lock items behind a certain skill level. Unused by mod skills,
 * but can be used by custom skills.
 * <p>{@link #skillKey}: Key of the skill. Item requisites are loaded before skills,
 * so we can't pass a skill here.</p>
 * <p>{@link #at}: The level the item is <b>unlocked</b> at.</p>
 * <p>{@link #predicate}: Target items. Can be a single item or a tag.</p>
 */
public class ItemLock implements BiPredicate<ServerPlayer, ItemStack>, RequisiteHolder {

    public final Predicate<ItemStack> predicate;
    public final int at;

    final ResourceLocation skillKey;

    ItemLock(ResourceLocation skillKey, int at, Predicate<ItemStack> predicate) {

        this.skillKey = skillKey;
        this.at = at;
        this.predicate = predicate;
    }

    public static ItemLock fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        int at = GsonHelper.getAsInt(jsonObject, "at");
        Predicate<ItemStack> predicate;
        String string;

        if (jsonObject.has("item")) {

            string = GsonHelper.getAsString(jsonObject, "item");

            ResourceLocation itemKey = ResourceLocation.tryParse(string);
            Item item = ForgeRegistries.ITEMS.getValue(itemKey);
            predicate = stack -> stack.is(item);

        } else {

            string = GsonHelper.getAsString(jsonObject, "tag");

            ResourceLocation tagLocation = ResourceLocation.tryParse(string);
            TagKey<Item> tag = TagKey.create(ForgeRegistries.Keys.ITEMS, Objects.requireNonNull(tagLocation));
            predicate = stack -> stack.is(tag);
        }

        return Skill.isValidLevel(at) ? new ItemLock(skillKey, at, predicate) : null;
    }

    /**
     * Items can have multiple requisites.
     */
    public static List<ItemLock> getRequisitesFor(ItemStack stack) {

        if (stack.isEmpty()) return List.of();

        List<Skill> skills = PoscardsSkills.getSkillHandler().getValues();
        List<ItemLock> requisites = new ArrayList<>();

        for (Skill skill : skills) {
            for (ItemLock requisite : skill.itemLocks) {

                if (requisite.predicate.test(stack)) requisites.add(requisite);
            }
        }
        return requisites;
    }

    public static boolean isItemLockedFor(@Nullable ServerPlayer player, ItemStack stack) {

        if (player == null) return false;
        List<ItemLock> requisites = getRequisitesFor(stack);

        for (ItemLock requisite : requisites) {

            if (!requisite.test(player, stack)) return true;
        }
        return false;
    }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at, PoscardsSkillsCommonConfig.KEEP_UNLOCKED_ITEMS.get()); }

    /**
     * Disables the requisite on creative and spectator modes.
     */
    @Override
    public boolean test(ServerPlayer player, ItemStack stack) {

        return !predicate.test(stack) || getRequisite().test(player) || player.isCreative() || player.isSpectator();
    }


}
