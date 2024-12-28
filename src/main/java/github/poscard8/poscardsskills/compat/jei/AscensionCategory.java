package github.poscard8.poscardsskills.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSItems;
import github.poscard8.poscardsskills.util.component.PSComponents;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AscensionCategory implements IRecipeCategory<Ascension> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/jei_ascension.png");

    final IDrawable background;
    final IDrawable icon;

    public AscensionCategory(IGuiHelper guiHelper) {

        this.background = guiHelper.createDrawable(TEXTURE_LOCATION, 0, 0, 160, 96);
        this.icon = guiHelper.createDrawableItemStack(PSItems.BRILLIANT_CATALYST.get().getDefaultInstance());
    }

    @Override
    public RecipeType<Ascension> getRecipeType() { return PoscardsSkillsJEIPlugin.ASCENSION_TYPE; }

    @Override
    public Component getTitle() { return PSComponents.ascensionTitle(); }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    /**
     * Recipe setter that displays up to 6 outputs.
     */
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Ascension recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.CATALYST, 72, 61).addItemStack(recipe.catalyst().getDefaultInstance());

        for (int i = 0; i < 6; i++) {

            try {

                Ascension.AscensionDrop drop = recipe.drops().get(i);

                int x = i < 3 ? 111 : 129;
                int y = 22 + 18 * (i % 3);

                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStacks(drop.getItemDisplays());

            } catch (Exception ignored) {}
        }
    }

    /**
     * Method to write 'Skill Progression' on the menu.
     */

    @Override
    public void draw(Ascension recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {

        IRecipeCategory.super.draw(recipe, recipeSlotsView, poseStack, mouseX, mouseY);

        Font font = Minecraft.getInstance().font;
        Component line1 = PSComponents.skillTitle();
        Component line2 = PSComponents.progress();
        int x1 = 31 - font.width(line1) / 2;
        int x2 = 31 - font.width(line2) / 2;
        int y1 = 39;
        int y2 = 48;

        font.draw(poseStack, line1, x1, y1, 0);
        font.draw(poseStack, line2, x2, y2, 0);
    }

}
