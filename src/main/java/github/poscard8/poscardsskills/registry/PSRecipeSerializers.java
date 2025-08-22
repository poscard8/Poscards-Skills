package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.recipe.RuneCraftingClearRecipe;
import github.poscard8.poscardsskills.recipe.RuneCraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class PSRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> ALL = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PoscardsSkills.ID);

    public static final RegistryObject<RecipeSerializer<RuneCraftingRecipe>> RUNE_CRAFTING = ALL.register("rune_crafting", () -> new SimpleCraftingRecipeSerializer<>(RuneCraftingRecipe::new));
    public static final RegistryObject<RecipeSerializer<RuneCraftingClearRecipe>> RUNE_CRAFTING_CLEAR = ALL.register("rune_crafting_clear", () -> new SimpleCraftingRecipeSerializer<>(RuneCraftingClearRecipe::new));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
