package github.poscard8.poscardsskills.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.module.BaseModule;
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

public class BrilliantShardItem extends ItemWithDescription {

    public BrilliantShardItem(Properties property) { super(property); }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(this);
        if (key == null) return List.of();

        MutableComponent description0 = Component.translatable(String.format("tooltip.%s.%s_desc", key.getNamespace(), key.getPath()))
                .withStyle(Style.EMPTY.withColor(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION)));
        Component description1 = Component.translatable(String.format("tooltip.%s.%s_desc_2", key.getNamespace(), key.getPath()))
                .withStyle(Style.EMPTY.withColor(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION)));

        Component newDescription = description0.append(description1);
        return PSComponents.split(newDescription);
    }

    protected Multimap<Attribute, AttributeModifier> attributeModifier(int count) {

        UUID uuid = UUID.fromString("CB3F55D3-5C64-4F38-A497-9C13A33DB535");
        return ImmutableMultimap.of(BaseModule.Attributes.WISDOM.get(), new AttributeModifier(uuid, () -> "Poscard's Skills: Shard modifier", count, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        return slot == EquipmentSlot.OFFHAND && stack.is(this) ? attributeModifier(stack.getCount()) : super.getAttributeModifiers(slot, stack);
    }

}
