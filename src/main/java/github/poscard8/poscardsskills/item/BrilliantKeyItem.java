package github.poscard8.poscardsskills.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import github.poscard8.poscardsskills.module.BaseModule;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BrilliantKeyItem extends ItemWithDescription {

    private final int additionalLuck;

    public BrilliantKeyItem(Properties property) { this(property, 50); }

    public BrilliantKeyItem(Properties property, int additionalLuck) {

        super(property);
        this.additionalLuck = additionalLuck;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        UUID uuid = UUID.fromString("CB3F55D3-5C64-4F38-A497-9C13A33DB548");
        return slot == EquipmentSlot.MAINHAND ?
                ImmutableMultimap.of(BaseModule.Attributes.CHEST_LUCK.get(), new AttributeModifier(uuid, () -> "Poscard's Skills: Key modifier", additionalLuck, AttributeModifier.Operation.ADDITION)) :
                super.getAttributeModifiers(slot, stack);
    }

}
