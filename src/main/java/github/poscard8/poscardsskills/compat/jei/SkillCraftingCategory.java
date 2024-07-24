package github.poscard8.poscardsskills.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SkillCraftingCategory implements IRecipeCategory<SkillRecipe> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/jei_skill_crafting.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SkillCraftingCategory(IGuiHelper guiHelper) {

        this.background = guiHelper.createDrawable(TEXTURE_LOCATION, 0, 0, 144, 96);
        this.icon = guiHelper.createDrawable(TEXTURE_LOCATION, 144, 0, 16, 16);
    }

    @Override
    public RecipeType<SkillRecipe> getRecipeType() { return SkillRecipe.JEI_TYPE; }

    @Override
    public Component getTitle() { return PSComponents.skillCrafting(); }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SkillRecipe recipe, IFocusGroup focuses) {

        ItemStack input2 = recipe.input2 != null ? recipe.input2 : ItemStack.EMPTY;

        builder.addSlot(RecipeIngredientRole.INPUT, 20, 40).addItemStack(recipe.input1);
        builder.addSlot(RecipeIngredientRole.INPUT, 46, 40).addItemStack(input2);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 40).addItemStack(recipe.output);
    }

    @Override
    public void draw(SkillRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);

        Font font = Minecraft.getInstance().font;
        float offset = ((float) font.width(PSComponents.requisite(recipe))) / 2.0F;
        font.draw(stack, PSComponents.requisite(recipe, ChatFormatting.DARK_GRAY), 72 - offset, 82, 0);
    }

}
