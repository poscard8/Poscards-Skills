package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSItems;
import github.poscard8.poscardsskills.secret.Secret;
import github.poscard8.poscardsskills.util.component.PSComponents;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SecretCategory implements IRecipeCategory<Secret> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/jei_secret.png");

    final IDrawable background;
    final IDrawable icon;

    public SecretCategory(IGuiHelper guiHelper) {

        this.background = guiHelper.createDrawable(TEXTURE_LOCATION, 0, 0, 144, 96);
        this.icon = guiHelper.createDrawableItemStack(PSItems.SECRET.get().getDefaultInstance());
    }

    @Override
    public RecipeType<Secret> getRecipeType() { return PoscardsSkillsJEIPlugin.SECRET_TYPE; }

    @Override
    public Component getTitle() { return PSComponents.secretTitle(); }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    /**
     * Simple recipe setter. See {@link Secret} for more info on display and reward items.
     */
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Secret secret, IFocusGroup focuses) {

        ItemStack display = secret.getDisplayItem();
        ItemStack reward = secret.getRewardItem();

        builder.addSlot(RecipeIngredientRole.INPUT, 33, 40).addItemStack(display);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 91, 40).addItemStack(reward);
    }

}
