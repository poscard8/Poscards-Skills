package github.poscard8.poscardsskills.ui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.ui.menu.SkillMenu;
import github.poscard8.poscardsskills.ui.screen.PoscardsSkillsScreen;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Optional;

public class SkillMenuButton extends ImageButton {

    private final SkillInstance instance;

    public SkillMenuButton(SkillInstance instance, int x, int y, Screen screen) {

        super(x, y, 18, 18, 176, 0, 18, PoscardsSkillsScreen.TEXTURE_LOCATION, 256, 256, press(instance), tooltip(screen, instance), CommonComponents.EMPTY);
        this.instance = instance;
    }

    private static OnPress press(SkillInstance instance) {

        return button -> {

            ServerPlayer serverPlayer = PSUtils.getServerPlayer();
            if (serverPlayer != null) serverPlayer.openMenu(SkillMenu.provider(instance));
        };
    }

    private static OnTooltip tooltip(Screen screen, SkillInstance instance) {

        return (button, poseStack, mouseX, mouseY) -> screen.renderTooltip(poseStack, PSComponents.skillInstanceComponents(instance), Optional.empty(), mouseX, mouseY);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {

        Minecraft minecraft = Minecraft.getInstance();

        super.render(poseStack, mouseX, mouseY, delta);

        ItemStack icon = instance.skill.icon.copy();
        if (instance.isMaxLevel()) icon.enchant(Enchantments.UNBREAKING, 1);

        minecraft.getItemRenderer().renderAndDecorateFakeItem(icon, x + 1, y + 1);

        if (instance.hasUnclaimedRewards()) {

            int color = ChatFormatting.LIGHT_PURPLE.getColor() != null ? ChatFormatting.LIGHT_PURPLE.getColor() : 0xFFFFFF;
            Component component = PSComponents.singleUnclaimedMark();

            PoseStack pose = new PoseStack();
            pose.translate(0.0D, 0.0D, getBlitOffset() + 300.0F);
            MultiBufferSource.BufferSource buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            minecraft.font.drawInBatch(component, x + 18 - minecraft.font.width(component), y, color, true, pose.last().pose(), buffersource, false, 0, 15728880);
            buffersource.endBatch();
        }
    }

}
