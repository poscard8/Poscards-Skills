package github.poscard8.poscardsskills.skill.misc;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ItemRequisite implements BiPredicate<Player, ItemStack>, RequisiteHolder {

    public final Predicate<ItemStack> predicate;
    public final int at;

    private final ResourceLocation skillKey;

    ItemRequisite(ResourceLocation skillKey, int at, Predicate<ItemStack> predicate) {

        this.skillKey = skillKey;
        this.at = at;
        this.predicate = predicate;
    }

    public static ItemRequisite fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

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

        return Skill.isValidLevel(at) ? new ItemRequisite(skillKey, at, predicate) : null;
    }

    public static List<ItemRequisite> getRequisitesFor(ItemStack stack) {

        if (stack.isEmpty()) return List.of();

        List<Skill> skills = PoscardsSkills.getSkillHandler().getSortedValues();
        List<ItemRequisite> requisites = new ArrayList<>();

        for (Skill skill : skills) {
            for (ItemRequisite requisite : skill.itemRequisites) {

                if (requisite.predicate.test(stack)) requisites.add(requisite);
            }
        }
        return requisites;
    }

    public static boolean isLockedFor(Player player, ItemStack stack) {

        List<ItemRequisite> requisites = getRequisitesFor(stack);

        for (ItemRequisite requisite : requisites) {

            if (!requisite.test(player, stack)) return true;
        }
        return false;
    }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at); }

    @Override
    public boolean test(Player player, ItemStack stack) {

        return !predicate.test(stack) || getRequisite().test(player) ||
                player.isCreative() || player.isSpectator();
    }


}
