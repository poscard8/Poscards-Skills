package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.secret.Secret;
import github.poscard8.poscardsskills.secret.Secrets;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Simple plugin with its own recipes and types.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@JeiPlugin
public class PoscardsSkillsJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = PoscardsSkills.asResource("jei_plugin");

    public static final RecipeType<SkillRecipe> SKILL_RECIPE_TYPE = RecipeType.create("poscardsskills", "skill_crafting", SkillRecipe.class);
    public static final RecipeType<Secret> SECRET_TYPE = RecipeType.create("poscardsskills", "secret", Secret.class);
    public static final RecipeType<Ascension> ASCENSION_TYPE = RecipeType.create("poscardsskills", "ascension", Ascension.class);

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        IModPlugin.super.registerRecipes(registration);
        registerModRecipes(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        IModPlugin.super.registerCategories(registration);

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new SkillCraftingCategory(guiHelper), new SecretCategory(guiHelper), new AscensionCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        IModPlugin.super.registerRecipeCatalysts(registration);
        for (ItemStack catalyst : Ascension.getCatalysts()) registration.addRecipeCatalyst(catalyst, ASCENSION_TYPE);
    }

    public void registerModRecipes(IRecipeRegistration registration) {

        registration.addRecipes(SKILL_RECIPE_TYPE, SkillRecipe.getValues());
        registration.addRecipes(SECRET_TYPE, Secrets.getSortedValues());
        registration.addRecipes(ASCENSION_TYPE, Ascension.getValues());
    }

    @Override
    @NotNull
    public ResourceLocation getPluginUid() { return ID; }

}
