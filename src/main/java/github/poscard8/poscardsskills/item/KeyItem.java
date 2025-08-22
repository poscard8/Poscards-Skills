package github.poscard8.poscardsskills.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import github.poscard8.peritia.registry.PeritiaAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class KeyItem extends ItemWithDescription
{
    protected static final UUID MODIFIER_UUID = UUID.fromString("B8470F59-CAD7-492C-AE1D-2F5CC4FC1B44");
    protected static final Supplier<String> MODIFIER_NAME = () -> "Poscard's Skills: Key modifier";

    protected final Supplier<Double> luckGetter;
    protected final Supplier<Double> wisdomGetter;

    public KeyItem(Properties property, Supplier<Double> luckGetter, Supplier<Double> wisdomGetter)
    {
        super(property);
        this.luckGetter = luckGetter;
        this.wisdomGetter = wisdomGetter;
    }

    public double getLuckValue() { return luckGetter.get(); }

    public double getWisdomValue() { return wisdomGetter.get(); }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        if (slot == EquipmentSlot.MAINHAND)
        {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();

            double luck = getLuckValue();
            double wisdom = getWisdomValue();

            if (luck != 0) builder.put(PeritiaAttributes.CHEST_LUCK.get(), new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, luck, AttributeModifier.Operation.ADDITION));
            if (wisdom != 0) builder.put(PeritiaAttributes.WISDOM.get(), new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, wisdom, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public Collection<Component> getDescriptionTexts(ItemStack stack, @Nullable Level level, TooltipFlag flag)
    {
        return List.of(Component.translatable("item.poscardsskills.key.desc").withStyle(ChatFormatting.GRAY));
    }

}
