package github.poscard8.poscardsskills.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.skill.SkillMilestone;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.ui.screen.SkillScreen;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SkillTab extends GuiComponent {

    public static final int fullWidth = getFullWidth();
    public static final int width = 160;
    public static final int height = 96;

    public final Minecraft minecraft = Minecraft.getInstance();
    public final SkillScreen parent;
    public final List<SkillMilestoneButton> buttons = new ArrayList<>();
    public final SkillInstance instance;

    public final ItemStack icon;
    public final ResourceLocation background;

    public final int leftPos;
    public final int topPos;

    private int currentX = 0;

    private static int getFullWidth() {

        int width = 1;

        for (int l = 1; l <= Skill.MAX_LEVEL; l++) { if (Set.of(3, 4, 8, 9).contains(l % 10)) ++width; }
        return width * 18 + 25;
    }

    public static ImageButton backButton(int leftPos, int topPos, Screen parent) {

        return new ImageButton(leftPos, topPos, 7, 11, 194, 0, 11, SkillScreen.TEXTURE_LOCATION, 256, 256,

                button -> {

                    ServerPlayer serverPlayer = PSUtils.getServerPlayer();
                    if (serverPlayer != null) serverPlayer.openMenu(PoscardsSkillsMenu.PROVIDER);
                },
                (button, poseStack, mouseX, mouseY) -> parent.renderTooltip(poseStack, PSComponents.back(), mouseX, mouseY),
                Component.empty());
    }

    public SkillTab(SkillScreen screen, SkillInstance instance, int leftPos, int topPos) {

        this.parent = screen;
        this.instance = instance;
        this.icon = instance.skill.icon;
        this.background = instance.skill.background != null ? instance.skill.background : new ResourceLocation("textures/block/stone.png");

        this.leftPos = leftPos;
        this.topPos = topPos;

        parent.newWidget(backButton());

        int row = 1;
        int column = 0;

        List<SkillMilestone> milestones = instance.milestones.subList(1, Skill.MAX_LEVEL + 1);
        int deltaX = 0;
        for (SkillMilestone milestone : milestones) {

            int i = milestone.level % 10;

            if (Set.of(3, 4, 8, 9).contains(i)) ++column;
            if (i < 3) ++row;
            if (i > 4 && i < 8) --row;

            boolean isMaxLevel = milestone.isUnlocked() && milestone.level == Skill.MAX_LEVEL;

            if (milestone.state == SkillMilestone.State.UNLOCKING || isMaxLevel) deltaX = column * 18 - 59;

            SkillMilestoneButton button = new SkillMilestoneButton(milestone, leftPos + 12 + column * 18, topPos + 12 + row * 18, screen, this);
            buttons.add(button);
            parent.newWidget(button);
        }
        move(deltaX);
    }

    public void renderBg(PoseStack poseStack) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, background);

        int first = currentX % 16;

        for (int x = 0; x < 10; x++) {

            for (int y = 0; y < 6; y++) blit(poseStack, leftPos + 16 * x, topPos + 16 * y, first, 0, 16, 16, 16, 16);
        }
    }

    public void move(int deltaX) {

        int newX = Math.max(0, Math.min(currentX + deltaX, fullWidth - width));

        buttons.forEach(button -> button.move(currentX - newX));
        currentX = newX;
    }

    private ImageButton backButton() { return backButton(leftPos + 2, topPos + 2, parent); }




}
