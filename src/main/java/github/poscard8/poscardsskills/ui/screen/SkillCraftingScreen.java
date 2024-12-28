package github.poscard8.poscardsskills.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import github.poscard8.poscardsskills.ui.menu.SkillCraftingMenu;
import github.poscard8.poscardsskills.ui.widget.SkillRecipeWidget;
import github.poscard8.poscardsskills.ui.widget.SkillTab;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Crafting screen inspired by villager trading screen.
 */
@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public final class SkillCraftingScreen extends AbstractContainerScreen<SkillCraftingMenu> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/skill_crafting.png");

    public LocalPlayer localPlayer = null;
    public ServerPlayer player = null;
    public SkillData skillData = null;
    public List<SkillRecipe> allRecipes;

    double currentY = 0;
    double deltaY = 0;

    double scrollerY = 0;
    double scrollerDeltaY = 0;

    boolean isDragging;

    public SkillCraftingScreen(SkillCraftingMenu menu, Inventory inventory, Component title) {

        super(menu, inventory, title);
        this.titleLabelY -= 16;
        this.inventoryLabelY += SkillCraftingMenu.Y_OFFSET;
    }

    @Override
    protected void init() {

        super.init();

        this.localPlayer = PSUtils.getLocalPlayer();
        this.player = PSUtils.getServerPlayer();

        assert player != null;

        this.skillData = SkillData.of(player);
        this.allRecipes = SkillRecipe.getValues();
        this.isDragging = false;

        addRenderableWidget(SkillTab.backButton(leftPos + 10, y0()));

        for (SkillRecipe recipe : allRecipes) {

            int row = allRecipes.indexOf(recipe);
            if (row != -1) addWidget(new SkillRecipeWidget(this, recipe, x0(),  20 * row + y0()));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        handleButtons(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.blit(TEXTURE_LOCATION, leftPos, topPos - 16, 0, 0, imageWidth, 196);
        guiGraphics.blit(TEXTURE_LOCATION, x2(), scrollerY(), 216, 0, 11, 15);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        super.renderTooltip(guiGraphics, mouseX, mouseY);
        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof SkillRecipeWidget skillRecipeWidget) skillRecipeWidget.renderTooltip(guiGraphics, mouseX, mouseY);
        });
    }

    public void handleButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        guiGraphics.enableScissor(x0(), y0(), x1(), y1());

        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof SkillRecipeWidget skillRecipeWidget) skillRecipeWidget.render(guiGraphics, mouseX, mouseY, delta);
        });

        guiGraphics.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int i) {

        isDragging = canScroll() && mouseX >= x2() && mouseX < x3() && mouseY >= y0() && mouseY < y1();
        return super.mouseClicked(mouseX, mouseY, i);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int i, double deltaX, double deltaY) {

        double y = mouseY + deltaY;

        if (isDragging) {

            double scrollerOldY = scrollerY;
            scrollerY = Mth.clamp(y - y0() - 8, 0, scrollerHeight());
            scrollerDeltaY = scrollerY - scrollerOldY;
            moveWidgets(false);
        }
        return super.mouseDragged(mouseX, mouseY, i, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolls) {

        scrollerDeltaY = -scrolls;
        moveWidgets(canScroll());
        return super.mouseScrolled(mouseX, mouseY, scrolls);
    }

    public void moveWidgets(boolean moveScroller) {

        deltaY = scrollerHeightRatio(true) * scrollerDeltaY;

        int newY = (int) Math.round(Mth.clamp(currentY + deltaY, 0, fullHeight()));
        int delta = (int) Math.round(currentY - newY);

        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof SkillRecipeWidget skillRecipeWidget) skillRecipeWidget.move(delta);
        });

        currentY = newY;
        deltaY = 0;

        if (moveScroller) scrollerY = Mth.clamp(scrollerY + scrollerDeltaY, 0, scrollerHeight());
        scrollerDeltaY = 0;
    }

    public boolean canScroll() { return fullHeight() > 0; }

    public int x0() { return leftPos + 30; }

    public int x1() { return leftPos + 134; }

    public int x2() { return leftPos + 135; }

    public int x3() { return leftPos + 146; }

    public int y0() { return topPos + 2; }

    public int y1() { return topPos + 82; }

    public int scrollerY() { return y0() + (int) Math.round(scrollerY); }

    public int fullHeight() { return allRecipes.size() * 20 - (y1() - y0()); }

    public int scrollerHeight() { return 65; }

    public float scrollerHeightRatio(boolean inverted) { return inverted ? (float) fullHeight() / scrollerHeight() : (float) scrollerHeight() / fullHeight(); }

}
