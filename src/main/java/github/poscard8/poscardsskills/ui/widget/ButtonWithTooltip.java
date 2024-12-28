package github.poscard8.poscardsskills.ui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Button that can display multiple texts when hovered.
 */
@OnlyIn(Dist.CLIENT)
public class ButtonWithTooltip extends ImageButton {

    protected Supplier<List<Component>> componentGetter;

    public ButtonWithTooltip(int x, int y, int width, int height, int xTexStart, int yTexStart, int yTexDiff,
                             ResourceLocation location, int textureWidth, int textureHeight, OnPress onPress, Supplier<List<Component>> componentGetter) {

        super(x, y, width, height, xTexStart, yTexStart, yTexDiff, location, textureWidth, textureHeight, onPress);
        this.componentGetter = componentGetter;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderTooltip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (isHovered() && isInside(mouseX, mouseY)) guiGraphics.renderTooltip(Minecraft.getInstance().font, componentGetter.get(), Optional.empty(), mouseX, mouseY);
    }

    /**
     * @return Is inside a given area. Since there is no area defined yet, returns true.
     */
    public boolean isInside(int mouseX, int mouseY) { return true; }


}
