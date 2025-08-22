package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.peritia.util.text.ColorGradient;
import github.poscard8.poscardsskills.item.RuneItem;
import github.poscard8.poscardsskills.recipe.RuneCraftingRecipe;
import github.poscard8.poscardsskills.util.PSTags;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class RuneCraftingExtension implements ICraftingCategoryExtension
{
    public final RuneCraftingRecipe recipe;

    public RuneCraftingExtension(RuneCraftingRecipe recipe) { this.recipe = recipe; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper gridHelper, IFocusGroup focusGroup)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(List.of(Ingredient.of(PSTags.Items.NON_EMPTY_RUNES).getItems()));
        inputs.add(List.of(Ingredient.of(PSTags.Items.RUNE_APPLICABLE).getItems()));

        gridHelper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
        gridHelper.createAndSetOutputs(builder, List.of(ItemStack.EMPTY));
    }

    @Override
    public void onDisplayedIngredientsUpdate(List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses)
    {
        ItemStack rune = recipeSlots.get(0).getDisplayedItemStack().orElse(ItemStack.EMPTY);
        ItemStack stack = recipeSlots.get(1).getDisplayedItemStack().orElse(Items.IRON_SWORD.getDefaultInstance());
        ItemStack copy = stack.copy();

        @Nullable ColorGradient gradient = rune.getItem() instanceof RuneItem runeItem ? runeItem.gradient() : null;
        if (gradient != null) ColorGradient.addToItem(gradient, copy);

        recipeSlots.get(9).createDisplayOverrides().addItemStack(copy);
    }

    @Override
    public int getWidth() { return 2; }

    @Override
    public int getHeight() { return 2; }

}
