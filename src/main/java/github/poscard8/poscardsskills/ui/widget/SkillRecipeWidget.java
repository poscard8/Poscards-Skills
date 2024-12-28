package github.poscard8.poscardsskills.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import github.poscard8.poscardsskills.ui.screen.SkillCraftingScreen;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 2 button widget for skill recipes. 1 for display, 1 for crafting.
 */
@OnlyIn(Dist.CLIENT)
public class SkillRecipeWidget extends AbstractWidget {

    public final SkillCraftingScreen screen;
    public final SkillRecipe recipe;
    public final ServerPlayer player;
    public final boolean unlocked;

    public final ShowcaseButton showcaseButton;
    public final CraftingButton craftingButton;

    public SkillRecipeWidget(SkillCraftingScreen screen, SkillRecipe recipe, int x, int y) {

        super(x, y, 104, 20, Component.empty());

        this.screen = screen;
        this.recipe = recipe;
        this.player = screen.player;
        this.unlocked = recipe.isUnlockedFor(player);

        this.showcaseButton = new ShowcaseButton(this);
        this.craftingButton = new CraftingButton(this);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {

        showcaseButton.render(poseStack, mouseX, mouseY, delta);
        craftingButton.render(poseStack, mouseX, mouseY, delta);
    }

    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {

        showcaseButton.renderTooltip(poseStack, mouseX, mouseY);
        craftingButton.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

        if (mouseWithinArea(mouseX, mouseY) && craftingButton.isHoveredOrFocused()) {

            craftingButton.onClick(mouseX, mouseY);
            craftingButton.playDownSound(Minecraft.getInstance().getSoundManager());
        }
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {}

    public boolean mouseWithinArea(double mouseX, double mouseY) { return mouseX >= screen.x0() && mouseX < screen.x1() && mouseY >= screen.y0() && mouseY < screen.y1(); }

    public boolean isWithinArea() { return x >= screen.x0() && x < screen.x1() && y >= screen.y0() && y < screen.y1(); }
    
    @Override
    protected boolean clicked(double mouseX, double mouseY) { return isWithinArea() && super.clicked(mouseX, mouseY); }

    public void move(int deltaY) {

        y += deltaY;
        showcaseButton.y = y;
        craftingButton.y = y;
    }


    public static class ShowcaseButton extends ImageButton {

        public final SkillRecipeWidget parent;

        public ShowcaseButton(SkillRecipeWidget parent) {

            super(parent.x, parent.y, 84, 20, 0, 196, 20, SkillCraftingScreen.TEXTURE_LOCATION, 256, 256, button -> {});
            this.parent = parent;
        }

        protected void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {

            if (isHovered && parent.mouseWithinArea(mouseX, mouseY)) {

                SkillCraftingScreen screen = parent.screen;
                SkillRecipe recipe = parent.recipe;
                Player player = parent.player;

                int x = mouseX - screen.x0();
                List<Component> tooltips;

                if (x < 20) tooltips = recipe.input1.getTooltipLines(player, TooltipFlag.Default.NORMAL);
                else if (x < 40) tooltips = recipe.input2 != null ? recipe.input2.getTooltipLines(player, TooltipFlag.Default.NORMAL) : null;
                else if (x < 64) tooltips = null;
                else tooltips = recipe.output.getTooltipLines(player, TooltipFlag.Default.NORMAL);

                RenderSystem.depthFunc(519);
                if (tooltips != null) parent.screen.renderTooltip(poseStack, tooltips, Optional.empty(), mouseX, mouseY);
                RenderSystem.disableDepthTest();

            }
        }

        @Override
        public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {

            super.render(poseStack, mouseX, mouseY, delta);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            SkillRecipe recipe = parent.recipe;
            Font font = Minecraft.getInstance().font;
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            itemRenderer.renderAndDecorateItem(recipe.input1, x + 2, y + 2);
            itemRenderer.renderAndDecorateItem(recipe.output, x + 66, y + 2);
            if (recipe.input2 != null) itemRenderer.renderAndDecorateItem(recipe.input2, x + 22, y + 2);

            itemRenderer.renderGuiItemDecorations(font, recipe.input1, x + 2, y + 2);
            itemRenderer.renderGuiItemDecorations(font, recipe.output, x + 66, y + 2);
            if (recipe.input2 != null) itemRenderer.renderGuiItemDecorations(font, recipe.input2, x + 22, y + 2);
        }

        @Override
        protected boolean isValidClickButton(int typeId) { return false; }


    }

    public static class CraftingButton extends ButtonWithTooltip {

        public final SkillRecipeWidget parent;

        public CraftingButton(SkillRecipeWidget parent) {

            super(parent.screen, parent.x + 84, parent.y, 20, 20, getXStart(parent), 0, getYDiff(parent),
                    SkillCraftingScreen.TEXTURE_LOCATION, 256, 256, onPress(parent), componentGetter(parent));
            this.parent = parent;
        }

        private static int getXStart(SkillRecipeWidget parent) { return parent.unlocked ? 176 : 196; }

        private static int getYDiff(SkillRecipeWidget parent) { return parent.unlocked ? 20 : 0; }

        private static OnPress onPress(SkillRecipeWidget parent) {

            return button ->  {

                ServerPlayer player = parent.player;
                if (player == null) return;

                if (Screen.hasShiftDown()) {

                    parent.recipe.craftStack(player);

                } else parent.recipe.craftSingle(player);
            };
        }

        private static Supplier<List<Component>> componentGetter(SkillRecipeWidget parent) {

            return () -> {

                Component component = parent.unlocked ? Screen.hasShiftDown() ? PSComponents.craftStack() : PSComponents.craftSingle() : PSComponents.requisite(parent.recipe);
                List<Component> components = new ArrayList<>();

                components.add(component);
                return components;
            };
        }

        @Override
        public boolean isInside(int mouseX, int mouseY) { return parent.mouseWithinArea(mouseX, mouseY); }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int typeId) { return parent.mouseWithinArea(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, typeId); }

        @Override
        protected boolean isValidClickButton(int typeId) { return parent.unlocked; }

        @Override
        public void playDownSound(@NotNull SoundManager soundManager) { if (parent.unlocked) super.playDownSound(soundManager); }

    }


}
