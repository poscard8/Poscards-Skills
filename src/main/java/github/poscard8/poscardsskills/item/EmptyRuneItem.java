package github.poscard8.poscardsskills.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;

@ParametersAreNonnullByDefault
public class EmptyRuneItem extends ItemWithDescription
{
    public EmptyRuneItem(Properties property) { super(property); }

    @Override
    public Collection<Component> getDescriptionTexts(ItemStack stack, @Nullable Level level, TooltipFlag flag)
    {
        Component text = Component.translatable("item.poscardsskills.empty_rune.desc").withStyle(ChatFormatting.GRAY);
        return List.of(text);
    }

}
