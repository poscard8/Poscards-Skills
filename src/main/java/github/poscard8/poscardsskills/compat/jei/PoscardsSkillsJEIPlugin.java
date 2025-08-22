package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.recipe.RuneCraftingClearRecipe;
import github.poscard8.poscardsskills.recipe.RuneCraftingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@JeiPlugin
public class PoscardsSkillsJeiPlugin implements IModPlugin
{
    static final ResourceLocation ID = PoscardsSkills.asResource("jei_plugin");

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration)
    {
        registration.getCraftingCategory().addCategoryExtension(RuneCraftingRecipe.class, RuneCraftingExtension::new);
        registration.getCraftingCategory().addCategoryExtension(RuneCraftingClearRecipe.class, RuneCraftingClearExtension::new);
    }

    @Override
    public ResourceLocation getPluginUid() { return ID; }

}
