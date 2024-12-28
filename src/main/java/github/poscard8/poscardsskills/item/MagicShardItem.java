package github.poscard8.poscardsskills.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Fundamental items that grant wisdom on offhand.
 * <p>Wisdom enables players to gain more skill xp.</p>
 * <p>+1 Wisdom: +%1 skill xp.</p>
 */
public class MagicShardItem extends ItemWithDescription {

    public final Supplier<Double> wisdomGetter;

    public MagicShardItem(Properties property, Supplier<Double> wisdomGetter) {

        super(property);
        this.wisdomGetter = wisdomGetter;
    }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
        if (key == null) return List.of();

        MutableComponent description0 = Component.translatable(String.format("tooltip.%s.magic_shard_desc_1", key.getNamespace()))
                .withStyle(Style.EMPTY.withColor(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION)));
        Component description1 = Component.translatable(String.format("tooltip.%s.magic_shard_desc_2", key.getNamespace()))
                .withStyle(Style.EMPTY.withColor(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION)));

        Component newDescription = description0.append(description1);
        return PSComponents.split(newDescription);
    }

    protected Multimap<Attribute, AttributeModifier> attributeModifier(int count) {

        double boost = count * getWisdom();
        UUID uuid = UUID.fromString("CB3F55D3-5C64-4F38-A497-9C13A33DB535");
        return ImmutableMultimap.of(PSAttributes.WISDOM.get(), new AttributeModifier(uuid, () -> "Poscard's Skills: Shard modifier", boost, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        return slot == EquipmentSlot.OFFHAND && stack.is(this) ? attributeModifier(stack.getCount()) : super.getAttributeModifiers(slot, stack);
    }

    public double getWisdom() { return wisdomGetter.get(); }

}
