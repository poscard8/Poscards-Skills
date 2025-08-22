package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.peritia.util.text.ColorGradient;
import github.poscard8.peritia.util.text.ColorGradients;
import github.poscard8.poscardsskills.recipe.RuneCraftingClearRecipe;
import github.poscard8.poscardsskills.util.PSTags;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class RuneCraftingClearExtension implements ICraftingCategoryExtension
{
    public final RuneCraftingClearRecipe recipe;

    public RuneCraftingClearExtension(RuneCraftingClearRecipe recipe) { this.recipe = recipe; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper gridHelper, IFocusGroup focusGroup)
    {
        List<List<ItemStack>> inputs = new ArrayList<>();
        List<ItemStack> runes = List.of(Ingredient.of(PSTags.Items.EMPTY_RUNES).getItems());
        List<ItemStack> items = List.of(Ingredient.of(PSTags.Items.RUNE_APPLICABLE).getItems());

        items.forEach(stack -> ColorGradient.addToItem(getRandomGradient(), stack));

        inputs.add(runes);
        inputs.add(items);

        gridHelper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
        gridHelper.createAndSetOutputs(builder, List.of(ItemStack.EMPTY));
    }

    @Override
    public void onDisplayedIngredientsUpdate(List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses)
    {
        ItemStack stack = recipeSlots.get(1).getDisplayedItemStack().orElse(Items.IRON_SWORD.getDefaultInstance());
        ItemStack copy = stack.copy();

        ColorGradient.removeFromItem(copy);
        recipeSlots.get(9).createDisplayOverrides().addItemStack(copy);
    }

    @Override
    public int getWidth() { return 2; }

    @Override
    public int getHeight() { return 2; }

    ColorGradient getRandomGradient()
    {
        List<ColorGradient> gradients = List.of
                (
                        ColorGradients.GOLD,
                        ColorGradients.DIAMOND,
                        ColorGradients.NETHERITE,
                        ColorGradients.AMETHYST,
                        ColorGradients.EMERALD,
                        ColorGradients.COMPOSITE
                );

        int index = new Random().nextInt(gradients.size());
        return gradients.get(index);
    }

}
