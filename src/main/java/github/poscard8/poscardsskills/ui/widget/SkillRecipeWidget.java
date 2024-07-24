package github.poscard8.poscardsskills.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import github.poscard8.poscardsskills.ui.screen.SkillCraftingScreen;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class SkillRecipeWidget extends AbstractWidget {

    public final SkillCraftingScreen screen;
    public final SkillRecipe recipe;
    public final Player player;
    public final boolean unlocked;

    public final ShowcaseButton showcaseButton;
    public final CraftSingleButton craftSingleButton;
    public final CraftStackButton craftStackButton;

    public SkillRecipeWidget(SkillCraftingScreen screen, SkillRecipe recipe, int x, int y) {

        super(x, y, 124, 20, Component.empty());

        this.screen = screen;
        this.recipe = recipe;
        this.player = screen.player;
        this.unlocked = recipe.isUnlockedFor(player);

        this.showcaseButton = new ShowcaseButton(this);
        this.craftSingleButton = new CraftSingleButton(this);
        this.craftStackButton = new CraftStackButton(this);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {

        showcaseButton.render(poseStack, mouseX, mouseY, delta);
        craftSingleButton.render(poseStack, mouseX, mouseY, delta);
        craftStackButton.render(poseStack, mouseX, mouseY, delta);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

        if (mouseWithinArea(mouseX, mouseY) && craftSingleButton.isHoveredOrFocused()) {

            craftSingleButton.onClick(mouseX, mouseY);
            craftSingleButton.playDownSound(Minecraft.getInstance().getSoundManager());
        }
        if (mouseWithinArea(mouseX, mouseY) && craftStackButton.isHoveredOrFocused()) {

            craftStackButton.onClick(mouseX, mouseY);
            craftSingleButton.playDownSound(Minecraft.getInstance().getSoundManager());
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}

    public boolean mouseWithinArea(double mouseX, double mouseY) {

        return mouseX >= screen.x0() && mouseX < screen.x1() && mouseY >= screen.y0() && mouseY < screen.y1();
    }

    public void move(int deltaY) {

        this.y += deltaY;
        showcaseButton.y += deltaY;
        craftSingleButton.y += deltaY;
        craftStackButton.y += deltaY;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {}


    public static class ShowcaseButton extends Button {

        public final SkillRecipeWidget parent;

        public ShowcaseButton(SkillRecipeWidget parent) {

            super(parent.x, parent.y, 84, 20, Component.empty(), button -> {}, tooltip(parent));
            this.parent = parent;
        }

        private static OnTooltip tooltip(SkillRecipeWidget parent) {

            SkillCraftingScreen screen = parent.screen;
            SkillRecipe recipe = parent.recipe;
            Player player = parent.player;

            return (button, poseStack, mouseX, mouseY) -> {

                if (parent.mouseWithinArea(mouseX, mouseY)) {

                    int x = mouseX - screen.x0();
                    List<Component> tooltips;

                    if (x < 20) tooltips = recipe.input1.getTooltipLines(player, TooltipFlag.Default.NORMAL);
                    else if (x < 40) tooltips = recipe.input2 != null ? recipe.input2.getTooltipLines(player, TooltipFlag.Default.NORMAL) : null;
                    else if (x < 64) tooltips = null;
                    else tooltips = recipe.output.getTooltipLines(player, TooltipFlag.Default.NORMAL);

                    RenderSystem.depthFunc(519);
                    if (tooltips != null) screen.renderTooltip(poseStack, tooltips, Optional.empty(), mouseX, mouseY);
                    RenderSystem.disableDepthTest();

                }
            };
        }

        @Override
        public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {

            super.render(poseStack, mouseX, mouseY, delta);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SkillCraftingScreen.TEXTURE_LOCATION);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(515);

            blit(poseStack, x + 43, y + 5, 236, 0, 18, 11);

            RenderSystem.disableDepthTest();

            SkillRecipe recipe = parent.recipe;

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            Font font = Minecraft.getInstance().font;

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

    public static class CraftSingleButton extends ImageButton {

        public final SkillRecipeWidget parent;

        public CraftSingleButton(SkillRecipeWidget parent) {

            super(parent.x + 84, parent.y, 20, 20, getXStart(parent), 0, getYDiff(parent),
                    SkillCraftingScreen.TEXTURE_LOCATION, 256, 256, onPress(parent), onTooltip(parent), Component.empty());
            this.parent = parent;
        }

        private static int getXStart(SkillRecipeWidget parent) { return parent.unlocked ? 176 : 216; }

        private static int getYDiff(SkillRecipeWidget parent) { return parent.unlocked ? 20 : 0; }

        private static OnPress onPress(SkillRecipeWidget parent) { return button -> parent.recipe.craftSingle(parent.player); }

        private static OnTooltip onTooltip(SkillRecipeWidget parent) {

            return (button, poseStack, mouseX, mouseY) -> {

                if (parent.mouseWithinArea(mouseX, mouseY)) {

                    Component component = parent.unlocked ? PSComponents.craftSingle() : PSComponents.requisite(parent.recipe);

                    RenderSystem.depthFunc(519);
                    parent.screen.renderTooltip(poseStack, component, mouseX, mouseY);
                    RenderSystem.disableDepthTest();
                }
            };
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) { return parent.mouseWithinArea(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, type); }

        @Override
        protected boolean isValidClickButton(int typeId) { return parent.unlocked; }

        @Override
        public void playDownSound(SoundManager soundManager) { if (parent.unlocked) super.playDownSound(soundManager); }

    }

    public static class CraftStackButton extends ImageButton {

        public final SkillRecipeWidget parent;

        public CraftStackButton(SkillRecipeWidget parent) {
            super(parent.x + 104, parent.y, 20, 20, getXStart(parent), 0, getYDiff(parent),
                    SkillCraftingScreen.TEXTURE_LOCATION, 256, 256, onPress(parent), onTooltip(parent), Component.empty());
            this.parent = parent;
        }

        private static int getXStart(SkillRecipeWidget parent) { return parent.unlocked ? 196 : 216; }

        private static int getYDiff(SkillRecipeWidget parent) { return parent.unlocked ? 20 : 0; }

        private static OnPress onPress(SkillRecipeWidget parent) { return button -> parent.recipe.craftStack(parent.player); }

        private static OnTooltip onTooltip(SkillRecipeWidget parent) {

            return (button, poseStack, mouseX, mouseY) -> {

                if (parent.mouseWithinArea(mouseX, mouseY)) {

                    Component component = parent.unlocked ? PSComponents.craftStack() : PSComponents.requisite(parent.recipe);

                    RenderSystem.depthFunc(519);
                    parent.screen.renderTooltip(poseStack, component, mouseX, mouseY);
                    RenderSystem.disableDepthTest();
                }
            };
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int type) { return parent.mouseWithinArea(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, type); }

        @Override
        protected boolean isValidClickButton(int typeId) { return parent.unlocked; }

        @Override
        public void playDownSound(SoundManager soundManager) { if (parent.unlocked) super.playDownSound(soundManager); }

    }


}
