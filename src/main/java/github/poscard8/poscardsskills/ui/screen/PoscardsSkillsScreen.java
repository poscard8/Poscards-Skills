package github.poscard8.poscardsskills.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.secret.Secrets;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.ui.menu.SkillCraftingMenu;
import github.poscard8.poscardsskills.ui.widget.ButtonWithTooltip;
import github.poscard8.poscardsskills.ui.widget.SkillMenuButton;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

/**
 * Main screen of the mod.
 */
@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public final class PoscardsSkillsScreen extends AbstractContainerScreen<PoscardsSkillsMenu> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/poscardsskills.png");

    public ItemStack playerHead = null;
    public LocalPlayer localPlayer = null;
    public ServerPlayer player = null;

    public PoscardsSkillsScreen(PoscardsSkillsMenu menu, Inventory inventory, Component title) {

        super(menu, inventory, title);
        titleLabelY -= 2;
    }

    @Override
    protected void init() {

        super.init();

        assert minecraft != null;
        localPlayer = minecraft.player;
        player = PSUtils.getServerPlayer();
        playerHead = new ItemStack(Items.PLAYER_HEAD);

        playerHead.getOrCreateTag().putString("SkullOwner", localPlayer.getName().getString());

        if (player != null) {

            SkillData skillData = skillData();
            assert skillData != null;

            for (Skill skill : PoscardsSkills.getSkillHandler().getValues()) {

                SkillInstance instance = skillData.getSkill(skill);
                addRenderableWidget(new SkillMenuButton(this, instance, leftPos + 7 + skill.column * 18, topPos + 15 + skill.row * 18));
            }
        }
        addRenderableWidget(profileButton());
        addRenderableWidget(journeyButton());
        addRenderableWidget(skillCraftingMenuButton());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {

        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);

        blit(poseStack, leftPos, topPos - 2, 0, 0, imageWidth, 168);
        blit(poseStack, leftPos - 30, topPos + 8, 0, 168, 30, 68);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {

        RenderSystem.depthFunc(519);
        super.renderTooltip(poseStack, mouseX, mouseY);
        children().forEach(guiEventListener -> {

            if (guiEventListener instanceof ButtonWithTooltip button) button.renderTooltip(poseStack, mouseX, mouseY);
        });
        RenderSystem.disableDepthTest();
    }

    ImageButton profileButton() {

        return new ButtonWithTooltip(this, leftPos - 23, topPos + 15, 18, 18, 176, 36, 18, TEXTURE_LOCATION, 256, 256,
                button -> {

                    ServerPlayer player = PSUtils.getServerPlayer();
                    if (player != null) Secrets.PROFILE_BUTTON.unlock(player);
                },
                () -> PSComponents.statComponents(skillData())) {

            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {

                super.render(poseStack, mouseX, mouseY, delta);
                Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(playerHead, x + 1, y + 1);
            }
        };
    }

    ImageButton journeyButton() {

        return new ButtonWithTooltip(this, leftPos - 23, topPos + 33, 18, 18, 176, 72, 18, TEXTURE_LOCATION, 256, 256, button -> {}, () -> PSComponents.journeyComponents(skillData()));
    }

    ImageButton skillCraftingMenuButton() {

        return new ButtonWithTooltip(this, leftPos - 23, topPos + 51, 18, 18, 176, 108, 18, TEXTURE_LOCATION, 256, 256,
                button -> Objects.requireNonNull(PSUtils.getServerPlayer()).openMenu(SkillCraftingMenu.PROVIDER),
                () -> List.of(PSComponents.skillCrafting()));
    }


    @Override
    public boolean keyPressed(int key, int scan, int modifier) {

        assert minecraft != null;

        if (PoscardsSkills.KEY_POSCARDS_SKILLS_MENU.matches(key, scan)) {

            minecraft.setScreen(null);
            minecraft.mouseHandler.grabMouse();
            return true;

        } else return super.keyPressed(key, scan, modifier);
    }

    @Nullable
    public SkillData skillData() { return player != null ? SkillData.of(player) : null; }

}
