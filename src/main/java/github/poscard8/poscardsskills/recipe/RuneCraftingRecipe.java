package github.poscard8.poscardsskills.recipe;

import github.poscard8.peritia.util.text.ColorGradient;
import github.poscard8.poscardsskills.item.RuneItem;
import github.poscard8.poscardsskills.registry.PSRecipeSerializers;
import github.poscard8.poscardsskills.util.PSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RuneCraftingRecipe extends CustomRecipe
{
    public RuneCraftingRecipe(ResourceLocation key, CraftingBookCategory category) { super(key, category); }

    @Override
    public boolean matches(CraftingContainer container, Level level)
    {
        int totalRuneCount = 0;
        int runeCount = 0;
        int itemCount = 0;

        for (ItemStack stack : container.getItems())
        {
            if (stack.is(PSTags.Items.RUNES)) totalRuneCount++;
            if (stack.is(PSTags.Items.NON_EMPTY_RUNES)) runeCount++;
            if (stack.is(PSTags.Items.RUNE_APPLICABLE)) itemCount++;
        }
        return totalRuneCount == 1 && runeCount == 1 && itemCount == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess)
    {
        ItemStack rune = ItemStack.EMPTY;
        ItemStack item = ItemStack.EMPTY;

        for (ItemStack stack : container.getItems())
        {
            if (rune.isEmpty() && stack.is(PSTags.Items.NON_EMPTY_RUNES)) rune = stack.copy();
            if (item.isEmpty() && stack.is(PSTags.Items.RUNE_APPLICABLE)) item = stack.copy();
        }
        if (rune.isEmpty() || item.isEmpty()) return ItemStack.EMPTY;

        @Nullable ColorGradient gradient = null;
        @Nullable ColorGradient existing = ColorGradient.ofNbt(item.getTag());

        if (rune.getItem() instanceof RuneItem runeItem) gradient = runeItem.gradient();
        if (gradient == null || gradient == existing) return ItemStack.EMPTY;

        ColorGradient.addToItem(gradient, item);
        item.setCount(1);
        return item;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) { return x * y >= 2; }

    @Override
    public RecipeSerializer<?> getSerializer() { return PSRecipeSerializers.RUNE_CRAFTING.get(); }

}
