package github.poscard8.poscardsskills.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillRecipe;
import github.poscard8.poscardsskills.ui.menu.SkillCraftingMenu;
import github.poscard8.poscardsskills.ui.widget.SkillRecipeWidget;
import github.poscard8.poscardsskills.ui.widget.SkillTab;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SkillCraftingScreen extends AbstractContainerScreen<SkillCraftingMenu> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/skill_crafting.png");

    public LocalPlayer player = null;
    public SkillData skillData = null;
    public List<SkillRecipe> allRecipes;

    int currentY = 0;
    int deltaY = 0;

    public SkillCraftingScreen(SkillCraftingMenu menu, Inventory inventory, Component title) {

        super(menu, inventory, title);
        this.titleLabelY -= 16;
        this.inventoryLabelY += SkillCraftingMenu.Y_OFFSET;
    }

    @Override
    protected void init() {

        super.init();

        this.player = PSUtils.getLocalPlayer();
        this.skillData = SkillData.of(player);
        this.allRecipes = SkillData.getAllRecipes();

        addRenderableWidget(SkillTab.backButton(leftPos + 10, y0(), this));

        for (SkillRecipe recipe : allRecipes) {

            int row = allRecipes.indexOf(recipe);
            if (row != -1) addWidget(new SkillRecipeWidget(this, recipe, x0(),  20 * row + y0()));
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {

        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        handleButtons(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);

        blit(poseStack, leftPos, topPos - 16, 0, 0, imageWidth, 196);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {

        RenderSystem.depthFunc(519);
        super.renderTooltip(poseStack, mouseX, mouseY);
        RenderSystem.disableDepthTest();
    }

    public void handleButtons(PoseStack poseStack, int mouseX, int mouseY, float delta) {

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 950.0D);
        RenderSystem.enableDepthTest();

        RenderSystem.colorMask(false, false, false, false);
        fill(poseStack, x0(), y0(), x1(), -4680, -16777216);
        fill(poseStack, x0(), 4680, x1(), y1(), -16777216);
        RenderSystem.colorMask(true, true, true, true);

        poseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.depthFunc(515);
        poseStack.popPose();

        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof SkillRecipeWidget skillRecipeWidget) skillRecipeWidget.render(poseStack, mouseX, mouseY, delta);
        });
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int i, double deltaX, double deltaY) {

        if (mouseX >= x0() && mouseX < x1() && mouseY >= y0() && mouseY < y1()) {

            this.deltaY = (int) -deltaY;
            moveButtons();
        }
        return super.mouseDragged(mouseX, mouseY, i, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolls) {

        deltaY = (int) (-6 * scrolls);
        moveButtons();
        return super.mouseScrolled(mouseX, mouseY, scrolls);
    }

    public void moveButtons() {

        int fullHeight = allRecipes.size() * 20;
        int newY = Math.max(0, Math.min(currentY + deltaY, fullHeight - (y1() - y0())));

        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof SkillRecipeWidget skillRecipeWidget) skillRecipeWidget.move(currentY - newY);
        });
        currentY = newY;
        deltaY = 0;
    }

    public int x0() { return leftPos + 26; }

    public int x1() { return leftPos + 150; }

    public int y0() { return topPos + 2; }

    public int y1() { return topPos + 82; }


}
