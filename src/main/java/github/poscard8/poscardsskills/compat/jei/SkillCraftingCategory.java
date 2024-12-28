package github.poscard8.poscardsskills.compat.jei;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import github.poscard8.poscardsskills.util.component.PSComponents;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillCraftingCategory implements IRecipeCategory<SkillRecipe> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/jei_skill_crafting.png");

    final IDrawable background;
    final IDrawable icon;

    public SkillCraftingCategory(IGuiHelper guiHelper) {

        this.background = guiHelper.createDrawable(TEXTURE_LOCATION, 0, 0, 144, 96);
        this.icon = guiHelper.createDrawable(TEXTURE_LOCATION, 144, 0, 16, 16);
    }

    @Override
    public RecipeType<SkillRecipe> getRecipeType() { return PoscardsSkillsJEIPlugin.SKILL_RECIPE_TYPE; }

    @Override
    public Component getTitle() { return PSComponents.skillCrafting(); }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    @Nullable
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SkillRecipe recipe, IFocusGroup focuses) {

        ItemStack input2 = recipe.input2 != null ? recipe.input2 : ItemStack.EMPTY;

        builder.addSlot(RecipeIngredientRole.INPUT, 20, 40).addItemStack(recipe.input1);
        builder.addSlot(RecipeIngredientRole.INPUT, 46, 40).addItemStack(input2);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 40).addItemStack(recipe.output);
    }

    /**
     * Method to write the skill requisite on the menu.
     */
    @Override
    public void draw(SkillRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        Font font = Minecraft.getInstance().font;
        int offset = font.width(PSComponents.requisite(recipe)) / 2;

        guiGraphics.drawString(font, PSComponents.requisite(recipe, ChatFormatting.DARK_GRAY), 72 - offset, 82, 0, false);
    }

}
