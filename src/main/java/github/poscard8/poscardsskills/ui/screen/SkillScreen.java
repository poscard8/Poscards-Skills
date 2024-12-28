package github.poscard8.poscardsskills.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.ui.menu.SkillMenu;
import github.poscard8.poscardsskills.ui.widget.ButtonWithTooltip;
import github.poscard8.poscardsskills.ui.widget.SkillTab;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public final class SkillScreen extends AbstractContainerScreen<SkillMenu> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/skill.png");

    public Skill skill;
    public SkillInstance instance;
    public SkillTab tab;

    double deltaX = 0;

    public SkillScreen(SkillMenu menu, Inventory inventory, Component title) {

        super(menu, inventory, title);
        this.titleLabelY -= 24;
        this.inventoryLabelY += SkillMenu.Y_OFFSET;
    }

    @Override
    protected void init() {

        super.init();

        this.skill = menu.skill;

        ServerPlayer player = PSUtils.getServerPlayer();
        assert player != null;

        this.instance = SkillInstance.of(player, skill);
        this.tab = new SkillTab(this, instance, x0(), y0());
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

        tab.renderBg(guiGraphics);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();

        guiGraphics.blit(TEXTURE_LOCATION, leftPos, topPos - 24, 0, 0, 176, 212);
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        super.renderTooltip(guiGraphics, mouseX, mouseY);
        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof ButtonWithTooltip button) button.renderTooltip(guiGraphics, mouseX, mouseY);
        });
    }

    public void handleButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        guiGraphics.enableScissor(x0(), y0(), x1(), y1());

        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof AbstractWidget widget) widget.render(guiGraphics, mouseX, mouseY, delta);
        });

        guiGraphics.disableScissor();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolls) {

        deltaX = 9 * scrolls;
        moveTab();
        return super.mouseScrolled(mouseX, mouseY, scrolls);
    }

    public <T extends GuiEventListener & NarratableEntry> void newWidget(T widget) { addWidget(widget); }

    public void moveTab() {

        tab.move((int) Math.round(deltaX));
        deltaX = 0;
    }

    public int x0() { return leftPos + 8; }

    public int x1() { return leftPos + 168; }

    public int y0() { return topPos - 6; }

    public int y1() { return topPos + 90; }


}
