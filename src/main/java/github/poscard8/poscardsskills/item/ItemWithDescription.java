package github.poscard8.poscardsskills.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;

/**
 * Utility item class. Item descriptions are displayed only if
 * the player is holding shift.
 */
@ParametersAreNonnullByDefault
public abstract class ItemWithDescription extends Item
{
    public ItemWithDescription(Properties property) { super(property); }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag)
    {
        components.addAll(getDescriptionTexts(stack, level, flag));
        super.appendHoverText(stack, level, components, flag);
    }

    public abstract Collection<Component> getDescriptionTexts(ItemStack stack, @Nullable Level level, TooltipFlag flag);

}
