package github.poscard8.poscardsskills.skill;

import com.google.gson.JsonObject;
import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.skill.misc.RequisiteHolder;
import github.poscard8.poscardsskills.skill.misc.SkillRequisite;
import github.poscard8.poscardsskills.util.PSUtils;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SkillRecipe implements RequisiteHolder {

    public static final RecipeType<SkillRecipe> JEI_TYPE = RecipeType.create("poscardsskills", "skill_crafting", SkillRecipe.class);

    public final ItemStack input1;
    public final @Nullable ItemStack input2;
    public final ItemStack output;

    public final int at;
    private final ResourceLocation skillKey;

    SkillRecipe(ResourceLocation skillKey, int at, ItemStack input1, ItemStack input2, ItemStack output) {

        this.skillKey = skillKey;
        this.at = at;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    public static SkillRecipe fromJsonObject(ResourceLocation skillKey, JsonObject jsonObject) {

        try {

            int at = GsonHelper.getAsInt(jsonObject, "at");
            ItemStack input1 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "input1"));
            ItemStack input2 = jsonObject.has("input2") ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "input2")) : null;
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

            return Skill.isValidLevel(at) ? new SkillRecipe(skillKey, at, input1, input2, output) : null;

        } catch (Exception exception) { return null; }
    }

    public boolean canCraft(Player player) {

        boolean hasInput1 = false;
        boolean hasInput2 = input2 == null;

        NonNullList<ItemStack> stacks = player.getInventory().items;

        for (ItemStack stack : stacks) {

            if (ItemStack.isSame(stack, input1) && stack.getCount() >= input1.getCount()) hasInput1 = true;
            if (input2 != null && ItemStack.isSame(stack, input2) && stack.getCount() >= input2.getCount()) hasInput2 = true;
        }

        return hasInput1 && hasInput2 && isUnlockedFor(player);
    }

    protected Optional<ItemStack> findInput1(ServerPlayer player) {

        NonNullList<ItemStack> stacks = player.getInventory().items;

        for (ItemStack stack : stacks) {

            if (ItemStack.isSame(stack, input1) && stack.getCount() >= input1.getCount()) return Optional.of(stack);
        }
        return Optional.empty();
    }

    protected Optional<ItemStack> findInput2(ServerPlayer player) {

        if (input2 == null) return Optional.empty();

        NonNullList<ItemStack> stacks = player.getInventory().items;

        for (ItemStack stack : stacks) {

            if (ItemStack.isSame(stack, input2) && stack.getCount() >= input2.getCount()) return Optional.of(stack);
        }
        return Optional.empty();
    }

    public void craftSingle(Player player) {

        if (canCraft(player)) {

            ServerPlayer serverPlayer = PSUtils.getServerPlayer(player);

            if (serverPlayer != null) {

                Inventory inventory = serverPlayer.getInventory();

                Optional<ItemStack> optional1 = findInput1(serverPlayer);
                Optional<ItemStack> optional2 = findInput2(serverPlayer);

                boolean changedInput1 = optional1.isPresent();
                boolean changedInput2 = optional2.isPresent() || input2 == null;

                if (optional1.isPresent()) {

                    ItemStack newInput1 = optional1.get();
                    newInput1.shrink(input1.getCount());
                }

                if (optional2.isPresent()) {

                    assert input2 != null;

                    ItemStack newInput2 = optional2.get();
                    newInput2.shrink(input2.getCount());
                }

                if (changedInput1 && changedInput2) {

                    inventory.placeItemBackInInventory(assemble());
                    PSCriteriaTriggers.SKILL_CRAFTING.trigger(serverPlayer, assemble());
                }
            }
        }
    }

    public void craftStack(Player player) {

        int times = output.getMaxStackSize();
        boolean canCraft = canCraft(player);

        while (canCraft && times > 0) {

            craftSingle(player);
            times--;
            canCraft = canCraft(player);
        }
    }

    public ItemStack assemble() { return output.copy(); }

    public boolean isUnlockedFor(Player player) { return getRequisite().test(player); }

    @Override
    public SkillRequisite getRequisite() { return new SkillRequisite(Skill.byKey(skillKey), at); }

}
