package github.poscard8.poscardsskills.util.component;

import github.poscard8.poscardsskills.util.item.PSStyles;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.UnaryOperator;

/**
 * Enchantment text colors. Only used in mod enchantments.
 */
public enum EnchantmentColor {

    BLACK(component -> component.withStyle(ChatFormatting.BLACK)),
    DARK_BLUE(component -> component.withStyle(ChatFormatting.DARK_BLUE)),
    DARK_GREEN(component -> component.withStyle(ChatFormatting.DARK_GREEN)),
    DARK_AQUA(component -> component.withStyle(ChatFormatting.DARK_AQUA)),
    DARK_RED(component -> component.withStyle(ChatFormatting.DARK_RED)),
    DARK_PURPLE(component -> component.withStyle(ChatFormatting.DARK_PURPLE)),
    GOLD(component -> component.withStyle(ChatFormatting.GOLD)),
    GRAY(component -> component.withStyle(ChatFormatting.GRAY)),
    DARK_GRAY(component -> component.withStyle(ChatFormatting.DARK_GRAY)),
    BLUE(component -> component.withStyle(ChatFormatting.BLUE)),
    GREEN(component -> component.withStyle(ChatFormatting.GREEN)),
    AQUA(component -> component.withStyle(ChatFormatting.AQUA)),
    RED(component -> component.withStyle(ChatFormatting.RED)),
    LIGHT_PURPLE(component -> component.withStyle(ChatFormatting.LIGHT_PURPLE)),
    YELLOW(component -> component.withStyle(ChatFormatting.YELLOW)),
    WHITE(component -> component.withStyle(ChatFormatting.WHITE)),
    ELEGANT(component -> component.withStyle(PSStyles.ELEGANT)),
    ETHEREAL(component -> component.withStyle(PSStyles.ETHEREAL)),
    CLASSICAL(component -> component.withStyle(PSStyles.CLASSICAL));

    final UnaryOperator<MutableComponent> applyFunction;

    EnchantmentColor(UnaryOperator<MutableComponent> applyFunction) { this.applyFunction = applyFunction; }

    public MutableComponent applyTo(MutableComponent component) { return applyFunction.apply(component); }

}
