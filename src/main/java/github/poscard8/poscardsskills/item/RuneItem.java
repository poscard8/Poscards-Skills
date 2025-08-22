package github.poscard8.poscardsskills.item;

import github.poscard8.peritia.util.text.ColorGradient;
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
public class RuneItem extends ItemWithDescription
{
    protected final ColorGradient gradient;

    public RuneItem(Properties property, ColorGradient gradient)
    {
        super(property.rarity(gradient.asRarity()));
        this.gradient = gradient;
    }

    public ColorGradient gradient() { return gradient; }

    @Override
    public Collection<Component> getDescriptionTexts(ItemStack stack, @Nullable Level level, TooltipFlag flag)
    {
        Component fancyText = Component.translatable("item.poscardsskills.fancy.desc").withStyle(gradient());
        Component text = Component.translatable("item.poscardsskills.rune.desc", fancyText).withStyle(ChatFormatting.GRAY);

        return List.of(text);
    }

}
