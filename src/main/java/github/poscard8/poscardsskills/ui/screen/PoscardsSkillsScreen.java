package github.poscard8.poscardsskills.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillHandler;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.ui.menu.SkillCraftingMenu;
import github.poscard8.poscardsskills.ui.widget.SkillMenuButton;
import github.poscard8.poscardsskills.util.PSUtils;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class PoscardsSkillsScreen extends AbstractContainerScreen<PoscardsSkillsMenu> {

    public static final ResourceLocation TEXTURE_LOCATION = PoscardsSkills.asResource("textures/gui/poscardsskills.png");

    public ItemStack playerHead = null;
    public Player player = null;

    public PoscardsSkillsScreen(PoscardsSkillsMenu menu, Inventory inventory, Component title) {

        super(menu, inventory, title);
        titleLabelY -= 2;
    }

    @Override
    protected void init() {

        super.init();

        assert minecraft != null;
        player = minecraft.player;
        playerHead = new ItemStack(Items.PLAYER_HEAD);

        playerHead.getOrCreateTag().putString("SkullOwner", player.getName().getString());

        if (player != null) {

            for (Skill skill : PoscardsSkills.getSkillHandler().getValues()) {

                SkillInstance instance = SkillData.of(player).getSkill(skill);

                SkillHandler.SkillPosition position = skill.position();
                addRenderableWidget(new SkillMenuButton(instance, leftPos + 43 + position.column * 18, topPos + 15 + position.row * 18, this));
            }
        }
        addRenderableWidget(profileButton());
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
    }

    protected ImageButton profileButton() {

        return new ImageButton(leftPos + 7, topPos + 24, 18, 18, 176, 0, 18, TEXTURE_LOCATION, 256, 256, button -> {},
                (button, poseStack, mouseX, mouseY) -> renderTooltip(poseStack, PSComponents.statComponents(skillData()), Optional.empty(), mouseX, mouseY),
                Component.empty()) {

            @Override
            public void render(PoseStack poseStack2, int mouseX2, int mouseY2, float delta) {

                super.render(poseStack2, mouseX2, mouseY2, delta);
                Minecraft.getInstance().getItemRenderer().renderAndDecorateFakeItem(playerHead, x + 1, y + 1);
            }
        };
    }



    protected ImageButton skillCraftingMenuButton() {

        return new ImageButton(leftPos + 7, topPos + 42, 18, 18, 176, 36, 18, TEXTURE_LOCATION, 256, 256,
                button -> Objects.requireNonNull(PSUtils.getServerPlayer()).openMenu(SkillCraftingMenu.PROVIDER),
                (button, poseStack, mouseX, mouseY) -> renderTooltip(poseStack, PSComponents.skillCrafting(), mouseX, mouseY),
                Component.empty());
    }


    @Override
    public boolean keyPressed(int key, int scan, int modifier) {

        assert minecraft != null;

        if (PoscardsSkills.KEY_SKILL_MENU.matches(key, scan)) {

            minecraft.setScreen(null);
            minecraft.mouseHandler.grabMouse();
            return true;

        } else return super.keyPressed(key, scan, modifier);
    }

    @Nullable
    public SkillData skillData() { return player != null ? SkillData.of(player) : null; }

}
