package github.poscard8.poscardsskills.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.component.PSComponents;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class RareDropCategory implements IRecipeCategory<RareDrop> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/jei_rare_drop.png");

    private final IDrawable background;
    private final IDrawable icon;

    public RareDropCategory(IGuiHelper guiHelper) {

        this.background = guiHelper.createDrawable(TEXTURE_LOCATION, 0, 0, 160, 125);
        this.icon = guiHelper.createDrawable(TEXTURE_LOCATION, 160, 0, 16, 16);
    }

    @Override
    public RecipeType<RareDrop> getRecipeType() { return RareDrop.TYPE; }

    @Override
    public Component getTitle() { return PSComponents.rareDrops(); }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RareDrop recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.OUTPUT, 72, 54).addItemStack(recipe.getItemDisplay());
    }

    @Override
    public void draw(RareDrop recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {

        IRecipeCategory.super.draw(recipe, recipeSlotsView, poseStack, mouseX, mouseY);

        Screen screen = Minecraft.getInstance().screen;
        boolean xCheck = mouseX >= 144 && mouseX < 160;
        boolean yCheck = mouseY >= 0 && mouseY < 16;

        if (screen != null && xCheck && yCheck) {

            screen.renderTooltip(poseStack, PSComponents.rareDropsDescriptionComponents(), Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }

}