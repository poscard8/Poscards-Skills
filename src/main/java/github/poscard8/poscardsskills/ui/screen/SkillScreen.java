package github.poscard8.poscardsskills.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.ui.menu.SkillMenu;
import github.poscard8.poscardsskills.ui.widget.SkillTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class SkillScreen extends AbstractContainerScreen<SkillMenu> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/skill.png");

    public Skill skill;
    public SkillInstance instance;

    public SkillTab tab;
    public int deltaX = 0;

    public SkillScreen(SkillMenu menu, Inventory inventory, Component title) {

        super(menu, inventory, title);
        this.titleLabelY -= 24;
        this.inventoryLabelY += SkillMenu.Y_OFFSET;
    }

    @Override
    protected void init() {

        super.init();

        String string = title.getContents().toString();
        String key = string.split("'")[1];
        String[] parts = key.split("\\.");

        skill = Skill.byKey(new ResourceLocation(parts[1], parts[2]));
        instance = SkillInstance.of(Objects.requireNonNull(Minecraft.getInstance().player), skill);
        tab = new SkillTab(this, instance, leftPos + 8, topPos - 6);
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

        tab.renderBg(poseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        RenderSystem.enableBlend();

        blit(poseStack, leftPos, topPos - 24, 0, 0, 176, 212);
        RenderSystem.disableBlend();
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
        fill(poseStack, leftPos + 8, topPos + 90, -4680, topPos - 6, -16777216);
        fill(poseStack, 4680, topPos + 90, leftPos + 168, topPos - 6, -16777216);
        RenderSystem.colorMask(true, true, true, true);

        poseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.depthFunc(515);
        poseStack.popPose();

        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof Widget widget) widget.render(poseStack, mouseX, mouseY, delta);
        });
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int i, double deltaX, double deltaY) {

        if (mouseX >= leftPos && mouseX < leftPos + 176 && mouseY >= topPos - 6 && mouseY < topPos + 90) {

            this.deltaX = -(int) deltaX;
            moveTab();
        }
        return super.mouseDragged(mouseX, mouseY, i, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolls) {

        deltaX = (int) (-6 * scrolls);
        moveTab();
        return super.mouseScrolled(mouseX, mouseY, scrolls);
    }

    public <T extends GuiEventListener & Widget & NarratableEntry> void newWidget(T widget) { addWidget(widget); }

    private void moveTab() {

        tab.move(deltaX);
        deltaX = 0;
    }


}
