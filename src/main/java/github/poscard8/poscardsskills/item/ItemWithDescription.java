package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Utility item class. Item descriptions are displayed only if
 * the player is holding shift.
 */
public abstract class ItemWithDescription extends Item {

    public ItemWithDescription(Properties property) { super(property); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> components, @NotNull TooltipFlag flag) {

        components.add(PSComponents.holdShift());

        if(Screen.hasShiftDown()) {

            components.add(PSComponents.space());
            components.addAll(getDescriptionComponents());
        }
        super.appendHoverText(stack, level, components, flag);
    }

    protected Collection<Component> getDescriptionComponents() {

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
        if (key == null) return List.of();

        Component description = Component.translatable(String.format("tooltip.%s.%s_desc_1", key.getNamespace(), key.getPath()))
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        return PSComponents.split(description);
    }
}
