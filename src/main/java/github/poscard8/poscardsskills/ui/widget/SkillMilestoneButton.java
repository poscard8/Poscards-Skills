package github.poscard8.poscardsskills.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import github.poscard8.poscardsskills.secret.Secrets;
import github.poscard8.poscardsskills.skill.SkillMilestone;
import github.poscard8.poscardsskills.ui.screen.SkillScreen;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * Button of a skill milestone. Gives rewards when clicked. Texture is based on the milestone's state.
 */
@OnlyIn(Dist.CLIENT)
public class SkillMilestoneButton extends ButtonWithTooltip {

    public final SkillTab parent;

    final SkillMilestone milestone;
    final ItemStack icon;

    public SkillMilestoneButton(Screen screen, SkillMilestone milestone, int x, int y, SkillTab parent) {

        super(screen, x, y, 18, 18, getTextureX(milestone), getTextureY(milestone), 18, SkillScreen.TEXTURE_LOCATION, 256, 256,
                press(milestone), () -> PSComponents.milestoneComponents(milestone));

        this.parent = parent;
        this.milestone = milestone;
        this.icon = milestone.skill.icon;
    }

    protected static int getTextureX(SkillMilestone milestone) { return milestone.level % 5 == 0 ? 194 : 176; }

    protected static int getTextureY(SkillMilestone milestone) {

        return milestone.state == SkillMilestone.State.LOCKED ? 0 : milestone.state == SkillMilestone.State.UNLOCKING ? 36 : 72;
    }

    protected static OnPress press(SkillMilestone milestone) {

        return button -> {

            ServerPlayer player = PSUtils.getServerPlayer();

            milestone.claimRewards(player);
            Secrets.handleSkill(player, milestone);
        };
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {

        RenderSystem.enableBlend();
        super.render(poseStack, mouseX, mouseY, delta);
        RenderSystem.disableBlend();

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getItemRenderer().renderAndDecorateItem(icon, x + 1, y + 1);

        if (milestone.canClaimRewards()) {

            int color = ChatFormatting.LIGHT_PURPLE.getColor() != null ? ChatFormatting.LIGHT_PURPLE.getColor() : 0xFFFFFF;
            Component component = PSComponents.singleUnclaimedMark();

            PoseStack pose = new PoseStack();
            pose.translate(0.0D, 0.0D, getBlitOffset() + 300.0F);
            MultiBufferSource.BufferSource buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            minecraft.font.drawInBatch(component, x + 18 - minecraft.font.width(component), y, color, true, pose.last().pose(), buffersource, false, 0, 15728880);
            buffersource.endBatch();
        }

        if (milestone.level % 5 == 0) {

            ItemStack stack = icon.copy();
            stack.setCount(milestone.level);

            minecraft.getItemRenderer().renderGuiItemDecorations(minecraft.font, stack, x + 1, y + 1);
        }
        RenderSystem.enableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int typeId) { return isInside((int) mouseX, (int) mouseY) && super.mouseClicked(mouseX, mouseY, typeId); }

    /**
     * @return Is inside the skill tab.
     */
    @Override
    public boolean isInside(int mouseX, int mouseY) {

        boolean xCheck = mouseX >= parent.leftPos && mouseX < parent.leftPos + 160;
        boolean yCheck = mouseY >= parent.topPos && mouseY < parent.topPos + 96;

        return xCheck && yCheck;
    }

    public void move(int deltaX) { x += deltaX; }

}
